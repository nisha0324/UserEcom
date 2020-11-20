package com.example.userproductscart.model;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Cart {

    public Map<String, CartItem> map = new HashMap<>();
    public Map<String, Integer> totalVariantQtyMap = new HashMap<>();

    public int noOfItems, subTotal;

    public int addToCart(Product product, Variant variant){
        String fullName = product.name + " "+ variant.name;

        if (map.containsKey(fullName)){
            CartItem previousValue =map.get(fullName);

            previousValue.qty++;
            previousValue.price += variant.price ;

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


        return (int) map.get(fullName).qty;
    }

    public int removeFromCart(Product product, Variant variant) {
        String fullName = product.name + " "+ variant.name;

        CartItem existingCartItem = map.get(fullName);
        existingCartItem.qty--;
        existingCartItem.price -= variant.price;


        if(map.get(fullName).qty == 0)
            map.remove(fullName);

        noOfItems--;
        subTotal -= variant.price;

        int totalQty = totalVariantQtyMap.get(product.name) - 1;
        totalVariantQtyMap.put(product.name, totalQty);

        if (totalVariantQtyMap.get(product.name) == 0)
            totalVariantQtyMap.remove(fullName);


        return map.containsKey(fullName) ? (int) map.get(fullName).qty : 0;
    }




    public void removeAllVariantsFromCart(Product product){
        for(Variant variant : product.variants){
            String key = product.name + " " + variant.name;

            if(map.containsKey(key)){
                subTotal -= map.get(key).price;
                noOfItems -= map.get(key).qty;

                map.remove(key);
            }
        }

        if(totalVariantQtyMap.containsKey(product.name))
            totalVariantQtyMap.remove(product.name);
    }

    

    public void updateWBQuantity(Product product, float qty){
        int newPrice = (int) (product.pricePerKg * qty);

        if (map.containsKey(product.name)){
            subTotal -= map.get(product.name).price; //TODO
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

    public int getVariantQty(Product product, Variant variant) {
        String key = product.name + " "+ variant.name;

        if (map.containsKey(key))
                  return (int) map.get(key).qty;

        return 0;
    }
}
