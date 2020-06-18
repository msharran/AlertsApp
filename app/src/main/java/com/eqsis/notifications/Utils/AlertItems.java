package com.eqsis.notifications.Utils;

public class AlertItems {
    private String buySell;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public int getSl() {
        return sl;
    }

    public void setSl(int sl) {
        this.sl = sl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBuySell() {
        return buySell;
    }

    public void setBuySell(String buySell) {
        this.buySell = buySell;
    }

    public AlertItems(String token, int target, int bid, int sl, String type, String id, String buySell) {
        this.token = token;
        this.target = target;
        this.bid = bid;
        this.sl = sl;
        this.type=type;
        this.id=id;
        this.buySell=buySell;
    }

    private String token;
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
    private int target,bid,sl;

}
