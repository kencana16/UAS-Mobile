package com.kencana.uas;

import androidx.appcompat.app.AppCompatActivity;

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

public class DetailSellActivity extends AppCompatActivity {

    TextView tv_namaBarang_detail, tv_kodeBarang_detail, tv_jumlahBarang_detail,
            tv_satuan_detail, tv_hargaBarang_detail, tv_deskripsi_detail, tv_jmlBeli, tv_jmlHarga;
    ImageView iv_gambar_detail;
    String nama,kodeBarang, satuan, jumlah, harga, deskripsi;
    byte[] image;
    Button plus, minus, buy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_sell);


        tv_namaBarang_detail = (TextView) findViewById(R.id.tv_namaBarang_detailSell);
        tv_kodeBarang_detail = (TextView)findViewById(R.id.tv_kodeBarang_detailSell);
        tv_jumlahBarang_detail = (TextView) findViewById(R.id.tv_jumlahBarang_detailSell);
        tv_satuan_detail = (TextView) findViewById(R.id.tv_satuan_detailSell);
        tv_hargaBarang_detail = (TextView) findViewById(R.id.tv_hargaBarang_detailSell);
        tv_deskripsi_detail = (TextView) findViewById(R.id.tv_deskripsi_detailSell);
        iv_gambar_detail = (ImageView) findViewById(R.id.iv_gambar_detailSell);
        tv_jmlBeli = (TextView) findViewById(R.id.jmlBeli);
        tv_jmlHarga = (TextView) findViewById(R.id.tv_totalHarga);
        plus = (Button) findViewById(R.id.plus);
        minus = (Button) findViewById(R.id.minus);
        buy = (Button) findViewById(R.id.btn_beli);

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
        tv_jmlHarga.setText("Rp. "+harga);

        Bitmap bmp = BitmapFactory.decodeByteArray(image, 0, image.length);
        iv_gambar_detail.setImageBitmap(bmp);


    }


}
