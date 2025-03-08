package com.oshiel.repositories;

import com.oshiel.entities.MemberEntity;
import com.oshiel.entities.TopicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 会員テーブルリポジトリ
 */
@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Integer> {

    /**
     * 通知フラグ かつ 現在時刻が配信時刻の会員一覧取得
     */
    public List<MemberEntity> findByNotificationFlagAndNotificationTime(int flg, String time);

}
