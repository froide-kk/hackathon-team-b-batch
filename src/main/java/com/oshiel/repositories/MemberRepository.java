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
     * 通知フラグによる会員ID取得
     */
    public List<MemberEntity> findByNotificationFlag(int flg);

}
