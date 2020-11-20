package com.example.userproductscart;

import android.content.Context;
import android.media.MediaRouter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.userproductscart.controllers.MultipleVBProductAndWBViewBinder;
import com.example.userproductscart.controllers.SingleVBProductViewBinder;
import com.example.userproductscart.databinding.ProductItemBinding;
import com.example.userproductscart.databinding.ProductItemSingleVbBinding;
import com.example.userproductscart.databinding.ProductItemWbMultiVbBinding;
import com.example.userproductscart.model.Cart;
import com.example.userproductscart.model.Product;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import java.util.List;

public class ProductsAdaptor extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_SINGLE_VB = 0, TYPE_WB_OR_MULTIPLE_VB = 1;

    private Context context;
    private List<Product> productList;

    private Cart cart;

    public ProductsAdaptor(Context context, List<Product> productList, Cart cart) {
        this.context = context;
        this.productList = productList;
        this.cart = cart;
    }

    @Override
    public int getItemViewType(int position) {
        Product product = productList.get(position);

        if (product.type == Product.WEIGHT_BASED || product.variants.size() > 1)
            return TYPE_WB_OR_MULTIPLE_VB;

        return TYPE_SINGLE_VB;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == TYPE_SINGLE_VB){
            ProductItemSingleVbBinding b = ProductItemSingleVbBinding.inflate(
                    LayoutInflater.from(context)
                    , parent
                    , false
            );

            return new SingleVbVh(b);
        }else {
            ProductItemWbMultiVbBinding b = ProductItemWbMultiVbBinding.inflate(
                    LayoutInflater.from(context)
                    , parent
                    , false
            );

            return new MultipleVbOrWbVH(b);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
              final Product product = productList.get(position);

              if (getItemViewType(position) == TYPE_SINGLE_VB){

                  SingleVbVh vh = (SingleVbVh) holder;
                  vh.b.name.setText(product.name + " " + product.variants.get(0).name);
                  vh.b.price.setText(product.priceString());

                  new SingleVBProductViewBinder()
                          .bind(vh.b,product,cart);
              }else{
                  MultipleVbOrWbVH vh = (MultipleVbOrWbVH) holder;

                  vh.b.name.setText(product.name);
                  vh.b.price.setText(product.priceString());

                  new MultipleVBProductAndWBViewBinder()
                          .bind(vh.b,product,cart);
              }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }



    public class SingleVbVh extends RecyclerView.ViewHolder {

        ProductItemSingleVbBinding b;

        public SingleVbVh( ProductItemSingleVbBinding b) {
            super(b.getRoot());
            this.b = b;
        }
    }

    public class MultipleVbOrWbVH extends RecyclerView.ViewHolder {

        ProductItemWbMultiVbBinding b;

        public MultipleVbOrWbVH(ProductItemWbMultiVbBinding b) {
            super(b.getRoot());
            this.b = b;
        }
    }
}
