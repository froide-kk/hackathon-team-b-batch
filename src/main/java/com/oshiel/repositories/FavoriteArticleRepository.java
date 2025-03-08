package com.oshiel.repositories;

import com.oshiel.entities.FavoriteArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * お気に入り記事テーブルリポジトリ
 */
@Repository
public interface FavoriteArticleRepository extends JpaRepository<FavoriteArticleEntity, Integer> {

}
