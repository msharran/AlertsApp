package com.eqsis.notifications.Utils;

public class PreferenceItems {
    private  String type;
    private String stock;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSubscribtion(String subscribtion) {
        this.subscribtion = subscribtion;
    }

    public void setNotified(boolean notified) {
        isNotified = notified;
    }

    public PreferenceItems(String stock, String id, String subscribtion, boolean isNotified, String type) {
        this.id = id;
        this.stock=stock;
        this.subscribtion = subscribtion;
        this.isNotified = isNotified;
        this.type=type;
    }

    public String getStock() {
        return stock;
    }

    public String getId() {
        return id;
    }

    public String getSubscribtion() {
        return subscribtion;
    }

    public boolean isNotified() {
        return isNotified;
    }

    private String id;
    private String subscribtion;
    private boolean isNotified;



}
