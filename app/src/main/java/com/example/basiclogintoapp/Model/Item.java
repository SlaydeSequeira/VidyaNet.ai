package com.example.basiclogintoapp.Model;

// Item.java
public class Item {
    private String itemName;
    private String img;
    private int CP;
    private int SP;
    private String ailes;
    private String category;
    private int quantity;
    private String review;

    public Item() {
        // Default constructor required for Firebase
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getCP() {
        return CP;
    }

    public void setCP(int CP) {
        this.CP = CP;
    }

    public int getSP() {
        return SP;
    }

    public void setSP(int SP) {
        this.SP = SP;
    }

    public String getAiles() {
        return ailes;
    }

    public void setAiles(String ailes) {
        this.ailes = ailes;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
