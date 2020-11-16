package com.example.userproductscart.model;

import java.util.HashMap;
import java.util.Map;

public class Cart {

    public Map<String, CartItem> map = new HashMap<>();
    public Map<String, Integer> totalVariantQtyMap = new HashMap<>();

    public int addToCart(Product product, Variant variant){
        String fullName = product.name + " "+ variant.name;

        if (map.containsKey(fullName)){
            map.get(fullName).qty++;
        }else {
            map.put(fullName, new CartItem(fullName, variant.price));
        }



        if (totalVariantQtyMap.containsKey(product.name)){
            int totalQty = totalVariantQtyMap.get(product.name);
            totalQty++;
            totalVariantQtyMap.put(product.name, totalQty);
        }else {
            totalVariantQtyMap.put(product.name, 1);
        }


        return (int) map.get(fullName).qty; //TODO : why it has been typecasted to int
    }

    public int removeFromCart(Product product, Variant variant) {
        String fullName = product.name + " "+ variant.name;

        map.get(fullName).qty--;

        int qty = (int) map.get(fullName).qty;
        if (qty == 0)
            map.remove(fullName);


        int totalQty = totalVariantQtyMap.get(product.name);
        totalQty--;
        if (totalQty == 0)
            totalVariantQtyMap.remove(product.name);
        else
            totalVariantQtyMap.put(product.name, totalQty);

        return qty;
    }


    public void updateWeightBasedProductQuantity(Product product, float qty){
        int price = (int) qty * product.pricePerKg;
        map.put(product.name, new CartItem(product.name, price, qty));
    }
}
