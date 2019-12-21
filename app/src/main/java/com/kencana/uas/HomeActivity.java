package com.kencana.uas;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {
    LinearLayout btnAdd, btnRead, btnSell, btnExit;


    public static SQLiteHelper mSQLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnAdd = (LinearLayout) findViewById(R.id.btnAddData);
        btnRead = (LinearLayout) findViewById(R.id.btnReadData);
        btnSell = (LinearLayout) findViewById(R.id.btnSell);
        btnExit = (LinearLayout) findViewById(R.id.btnExit);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AddDataActivity.class);
                startActivity(intent);
            }
        });

        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ShowDataActivity.class);
                startActivity(intent);
            }
        });

        btnSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SellDataActivity.class);
                startActivity(intent);
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);       //alert ketika ingin keluar
                builder.setCancelable(true);
                builder.setTitle("Peringatan");
                builder.setMessage("Yakin ingin Keluar");
                builder.setPositiveButton("Keluar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                System.exit(0);
                            }
                        });
                builder.setNegativeButton("Batal",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        //creating database
        mSQLiteHelper = new SQLiteHelper(this, "RECORDDB.sqlite", null, 1);

        //creating table in database
        mSQLiteHelper.queryData("CREATE TABLE IF NOT EXISTS BARANG(kode_barang varchar, nama_barang varchar, satuan varchar, jumlah_barang int, harga double, gambar BLOB, deskripsi varchar)");

    }
}
