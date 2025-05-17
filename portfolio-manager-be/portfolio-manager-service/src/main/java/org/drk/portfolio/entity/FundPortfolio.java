package org.drk.portfolio.entity;

public class FundPortfolio {
    private String fundName;
    private String fundCode; // (örneğin, ANT, BVZ)
    private double fundPrice;
    private double averageCost;
    private double returnValue; // Getiri (boşsa 0 olarak varsayalım)
    private double shares; // Pay
    private double blockedShares; // Blokeli Pay Sayısı
    private double amount; // Tutar
    private String accountNumber; // Hesap No

    // Constructor
    public FundPortfolio(String fundName, String fundCode, double fundPrice, double averageCost,
                         double returnValue, double shares, double blockedShares, double amount, String accountNumber) {
        this.fundName = fundName;
        this.fundCode = fundCode;
        this.fundPrice = fundPrice;
        this.averageCost = averageCost;
        this.returnValue = returnValue;
        this.shares = shares;
        this.blockedShares = blockedShares;
        this.amount = amount;
        this.accountNumber = accountNumber;
    }

    // Getters and Setters
    public String getFundName() { return fundName; }
    public void setFundName(String fundName) { this.fundName = fundName; }
    public String getFundCode() { return fundCode; }
    public void setFundCode(String fundCode) { this.fundCode = fundCode; }
    public double getFundPrice() { return fundPrice; }
    public void setFundPrice(double fundPrice) { this.fundPrice = fundPrice; }
    public double getAverageCost() { return averageCost; }
    public void setAverageCost(double averageCost) { this.averageCost = averageCost; }
    public double getReturnValue() { return returnValue; }
    public void setReturnValue(double returnValue) { this.returnValue = returnValue; }
    public double getShares() { return shares; }
    public void setShares(double shares) { this.shares = shares; }
    public double getBlockedShares() { return blockedShares; }
    public void setBlockedShares(double blockedShares) { this.blockedShares = blockedShares; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    @Override
    public String toString() {
        return "FundPortfolio{" +
                "fundName='" + fundName + '\'' +
                ", fundCode='" + fundCode + '\'' +
                ", fundPrice=" + fundPrice +
                ", averageCost=" + averageCost +
                ", returnValue=" + returnValue +
                ", shares=" + shares +
                ", blockedShares=" + blockedShares +
                ", amount=" + amount +
                ", accountNumber='" + accountNumber + '\'' +
                '}';
    }
}
