package com.oshiel.repositories;

import com.oshiel.entities.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 記事テーブルリポジトリ
 */
@Repository
public interface ArticleRepository extends JpaRepository<ArticleEntity, Integer> {
}
