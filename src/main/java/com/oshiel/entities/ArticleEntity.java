package com.oshiel.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

/**
 * 記事テーブルエンティティ
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "article")
public class ArticleEntity {

    /**
     * 記事ID
     */
    @Id
    @Column(name = "article_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer article_id;

    /**
     * タイトル
     */
    @Column(name = "title")
    private String title;

    /**
     * URL
     */
    @Column(name = "url")
    private String url;

    /**
     * 説明
     */
    @Column(name = "description")
    private String description;

    /**
     * 画像
     */
    @Column(name = "urlToImage")
    private String urlToImage;

    /**
     * 公開日
     */
    @Column(name = "publishedAt")
    private String publishedAt;

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
