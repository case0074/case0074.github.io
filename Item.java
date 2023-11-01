package application;

import java.util.*;

class Item {
    private String name;
    private int quantity;
    private Date expirationDate;

    public Item(String name, int quantity, Date expirationDate) {
        this.name = name;
        this.quantity = quantity;
        this.expirationDate = expirationDate;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Quantity: " + quantity + ", Expiration Date: " + expirationDate;
    }
    public void decrementQuantity() {
        quantity--;
    }
    }




