package com.kencana.uas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

//implement filterable digunakan untuk mengaktifkan fungsi filter(pencarian)
public class CardBarangAdapter extends RecyclerView.Adapter<CardBarangAdapter.listViewHolder> implements Filterable {
    //initial list
    private ArrayList<Barang> listBarang;
    private ArrayList<Barang> listBarangfull;

    //construct
    public CardBarangAdapter(ArrayList<Barang> list){
        this.listBarang = list;
        listBarangfull = new ArrayList<>(listBarang);
    }

    //when card view clicked
    private OnItemClickCallback onItemClickCallback;
    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback){
        this.onItemClickCallback = onItemClickCallback;
    }

    //set inflater
    @NonNull
    @Override
    public listViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_card_barang, viewGroup, false);
        return new listViewHolder(view);
    }

    //add data to view
    @Override
    public void onBindViewHolder(@NonNull final listViewHolder holder, int position) {
        Barang barang = listBarang.get(position);
        Glide.with(holder.itemView.getContext())
                .load(barang.getItemImage())
                .apply(new RequestOptions().override(2000,2000))
                .into(holder.iv_gambar_sell);
        holder.tv_namaBarang_sell.setText(barang.getItemName());
        holder.tv_jumlahBarang_sell.setText(barang.getItemQuantity());
        holder.tv_hargaBarang_sell.setText("Rp. "+barang.getItemPrice());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(listBarang.get(holder.getAdapterPosition()));
            }
        });


    }

    //count all data to show in recyclerview
    @Override
    public int getItemCount() {
        return listBarang.size();
    }

    //init item from layout inflater
    public class listViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_gambar_sell;
        TextView  tv_namaBarang_sell, tv_jumlahBarang_sell, tv_hargaBarang_sell;

        public listViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_gambar_sell = itemView.findViewById(R.id.cover_image);
            tv_namaBarang_sell = itemView.findViewById(R.id.tv_namaBarang_sell);
            tv_jumlahBarang_sell = itemView.findViewById(R.id.tv_jumlahBarang_sell);
            tv_hargaBarang_sell = itemView.findViewById(R.id.tv_hargaBarang_sell);


        }
    }
    public interface OnItemClickCallback {
        void onItemClicked(Barang data);
    }

    //filter(pencarian)
    @Override
    public Filter getFilter() {
        return barangFilter;
    }

    //method filter
    private Filter barangFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Barang> filteredList = new ArrayList<>();

            // jika tidak tertulis apa2 di searchview
            if(constraint == null || constraint.length() == 0){
                filteredList.addAll(listBarangfull);
            }else{
                //jika terdapat character pada searchview
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

        //fungsi realtime searching
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listBarang.clear();
            listBarang.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };
}
