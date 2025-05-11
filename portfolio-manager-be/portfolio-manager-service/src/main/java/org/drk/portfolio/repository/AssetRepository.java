package org.drk.portfolio.repository;

import org.drk.portfolio.entity.Asset;
import org.drk.portfolio.entity.AssetType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssetRepository extends JpaRepository<Asset, String> {
    List<Asset> findByType(AssetType type);
}