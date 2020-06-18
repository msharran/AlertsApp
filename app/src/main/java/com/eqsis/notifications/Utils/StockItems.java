package com.eqsis.notifications.Utils;

public class StockItems {
    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public StockItems(String stock,String id) {

        this.stock = stock;
        this.id=id;
    }

    private String stock;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
}
