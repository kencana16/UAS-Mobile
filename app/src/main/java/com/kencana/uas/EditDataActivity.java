package com.kencana.uas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.util.Objects;

public class EditDataActivity extends AppCompatActivity {

    TextInputEditText ti_kodeBarang, ti_namaBarang, ti_satuan,ti_jumlahBarang,ti_hargaBarang,ti_deskripsi;
    TextInputLayout til_kodeBarang, til_namaBarang, til_satuan,til_jumlahBarang,til_hargaBarang,til_deskripsi;
    Button btn_back,btn_save;
    ImageView iv_gambar;

    final int REQUEST_CODE_GALLERY = 999;

    String nama,kodeBarang, satuan, jumlah, harga, deskripsi;
    byte[] image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);

        //button
        btn_back = findViewById(R.id.btn_back_edit);
        btn_save = findViewById(R.id.btn_save_edit);

        //textInput
        ti_kodeBarang = findViewById(R.id.ti_kodeBarang_edit);
        ti_namaBarang = findViewById(R.id.ti_namaBarang_edit);
        ti_satuan = findViewById(R.id.ti_satuan_edit);
        ti_jumlahBarang = findViewById(R.id.ti_jumlahBarang_edit);
        ti_hargaBarang = findViewById(R.id.ti_hargaBarang_edit);
        ti_deskripsi  = findViewById(R.id.ti_desc_edit);

        //Text Input Layout
        til_kodeBarang = findViewById(R.id.til_kodeBarang_edit);
        til_namaBarang = findViewById(R.id.til_namaBarang_edit);
        til_satuan = findViewById(R.id.til_satuan_edit);
        til_jumlahBarang = findViewById(R.id.til_jumlahBarang_edit);
        til_hargaBarang = findViewById(R.id.til_hargaBarang_edit);
        til_deskripsi = findViewById(R.id.til_desc_edit);

        //ImageView
        iv_gambar = findViewById(R.id.iv_gambar_edit);

        ambilData();

        //select image by on imageview click
        iv_gambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //read external storage permission to select image from gallery
                //runtime permission for devices android 6.0 and above
                ActivityCompat.requestPermissions(
                        EditDataActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });

        //add record to sqlite
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //reset error
                til_kodeBarang.setErrorEnabled(false);
                til_kodeBarang.setError(null);
                til_jumlahBarang.setErrorEnabled(false);
                til_jumlahBarang.setError(null);
                til_hargaBarang.setErrorEnabled(false);
                til_hargaBarang.setError(null);

                if(Objects.requireNonNull(ti_kodeBarang.getText()).toString().isEmpty() || ti_jumlahBarang.getText().toString().isEmpty() || ti_hargaBarang.getText().toString().isEmpty()){
                    if(ti_kodeBarang.getText().toString().isEmpty()){
                        til_kodeBarang.setErrorEnabled(true);
                        til_kodeBarang.setError("Masukan Kode Barang");
                    }
                    if(ti_jumlahBarang.getText().toString().isEmpty()){
                        til_jumlahBarang.setErrorEnabled(true);
                        til_jumlahBarang.setError("Jumlah Barang Minimal Satu");
                    }
                    if(ti_hargaBarang.getText().toString().isEmpty()) {
                        til_hargaBarang.setErrorEnabled(true);
                        til_hargaBarang.setError("Harga Barang Harus Diisi");
                    }
                }else {
                    try {
                        //update data ke database
                        HomeActivity.mSQLiteHelper.updateData(
                                ti_namaBarang.getText().toString().trim(),
                                ti_satuan.getText().toString().trim(),
                                Integer.parseInt(ti_jumlahBarang.getText().toString().trim()),
                                Double.parseDouble(ti_hargaBarang.getText().toString().trim()),
                                imageViewToByte(iv_gambar),
                                ti_deskripsi.getText().toString().trim(),
                                ti_kodeBarang.getText().toString().trim()
                        );
                        Toast.makeText(EditDataActivity.this, "Record Berhasil Diubah", Toast.LENGTH_SHORT).show();
                        Intent intent2 = new Intent(getApplicationContext(),ShowDataActivity.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent2);

                        finish();
                        //reset views
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //tombol kembali
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void ambilData(){
        //format number ###.###.###
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
            this.harga = nf.format(cursor.getDouble(4)).replace(",", "");
            this.image  = cursor.getBlob(5);
            this.deskripsi = cursor.getString(6);
        }

        ti_namaBarang.setText(nama);
        ti_kodeBarang.setText(kodeBarang);
        ti_jumlahBarang.setText(jumlah);
        ti_satuan.setText(satuan);
        ti_hargaBarang.setText(harga);
        ti_deskripsi.setText(deskripsi);

        Bitmap bmp = BitmapFactory.decodeByteArray(image, 0, image.length);
        iv_gambar.setImageBitmap(bmp);

    }

    //mengubah gambar ke byte[]
    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_GALLERY){
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //gallery intent
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, REQUEST_CODE_GALLERY);
            }
            else {
                Toast.makeText(this, "Tidak ada perizinan untuk mengakses gambar", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON) //enable image guidlines
                    .setAspectRatio(1,1)// image will be square
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result =CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){
                Uri resultUri = result.getUri();
                //set image choosed from gallery to image view
                iv_gambar.setImageURI(resultUri);
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }
}
