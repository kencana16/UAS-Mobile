package com.kencana.uas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

public class AddDataActivity extends AppCompatActivity {

    //inisialisasi variabel
    TextInputEditText ti_kodeBarang, ti_namaBarang, ti_satuan,ti_jumlahBarang,ti_hargaBarang,ti_deskripsi;
    TextInputLayout til_kodeBarang, til_namaBarang, til_satuan,til_jumlahBarang,til_hargaBarang,til_deskripsi;
    Button btn_back,btn_save;
    ImageView iv_gambar;

    final int REQUEST_CODE_GALLERY = 999;       //request code untuk buka galeri



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //button
        btn_back = findViewById(R.id.btn_back);
        btn_save = findViewById(R.id.btn_save);

        //textInput
        ti_kodeBarang = findViewById(R.id.ti_kodeBarang_add);
        ti_namaBarang = findViewById(R.id.ti_namaBarang_add);
        ti_satuan = findViewById(R.id.ti_satuan_add);
        ti_jumlahBarang = findViewById(R.id.ti_jumlahBarang_add);
        ti_hargaBarang = findViewById(R.id.ti_hargaBarang_add);
        ti_deskripsi  = findViewById(R.id.ti_desc_add);

        //Text Input Layout
        til_kodeBarang = findViewById(R.id.til_kodeBarang_add);
        til_namaBarang = findViewById(R.id.til_namaBarang_add);
        til_satuan = findViewById(R.id.til_satuan_add);
        til_jumlahBarang = findViewById(R.id.til_jumlahBarang_add);
        til_hargaBarang = findViewById(R.id.til_hargaBarang_add);
        til_deskripsi = findViewById(R.id.til_desc_add);

        //ImageView
        iv_gambar = findViewById(R.id.iv_gambar_add);



        //Pilih gambar ketika gambar di klik
        iv_gambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //read external storage permission to select image from gallery
                //runtime permission for devices android 6.0 and above
                ActivityCompat.requestPermissions(
                        AddDataActivity.this,
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

                //check error
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
                        //insert data to DB
                        HomeActivity.mSQLiteHelper.insertData(
                                ti_kodeBarang.getText().toString().trim(),
                                ti_namaBarang.getText().toString().trim(),
                                ti_satuan.getText().toString().trim(),
                                Integer.parseInt(ti_jumlahBarang.getText().toString().trim()),
                                Double.parseDouble(ti_hargaBarang.getText().toString().trim()),
                                imageViewToByte(iv_gambar),
                                ti_deskripsi.getText().toString().trim()
                        );
                        Toast.makeText(AddDataActivity.this, "Record Berhasil Ditambahkan", Toast.LENGTH_SHORT).show();
                        //reset views
                        ti_kodeBarang.setText("");
                        ti_namaBarang.setText("");
                        ti_satuan.setText("");
                        ti_jumlahBarang.setText("");
                        ti_hargaBarang.setText("");
                        ti_deskripsi.setText("");
                        iv_gambar.setImageResource(R.drawable.add_image);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        //button back
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    //convert image to byte[]
    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    //open request to storage
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

    //if request granted
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

    //hide keyboard when touch other view
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }
}
