package com.example.userproductscart.model;

import java.util.HashMap;
import java.util.Map;

public class Cart {

    public Map<String, CartItem> map = new HashMap<>();
    public Map<String, Integer> totalVariantQtyMap = new HashMap<>();

    public int noOfItems, subTotal;

    public int addToCart(Product product, Variant variant){
        String fullName = product.name + " "+ variant.name;

        if (map.containsKey(fullName)){
            CartItem previousValue =map.get(fullName);

            previousValue.qty++;
            subTotal += previousValue.price;
        }else {
            map.put(fullName, new CartItem(fullName, variant.price));
        }

         noOfItems++;
        subTotal += variant.price;

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

        noOfItems--;
        subTotal -= variant.price;

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

    public void removeAllVariantsFromCart(Product product){
        for(Variant variant : product.variants){
            String key = product.name + " " + variant.name;

            if(map.containsKey(key)){
                subTotal -= map.get(key).price;
                noOfItems -= map.get(key).qty;
            }
        }

        if(totalVariantQtyMap.containsKey(product.name))
            totalVariantQtyMap.remove(product.name);
    }

    

    public void updateWBQuantity(Product product, float qty){
        int newPrice = (int) (product.pricePerKg * qty);

        if (map.containsKey(product.name)){
            subTotal -= map.get(product.name).price;
        }else
            noOfItems++;

        map.put(product.name, new CartItem(product.name, newPrice, qty));
        subTotal += newPrice;
    }

    public void removeWBFromCart(Product product){
        if (map.containsKey(product.name)){
            subTotal -= map.get(product.name).price;
            noOfItems--;

            map.remove(product.name);
        }
    }
}
