package org.acme.model;

import java.math.BigDecimal;

public class LineItem {

    private String description;
    private String currency;
    private BigDecimal amount;

    public LineItem() {
    }

    public LineItem(String description, String currency, BigDecimal amount) {
        this.description = description;
        this.currency = currency;
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
