package com.kencana.uas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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

import java.text.NumberFormat;
import java.util.ArrayList;

public class SellDataActivity extends AppCompatActivity {
    private CardBarangAdapter cardBarangAdapter;

    RecyclerView recyclerView;
    ArrayList<Barang> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_data);

        recyclerView= findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);


        //////////////////////////////////////////////
        ///mengambil dari database dimasukkan ke array list dengan bantuan objek cursor
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

    //menampilkan recyclerview
    private void showRecyclerList(){
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        cardBarangAdapter = new CardBarangAdapter(list);
        recyclerView.setAdapter(cardBarangAdapter);

        cardBarangAdapter.setOnItemClickCallback(new CardBarangAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Barang data) {
                showSelectedBarang(data);
            }
        });
    }

    //ketika item recycler di klik
    private void showSelectedBarang(Barang barang) {
        Toast.makeText(this, barang.getItemId(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(SellDataActivity.this, DetailSellActivity.class);
        intent.putExtra("kodeBarang",barang.getItemId());

        startActivity(intent);
    }

    //tombol search di action bar
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
                cardBarangAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }
}
