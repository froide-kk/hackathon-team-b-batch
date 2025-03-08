package com.oshiel.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.util.Date;

/**
 * 通知記事テーブルエンティティ
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "notification_article")
public class NotificationArticleEntity {

    /**
     * 通知記事ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_article_id")
    private Integer notificationArticleId;

    /**
     * 記事ID
     */
    @Column(name = "article_id", insertable = false, updatable = false)
    private Integer articleId;

    /**
     * oshiel会員ID
     */
    @Column(name = "oshiel_id", insertable = false, updatable = false)
    private Integer oshielId;

    @ManyToOne
    @JoinColumn(name = "article_id", referencedColumnName = "article_id")
    private ArticleEntity article;

    @ManyToOne
    @JoinColumn(name = "oshiel_id", referencedColumnName = "oshiel_id")
    private MemberEntity member;

    /**
     * CREATE日時
     */
    @CreatedDate
    @Column(name = "createdate", updatable = false)
    private Date createDate;

    /**
     * UPDATE日時
     */
    @LastModifiedDate
    @Column(name = "updatedate")
    private Date updateDate;

}
