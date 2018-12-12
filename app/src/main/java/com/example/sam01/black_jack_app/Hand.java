package com.example.sam01.black_jack_app;

public class Hand {
    private static int staticNum = 1;
    private String strYours, strDealers, earnings;
    Boolean outcome;
    private final int handNum;

    public Hand(String strYours, String strDealers, Boolean outcome, String earnings) {
        this.strYours = strYours;
        this.strDealers = strDealers;
        this.outcome = outcome;
        this.earnings = earnings;
        this.handNum = staticNum++;
    }

    public String getStrYours() {
        return strYours;
    }

    public void setStrYours(String strYours) {
        this.strYours = strYours;
    }

    public String getStrDealers() {
        return strDealers;
    }

    public void setStrDealers(String strDealers) {
        this.strDealers = strDealers;
    }

    public Boolean getOutcome() {
        return outcome;
    }

    public void setOutcome(Boolean outcome) {
        this.outcome = outcome;
    }

    public String getEarnings() {
        return earnings;
    }

    public void setEarnings(String earnings) {
        this.earnings = earnings;
    }

    @Override
    public String toString() {
        return "Hand #" + handNum +
                ": Yours = "+ strYours +
                "  Dealers = " + strDealers +
                "  Win = " + outcome +
                "  Earnings = " + earnings;
    }
}
