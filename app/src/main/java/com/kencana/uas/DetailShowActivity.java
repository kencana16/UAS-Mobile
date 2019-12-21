package com.kencana.uas;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;

public class DetailShowActivity extends AppCompatActivity {
    TextView tv_namaBarang_detail, tv_kodeBarang_detail, tv_jumlahBarang_detail,
            tv_satuan_detail, tv_hargaBarang_detail, tv_deskripsi_detail;
    ImageView iv_gambar_detail;
    Button btn_delete, btn_edit;

    String nama,kodeBarang, satuan, jumlah, harga, deskripsi;
    byte[] image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_show);



        tv_namaBarang_detail = (TextView) findViewById(R.id.tv_namaBarang_detail);
        tv_kodeBarang_detail = (TextView) findViewById(R.id.tv_kodeBarang_detail);
        tv_jumlahBarang_detail = (TextView) findViewById(R.id.tv_jumlahBarang_detail);
        tv_satuan_detail = (TextView) findViewById(R.id.tv_satuan_detail);
        tv_hargaBarang_detail = (TextView) findViewById(R.id.tv_hargaBarang_detail);
        tv_deskripsi_detail = (TextView) findViewById(R.id.tv_deskripsi_detail);
        iv_gambar_detail = (ImageView) findViewById(R.id.iv_gambar_detail);
        btn_delete = (Button) findViewById(R.id.btn_delete);
        btn_edit = (Button) findViewById(R.id.btn_edit);

        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(0);

//        mengambil kunci
        Intent intent = getIntent();
        this.kodeBarang = intent.getStringExtra("kodeBarang");

//         mengambil dari Database
        Cursor cursor = HomeActivity.mSQLiteHelper.getData("SELECT * FROM BARANG WHERE kode_barang = '"+kodeBarang+"'");
        while (cursor.moveToNext()){
            this.nama = cursor.getString(1);
            this.satuan = cursor.getString(2);
            this.jumlah = cursor.getString(3);
            this.harga = nf.format(cursor.getDouble(4));
            this.image  = cursor.getBlob(5);
            this.deskripsi = cursor.getString(6);
        }

        tv_namaBarang_detail.setText(nama);
        tv_kodeBarang_detail.setText(kodeBarang);
        tv_jumlahBarang_detail.setText(jumlah);
        tv_satuan_detail.setText(satuan);
        tv_hargaBarang_detail.setText("Rp. "+harga);
        tv_deskripsi_detail.setText(deskripsi);

        Bitmap bmp = BitmapFactory.decodeByteArray(image, 0, image.length);
        iv_gambar_detail.setImageBitmap(bmp);

        //tombol delete dan confirm alert
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailShowActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Hapus");
                builder.setMessage("Yakin ingin menghapus "+nama);
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                HomeActivity.mSQLiteHelper.queryData("DELETE FROM BARANG WHERE kode_barang = '"+kodeBarang+"'");
                                Intent intent2 = new Intent(getApplicationContext(),ShowDataActivity.class);
                                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent2);
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.setIcon(android.R.drawable.ic_dialog_alert);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        //fungsi tombol edit menuju activity EditDataActivity
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailShowActivity.this, EditDataActivity.class);
                intent.putExtra("kodeBarang",kodeBarang);

                startActivity(intent);
            }
        });
    }
}
