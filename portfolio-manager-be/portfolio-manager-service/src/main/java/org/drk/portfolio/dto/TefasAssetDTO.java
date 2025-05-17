package org.drk.portfolio.dto;

public class TefasAssetDTO {
    private final String fundName;
    private final double fundPrice;
    private final double averageCost;
    private final double returnPercentage;
    private final double shareCount;
    private final double totalAmount;

    public TefasAssetDTO(String fundName, double fundPrice, double averageCost,
                         double returnPercentage, double shareCount, double totalAmount) {
        this.fundName = fundName;
        this.fundPrice = fundPrice;
        this.averageCost = averageCost;
        this.returnPercentage = returnPercentage;
        this.shareCount = shareCount;
        this.totalAmount = totalAmount;
    }

    // Getters
    public String getFundName() {
        return fundName;
    }

    public double getFundPrice() {
        return fundPrice;
    }

    public double getAverageCost() {
        return averageCost;
    }

    public double getReturnPercentage() {
        return returnPercentage;
    }

    public double getShareCount() {
        return shareCount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
}