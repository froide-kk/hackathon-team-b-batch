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
     * トピックID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "topic_id")
    private Integer topicId;

    /**
     * oshiel会員ID
     */
    @Column(name = "oshiel_id", insertable = false, updatable = false)
    private Integer oshielId;

    @ManyToOne
    @JoinColumn(name = "oshiel_id", referencedColumnName = "oshiel_id")
    private MemberEntity member;

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
