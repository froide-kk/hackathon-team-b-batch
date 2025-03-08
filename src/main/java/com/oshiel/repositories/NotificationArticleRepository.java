package com.oshiel.repositories;

import com.oshiel.entities.NotificationArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 通知記事テーブルリポジトリ
 */
@Repository
public interface NotificationArticleRepository extends JpaRepository<NotificationArticleEntity, Integer> {

}
