package org.drk.portfolio.repository;

import org.drk.portfolio.entity.Asset;
import org.drk.portfolio.entity.AssetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetRepository extends JpaRepository<Asset, String> {
    @Query("SELECT a FROM Asset a WHERE a.type = :type")
    List<Asset> findByType(@Param("type") AssetType type);

    @Query("SELECT DISTINCT a.assetCode FROM Asset a WHERE a.type = :type")
    List<String> findDistinctAssetCodesByType(@Param("type") AssetType type);
}