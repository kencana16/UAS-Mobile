package com.kencana.uas;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

public class ListBarangAdapter extends RecyclerView.Adapter<ListBarangAdapter.listViewHolder> implements Filterable {
    private ArrayList<Barang> listBarang;
    private ArrayList<Barang> listBarangfull;

    public ListBarangAdapter(ArrayList<Barang> list){
        this.listBarang = list;
        listBarangfull = new ArrayList<>(listBarang);
    }

    private OnItemClickCallback onItemClickCallback;
    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback){
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public listViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row_barang, viewGroup, false);
        return new listViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final listViewHolder holder, int position) {
        Barang barang = listBarang.get(position);
        Glide.with(holder.itemView.getContext())
                .load(barang.getItemImage())
                .apply(new RequestOptions().override(55,55))
                .into(holder.iv_gambar_show);
        holder.tv_kodeBarang_show.setText(barang.getItemId());
        holder.tv_namaBarang_show.setText(barang.getItemName());
        holder.tv_satuan_show.setText(barang.getItemDenomination());
        holder.tv_jumlahBarang_show.setText(barang.getItemQuantity());
        holder.tv_hargaBarang_show.setText("Rp. "+barang.getItemPrice());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(listBarang.get(holder.getAdapterPosition()));
            }
        });


    }

    @Override
    public int getItemCount() {
        return listBarang.size();
    }

    public class listViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_gambar_show;
        TextView tv_kodeBarang_show, tv_namaBarang_show, tv_satuan_show, tv_jumlahBarang_show, tv_hargaBarang_show;
        RelativeLayout rlparent;

        public listViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_gambar_show = itemView.findViewById(R.id.iv_gambar_show);
            tv_kodeBarang_show = itemView.findViewById(R.id.tv_kodeBarang_show);
            tv_namaBarang_show = itemView.findViewById(R.id.tv_namaBarang_show);
            tv_satuan_show = itemView.findViewById(R.id.tv_satuan_show);
            tv_jumlahBarang_show = itemView.findViewById(R.id.tv_jumlahBarang_show);
            tv_hargaBarang_show = itemView.findViewById(R.id.tv_hargaBarang_show);

        }
    }
    public interface OnItemClickCallback {
        void onItemClicked(Barang data);
    }

    @Override
    public Filter getFilter() {
        return barangFilter;
    }

    private Filter barangFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Barang> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                filteredList.addAll(listBarangfull);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(Barang item: listBarangfull){
                    if(item.getItemName().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listBarang.clear();
            listBarang.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };
}
