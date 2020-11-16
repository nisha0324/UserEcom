package com.example.userproductscart.model;

public class Variant {

    String name;
    int price;

    public Variant(String name, int price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() {
        return name + " - Rs. " + price;
    }
}

