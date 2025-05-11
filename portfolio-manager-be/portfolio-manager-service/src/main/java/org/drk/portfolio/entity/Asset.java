package org.drk.portfolio.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.sql.Date;

@Entity
@Table(name = "assets")
@Data
public class Asset {
    @Id
    private String id;
    private String name;
    private BigDecimal currentPrice;
    private BigDecimal purchasePrice;
    private BigDecimal quantity;
    private Date purchaseDate;
    
    @Enumerated(EnumType.STRING)
    private AssetType type;
}