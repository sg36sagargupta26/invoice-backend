package org.acme.model;

import java.util.List;

public class Invoice {

    private String currency;
    private String date;
    private List<LineItem> lines;

    public Invoice() {
    }

    public Invoice(String currency, String date, List<LineItem> lines) {
        this.currency = currency;
        this.date = date;
        this.lines = lines;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<LineItem> getLines() {
        return lines;
    }

    public void setLines(List<LineItem> lines) {
        this.lines = lines;
    }
}
