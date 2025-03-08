package com.oshiel.runners;

import com.oshiel.entities.ArticleEntity;
import com.oshiel.entities.MemberEntity;
import com.oshiel.entities.NotificationArticleEntity;
import com.oshiel.entities.TopicEntity;
import com.oshiel.repositories.MemberRepository;
import com.oshiel.services.SlackNotificationService;
import com.oshiel.util.logUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Component
public class SlackNotificationRunner implements ApplicationRunner {

    /**
     * Slack通知サービス
     */
    @Autowired
    private SlackNotificationService slackNotificationService;

    // 毎時1回実行
    @Scheduled(cron = "0 0 * * * ?", zone = "Asia/Tokyo")
    public void executeScheduledTask() throws Exception {
        logUtil.info("---------- バッチ実行開始 ----------");
        try {
            // 通知オン かつ 配信時間対象の会員一覧取得
            List<MemberEntity> oshielMemberList =  slackNotificationService.getOshielMember();

            // 会員ごとにトピック取得、最新ニュース記事取得、Slack通知
            for (MemberEntity oshielMember: oshielMemberList) {

                // トピック取得
                String topic = slackNotificationService.getTopic(oshielMember.getOshielId());

                // ニュース一覧
                ArticleEntity article;

                if (topic != null) {
                    // トピック取得ありの場合 ニュース記事取得
                    article = slackNotificationService.getNews(oshielMember, topic);

                    // 取得記事がnullではない場合 Slack通知
                    if (article != null) {
                        // Slack通知
                        slackNotificationService.slackNotification(oshielMember, article);
                    }
                }
            }
        }catch(Exception e){
            // エラーログ出力
            logUtil.error(e.getMessage());
        }
        logUtil.info("---------- バッチ実行終了 ----------");
    }

    /**
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        // アプリケーション起動時の処理が必要であればここに記述
        // バッチが作成できなかった場合 手動実行
        executeScheduledTask();
    }
}
