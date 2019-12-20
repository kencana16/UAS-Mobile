package com.kencana.uas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.bumptech.glide.load.model.Model;

import java.text.NumberFormat;
import java.util.ArrayList;

public class ShowDataActivity extends AppCompatActivity {

    private ListBarangAdapter listBarangAdapter;

    RecyclerView rv_dataBarang;
    ArrayList<Barang> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);

        rv_dataBarang = findViewById(R.id.rv_dataBarang);
        rv_dataBarang.setHasFixedSize(true);



        //////////////////////////////////////////////
        Cursor cursor = HomeActivity.mSQLiteHelper.getData("SELECT * FROM BARANG");
        list.clear();

        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(0);

        while (cursor.moveToNext()){
            String kode = cursor.getString(0);
            String nama = cursor.getString(1);
            String satuan = cursor.getString(2);
            String jumlah = cursor.getString(3);
            String harga = nf.format(cursor.getDouble(4));
            byte[] image  = cursor.getBlob(5);
            String deskripsi = cursor.getString(4);
            //add to list
            list.add(new Barang(kode, nama, satuan, jumlah, harga, image,deskripsi));
        }


        if (list.size()==0){
            //if there is no record in table of database which means listview is empty
            Toast.makeText(this, "Record Tidak Ditemukan..", Toast.LENGTH_LONG).show();
        }


        showRecyclerList();
    }

    private void showRecyclerList(){
        rv_dataBarang.setLayoutManager(new LinearLayoutManager(this));
        listBarangAdapter = new ListBarangAdapter(list);
        rv_dataBarang.setAdapter(listBarangAdapter);

        listBarangAdapter.setOnItemClickCallback(new ListBarangAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Barang data) {
                showSelectedBarang(data);
            }
        });
    }
    private void showSelectedBarang(Barang barang) {
        Toast.makeText(this, barang.getItemId(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ShowDataActivity.this, DetailShowActivity.class);
        intent.putExtra("kodeBarang",barang.getItemId());

        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listBarangAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }
}
