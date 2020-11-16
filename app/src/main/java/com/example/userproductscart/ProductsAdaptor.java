package com.example.userproductscart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.userproductscart.databinding.ProductItemBinding;
import com.example.userproductscart.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductsAdaptor extends RecyclerView.Adapter<ProductsAdaptor.ViewHolder> {

    private Context context;
    private List<Product> productList;

    public ProductsAdaptor(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ProductItemBinding b = ProductItemBinding.inflate(LayoutInflater.from(context)
                                       , parent
                                       , false);
        return new ViewHolder(b);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Product product =  productList.get(position);

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
            private ProductItemBinding b;

        public ViewHolder(ProductItemBinding b) {
            super(b.getRoot());
            this.b = b;
        }
    }
}
