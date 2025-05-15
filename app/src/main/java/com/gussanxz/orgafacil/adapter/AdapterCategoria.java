package com.gussanxz.orgafacil.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gussanxz.orgafacil.R;

import java.util.List;

public class AdapterCategoria extends RecyclerView.Adapter<AdapterCategoria.MyViewHolder> {

    private List<String> categorias;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(String categoria);
    }

    public AdapterCategoria(List<String> categorias, OnItemClickListener listener) {
        this.categorias = categorias;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_categoria, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String cat = categorias.get(position);
        holder.textCategoria.setText(cat);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(cat);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categorias.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textCategoria;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textCategoria = itemView.findViewById(R.id.textCategoriaItem);
        }
    }

    // Método para remover item da lista local (usado em swipe)
    public void removeItem(int position) {
        categorias.remove(position);
        notifyItemRemoved(position);
    }

    // Retorna o nome da categoria na posição
    public String getItem(int position) {
        return categorias.get(position);
    }
}
