package com.kencana.uas;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

public class DetailSellActivity extends AppCompatActivity {

    TextView tv_namaBarang_detail, tv_kodeBarang_detail, tv_jumlahBarang_detail,
            tv_satuan_detail, tv_hargaBarang_detail, tv_deskripsi_detail, tv_jmlBeli, tv_jmlHarga;
    ImageView iv_gambar_detail;
    String nama,kodeBarang, satuan, jumlah, harga, deskripsi;
    byte[] image;
    Button plus, minus, buy;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_sell);


        tv_namaBarang_detail =  findViewById(R.id.tv_namaBarang_detailSell);
        tv_kodeBarang_detail = findViewById(R.id.tv_kodeBarang_detailSell);
        tv_jumlahBarang_detail =  findViewById(R.id.tv_jumlahBarang_detailSell);
        tv_satuan_detail =  findViewById(R.id.tv_satuan_detailSell);
        tv_hargaBarang_detail =  findViewById(R.id.tv_hargaBarang_detailSell);
        tv_deskripsi_detail =  findViewById(R.id.tv_deskripsi_detailSell);
        iv_gambar_detail =  findViewById(R.id.iv_gambar_detailSell);
        tv_jmlBeli =  findViewById(R.id.jmlBeli);
        tv_jmlHarga =  findViewById(R.id.tv_totalHarga);
        plus =  findViewById(R.id.plus);
        minus =  findViewById(R.id.minus);
        buy =  findViewById(R.id.btn_beli);

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

        if(Integer.parseInt(jumlah)==0){
            tv_jmlBeli.setText("0");
            tv_jmlHarga.setText("Rp. 0");
            plus.setEnabled(false);
        }

        if(Integer.parseInt(jumlah)==1){plus.setEnabled(false);}
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minus.setEnabled(true);


                NumberFormat nf = NumberFormat.getNumberInstance();
                nf.setMaximumFractionDigits(0);

                String sumStr = tv_jmlBeli.getText().toString();
                int sum = Integer.valueOf(sumStr);
                if(sum == (Integer.parseInt(jumlah)-1)){
                    plus.setEnabled(false);
                }
                sum += 1;
                long sumPrc = Integer.parseInt(harga.replace(",",""));
                sumPrc *= sum;
                sumStr = String.valueOf(sum);
                String sumPrcStr = nf.format(sumPrc);
                tv_jmlBeli.setText(sumStr);
                tv_jmlHarga.setText("Rp. "+sumPrcStr);

            }
        });

        minus.setEnabled(false);
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plus.setEnabled(true);

                NumberFormat nf = NumberFormat.getNumberInstance();
                nf.setMaximumFractionDigits(0);

                String sumStr = tv_jmlBeli.getText().toString();
                int sum = Integer.valueOf(sumStr);
                if(sum <= 1){
                    minus.setEnabled(false);
                }else{
                    sum -= 1;
                    long sumPrc = Integer.parseInt(harga.replace(",",""));
                    sumPrc *= sum;
                    sumStr = String.valueOf(sum);
                    String sumPrcStr = nf.format(sumPrc);
                    tv_jmlBeli.setText(sumStr);
                    tv_jmlHarga.setText("Rp. "+sumPrcStr);
                }
            }
        });

        if(Integer.parseInt(jumlah)<=0){buy.setEnabled(false);} //mematikan tombol nuy ketika stok habis

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String jmlHarga = tv_jmlHarga.getText().toString();
                String jmlBeli = tv_jmlBeli.getText().toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailSellActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Konfirmasi Pemesanan");
                builder.setMessage("Yakin ingin membeli "+jmlBeli+" "+satuan+" "+nama+" dengan harga "+jmlHarga);
                builder.setPositiveButton("Beli",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String sumStr = tv_jmlBeli.getText().toString();
                                int sum = Integer.valueOf(sumStr);
                                int jml = Integer.parseInt(jumlah)-sum;
                                HomeActivity.mSQLiteHelper.queryData("update BARANG  set jumlah_barang = '"+jml+"' WHERE kode_barang = '"+kodeBarang+"'");

                                Intent intent2 = new Intent(getApplicationContext(),SellDataActivity.class);
                                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent2);
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
    }


}
