package pl.galas.app.models;

import java.io.Serializable;

public class ExchangeRatesDTO implements Serializable {
    private String effectiveDate;
    private double bid;
    private double ask;
    private double askDifference;
    private double bidDifference;

    public ExchangeRatesDTO() {
    }

    public ExchangeRatesDTO(Rate rate) {
        this.effectiveDate = rate.getEffectiveDate();
        this.bid = rate.getBid();
        this.ask = rate.getAsk();
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public double getBid() {
        return bid;
    }

    public void setBid(double bid) {
        this.bid = bid;
    }

    public double getAsk() {
        return ask;
    }

    public void setAsk(double ask) {
        this.ask = ask;
    }

    public double getAskDifference() {
        return askDifference;
    }

    public void setAskDifference(double askDifference) {
        this.askDifference = askDifference;
    }

    public double getBidDifference() {
        return bidDifference;
    }

    public void setBidDifference(double bidDifference) {
        this.bidDifference = bidDifference;
    }
}
