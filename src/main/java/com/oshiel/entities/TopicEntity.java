package com.oshiel.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

/**
 * トピックテーブルエンティティ
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "topic")
public class TopicEntity {

    /**
     * oshiel会員ID
     */
    @Id
    @Column(name = "oshiel_id")
    private Integer oshielId;

    /**
     * トピックID
     */
    @Id
    @Column(name = "topic_id")
    private Integer topicId;

    /**
     * トピック
     */
    @Column(name = "topic_detail")
    private String topicDetail;

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
