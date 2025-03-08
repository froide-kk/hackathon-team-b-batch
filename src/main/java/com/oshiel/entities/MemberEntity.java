package com.oshiel.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

/**
 * 会員テーブルエンティティ
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "member")
public class MemberEntity {

    /**
     * oshiel会員ID
     */
    @Id
    @Column(name = "oshiel_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer oshielId;

    /**
     * slackID
     */
    @Column(name = "slack_id")
    private String slackId ;

    /**
     * slack Token
     */
    @Column(name = "slack_token")
    private String slackToken;

    /**
     * slack expire
     */
    @Column(name = "slack_expire")
    private Date slackExpire;

    /**
     * 通知フラグ
     */
    @Column(name = "notification_flag")
    private int notificationFlag;

    /**
     * 通知時間
     */
    @Column(name = "notification_time")
    private String notificationTime;

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
