package com.kencana.uas;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

import java.sql.Blob;

public class SQLiteHelper extends SQLiteOpenHelper {

    //construct
    public SQLiteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //method untuk menjalankan query
    public void queryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    //insert Method
    public void insertData(String id, String name, String denom, int qty, double price, byte[] image, String desc){
        SQLiteDatabase database = getWritableDatabase();
        //query to insert record in database table
        String sql = "INSERT INTO BARANG VALUES(?, ?, ?, ?, ?, ?, ?)"; //where "RECORD" is table name in database we will create in mainActivity

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, id);
        statement.bindString(2, name);
        statement.bindString(3, denom);
        statement.bindLong(4,qty);
        statement.bindDouble(5, price);
        statement.bindBlob(6, image);
        statement.bindString(7, desc);

        statement.executeInsert();
    }

    //updateData
    public void updateData(String name, String denom, int qty, double price, byte[] image, String desc, String id){
        SQLiteDatabase database = getWritableDatabase();
        //query to update record
        String sql = "UPDATE BARANG SET nama_barang=?, satuan=?, jumlah_barang=?, harga=?, gambar=?, deskripsi=?  WHERE kode_barang=?";

        SQLiteStatement statement = database.compileStatement(sql);

        statement.bindString(1, name);
        statement.bindString(2, denom);
        statement.bindLong(3,qty);
        statement.bindDouble(4, price);
        statement.bindBlob(5, image);
        statement.bindString(6, desc);
        statement.bindString(7, (String) id);

        statement.execute();
        database.close();
    }

    //deleteData
    public void deleteData(String id){
        SQLiteDatabase database = getWritableDatabase();
        //query to delete record using id
        String sql = "DELETE FROM BARANG WHERE kode_barang=?";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1, (String) id);

        statement.execute();
        database.close();
    }


    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
