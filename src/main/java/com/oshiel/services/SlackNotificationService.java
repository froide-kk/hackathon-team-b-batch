package com.oshiel.services;

import com.oshiel.commons.Constants;
import com.oshiel.entities.ArticleEntity;
import com.oshiel.entities.MemberEntity;
import com.oshiel.entities.NotificationArticleEntity;
import com.oshiel.entities.TopicEntity;
import com.oshiel.repositories.ArticleRepository;
import com.oshiel.repositories.MemberRepository;
import com.oshiel.repositories.NotificationArticleRepository;
import com.oshiel.repositories.TopicRepository;
import com.oshiel.util.logUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SlackNotificationService {

    /**
     * ニュースAPIKey
     */
    @Value("${news.api.key}")
    private String newsApikey;

    /**
     * ニュースAPIエンドポイント
     */
    @Value("${news.api.endpoint}")
    private String newsApiEndpoint;

    /**
     * Slack DM取得APIエンドポイント
     */
    @Value("${slack.client.url.dm}")
    private String SlackDMUrl;

    /**
     * Slack メッセージ送信
     */
    @Value("${slack.client.url.notification}")
    private String SlackNotificationUrl;

    /**
     * Slackボットトークン
     */
    @Value("${slack.client.token}")
    private String SlackBotToken;

    /**
     * 会員テーブルリポジトリ
     */
    @Autowired
    private MemberRepository memberRepository;

    /**
     * トピックテーブルリポジトリ
     */
    @Autowired
    private TopicRepository topicRepository;

    /**
     * 通知記事テーブルリポジトリ
     */
    @Autowired
    private NotificationArticleRepository notificationArticleRepository;

    /**
     * 記事テーブルリポジトリ
     */
    @Autowired
    private ArticleRepository articleRepository;

    /**
     * 通知オン かつ 現在時刻が配信時刻の会員一覧を取得
     *
     * @return
     */
    public List<MemberEntity> findByNotificationFlag(String time) {
        return this.memberRepository.findByNotificationFlagAndNotificationTime(Constants.NotificationValue.notification_on, time);
    }

    /**
     * トピック取得
     *
     * @return
     */
    public TopicEntity findByOshielId(Integer memberId) {
        return this.topicRepository.findByOshielId(memberId);
    }

    /**
     * 会員一覧取得
     *
     * @return
     */
    public List<MemberEntity> getOshielMember() {

        // 現在時刻を取得
        LocalTime currentTime = LocalTime.now();

        // 時刻をフォーマットするパターンを指定
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH");

        // フォーマットに従って時刻を文字列に変換
        String formattedTime = currentTime.format(formatter);

        // 通知オンの会員一覧取得
        List<MemberEntity> oshielMemberList = this.findByNotificationFlag(formattedTime);

        logUtil.info("会員リスト" + oshielMemberList);

        return oshielMemberList;
    }

    /**
     * トピック取得
     */
    public String getTopic(Integer memberId) {
        logUtil.info("---------- oshile会員" + memberId + "のトピック取得開始 ----------");
        String topic = null;
        // トピック取得
        TopicEntity topicDetail = this.findByOshielId(memberId);

        // トピックがnullの場合 通知スキップ
        // トピックテーブルいつ追加する？トピック設定しない限りオブジェクトは作成しない？
        if (topicDetail == null) {
            logUtil.info("トピック未設定");
            topic = null;
        } else {
            logUtil.info("トピック設定あり" + topicDetail.getTopicDetail());
            topic = topicDetail.getTopicDetail();
        }
        logUtil.info("---------- oshile会員" + memberId + "のトピック取得終了 ----------");
        return topic;
    }

    /**
     * 最新ニュース記事取得
     *
     * @param topic
     * @throws IOException
     */
    public ArticleEntity getNews(MemberEntity oshielMember, String topic) throws IOException {
        try {
            logUtil.info("---------- " + topic + "の記事取得開始 ----------");

            // 最新ニュース記事取得
            RestTemplate restTemplate = new RestTemplate();

            String newsApiUrl = UriComponentsBuilder.fromHttpUrl(newsApiEndpoint)
                    .queryParam("q", topic)
                    .queryParam("sortBy", "publishedAt")
                    .queryParam("apiKey", newsApikey)
                    .build(false)
                    .toUriString();

            logUtil.info("ニュースURL" + newsApiUrl);

            String newsResult = restTemplate.getForObject(newsApiUrl, String.class);
            logUtil.info("ニュース記事取得" + newsResult);

            JSONObject root = new JSONObject(newsResult);

            String resStatus = null;
            Integer totalResults = null;
            String title = null;
            String description = null;
            String url = null;
            String urlToImage = null;
            String publishedAt = null;

            ArticleEntity article = new ArticleEntity();

            resStatus = root.getString("status");
            totalResults = root.getInt("totalResults");

            // ニュース記事取得失敗 または 取得記事が0件
            if (!"ok".equals(resStatus) || totalResults == 0) {
                logUtil.info("取得記事がありません。");
                return null;
            }

            JSONArray articlesObject = root.getJSONArray("articles");

            for (int i = 0; i < articlesObject.length(); i++) {

                JSONObject arrayElement = articlesObject.getJSONObject(i);

                title = arrayElement.getString("title");

                if (!arrayElement.isNull("description")) {
                    description = arrayElement.getString("description");
                } else {
                    description = null;
                }

                url = arrayElement.getString("url");

                if (!arrayElement.isNull("urlToImage")) {
                    urlToImage = arrayElement.getString("urlToImage");
                } else {
                    urlToImage = null;
                }

                publishedAt = arrayElement.getString("publishedAt");

                NotificationArticleEntity notificationArticle = new NotificationArticleEntity();

                if (i == 0) {
                    // 最新記事のみ記事テーブルへ登録
                    article.setDescription(description);
                    article.setPublishedAt(publishedAt);
                    article.setTitle(title);
                    article.setUrl(url);
                    article.setUrlToImage(urlToImage);
                    articleRepository.save(article);

                    // 最新記事を通知記事テーブルへ登録
                    notificationArticle.setArticleId(article.getArticle_id());
                    notificationArticle.setOshielId(oshielMember.getOshielId());
                    notificationArticle.setArticle(article);
                    notificationArticle.setMember(oshielMember);
                    logUtil.info("通知記事テーブル登録値" + notificationArticle);
                    notificationArticleRepository.save(notificationArticle);
                }
            }
            logUtil.info("---------- " + topic + "の記事取得終了 ----------");
            return article;
        } catch (Exception e) {
            logUtil.error(e.getMessage());
            return null;
        }
    }

    /**
     * Slack通知
     * @param oshielMember
     * @param article
     */
    public void slackNotification(MemberEntity oshielMember, ArticleEntity article) {
        logUtil.info("---------- Slack通知開始 ----------");

        String slackDMId = null;

        // DMチャンネルID取得
        slackDMId = getSlackDMId(oshielMember.getSlackToken(), oshielMember.getSlackId());

        // DMチャンネルにSlack通知
        sendSlackMessage(slackDMId, article, oshielMember.getSlackToken());

        logUtil.info("---------- Slack通知終了 ----------");
    }

    /**
     * SlackのDMID取得
     * @param slackId
     * @return
     */
    private String getSlackDMId(String slackToken, String slackId) {
        // HTTPリクエストの設定
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // ユーザートークン
//        headers.setBearerAuth(slackToken);
        // ボットトークン
        headers.setBearerAuth(SlackBotToken);

        // リクエストボディの作成
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("users", slackId);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        // RestTemplateを使用してリクエストを送信
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.postForEntity(SlackDMUrl, entity, Map.class);

        logUtil.info("チャンネルID取得APIレスポンス" + response);

        // チャネルIDの取得
        Map<String, Object> responseBody = response.getBody();
        String channelId = (String) ((Map<String, Object>) responseBody.get("channel")).get("id");
        logUtil.info("チャンネルID" + channelId);
        return channelId;
    }

    private void sendSlackMessage(String slackDMId, ArticleEntity article, String slackToken){
        // HTTPリクエストの設定
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // ユーザートークン
//        headers.setBearerAuth("slackToken");
        // ボットトークン
        headers.setBearerAuth(SlackBotToken);

        // リクエストボディの作成
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("channel", slackDMId);
        requestBody.put("blocks", new Object[]{
                new HashMap<String, Object>() {{
                    put("type", "section");
                    put("text", new HashMap<String, String>() {{
                        put("type", "mrkdwn");
                        put("text", "*<" + article.getUrl() + "|" + article.getTitle().toUpperCase() + ">*");
                    }});
                }},
                new HashMap<String, Object>() {{
                    put("type", "image");
                    put("image_url", article.getUrlToImage());
                    put("alt_text", article.getTitle());
                }}
        });

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        // RestTemplateを使用してリクエストを送信
        RestTemplate restTemplate = new RestTemplate();
        String response = String.valueOf(restTemplate.postForEntity(SlackNotificationUrl, entity, String.class));
        logUtil.info("通知レスポンス" + response);
    }
}