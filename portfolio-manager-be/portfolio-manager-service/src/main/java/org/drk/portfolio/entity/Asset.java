package org.drk.portfolio.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.sql.Date;

@Entity
@Table(name = "asset") // Change from "assets" to "asset"
@Data
public class Asset {
    @Id
    private String id;
    
    @Column(name = "asset_name")
    private String assetName;

    @Column(name = "asset_code")
    private String assetCode;
    
    // Remove the unused 'name' field since we now use assetName
    
    @Column(name = "current_price", precision = 38, scale = 8)
    private BigDecimal currentPrice;

    @Column(name = "purchase_price", precision = 38, scale = 8)
    private BigDecimal purchasePrice;
    
    @Column(name = "quantity")
    private BigDecimal quantity;
    
    @Column(name = "purchase_date")
    private Date purchaseDate;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private AssetType type;
}