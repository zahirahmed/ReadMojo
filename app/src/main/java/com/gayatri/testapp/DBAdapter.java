package com.gayatri.testapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gayatri.testapp.Model.BookDetailClass;
import com.gayatri.testapp.Model.ImageBookDetailClass;

import java.util.ArrayList;

public class DBAdapter {

    private static final String DATABASE_NAME = "ReadMojo";
    private static final int DATABASE_VERSION = 1;
    private static final String BookDetail = "create table bookdetail(book_id TEXT,user_id TEXT,bookName TEXT,bookType TEXT,bookContent TEXT,audio_path TEXT,moral TEXT,price TEXT,created_on TEXT);";
    private static final String BookImageDetail = "create table bookimagedetail(book_id TEXT,user_id TEXT,id TEXT,book_name TEXT,created_on TEXT);";
     private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;


    public DBAdapter(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(BookDetail);
            db.execSQL(BookImageDetail);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + BookDetail);
            db.execSQL("DROP TABLE IF EXISTS " + BookImageDetail);
            onCreate(db);
        }
    }

    public DBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }


    public void close() {
        DBHelper.close();
    }

    public boolean deleteAll(String tabName) {
        return db.delete(tabName, null, null) > 0;

    }

    public long insertbookdetail(String book_id, String user_id, String bookName, String bookType, String bookContent, String audio_path, String moral, String price, String created_on) {
        ContentValues initialvalues = new ContentValues();
        initialvalues.put("book_id", book_id);
        initialvalues.put("user_id", user_id);
        initialvalues.put("bookName", bookName);
        initialvalues.put("bookType", bookType);
        initialvalues.put("bookContent", bookContent);
        initialvalues.put("audio_path", audio_path);
        initialvalues.put("moral", moral);
        initialvalues.put("price", price);
        initialvalues.put("created_on", created_on);
        return db.insert("bookdetail", null, initialvalues);

    }


    public long insertImageBookdetail(String book_id, String user_id, String id, String book_name, String created_on) {
        ContentValues initialvalues = new ContentValues();
        initialvalues.put("book_id", book_id);
        initialvalues.put("user_id", user_id);
        initialvalues.put("id", id);
        initialvalues.put("book_name", book_name);
        initialvalues.put("created_on", created_on);
        return db.insert("bookimagedetail", null, initialvalues);

    }

//=========================== get Data =========================================


    public ArrayList<BookDetailClass> getbookdetail() {
        ArrayList<BookDetailClass> bookdetail = new ArrayList<BookDetailClass>();
        Cursor c = db.rawQuery("Select * from BookDetail", null);
        c.moveToFirst();

        for (int i = 0; i < c.getCount(); i++) {
            BookDetailClass Pdata = new BookDetailClass();
            Pdata.setBook_id(c.getString(0));
            Pdata.setUser_id(c.getString(1));
            Pdata.setBookName(c.getString(2));
            Pdata.setBookType(c.getString(3));
            Pdata.setBookContent(c.getString(4));
            Pdata.setAudio_path(c.getString(5));
            Pdata.setMoral(c.getString(6));
            Pdata.setPrice(c.getString(7));
            Pdata.setCreated_on(c.getString(8));
            bookdetail.add(Pdata);
            c.moveToNext();
        }
        return bookdetail;
    }


    public ArrayList<ImageBookDetailClass> getimagebookdetail() {
        ArrayList<ImageBookDetailClass> imagebookdetail = new ArrayList<ImageBookDetailClass>();
        Cursor c = db.rawQuery("Select * from BookImageDetail", null);
        c.moveToFirst();

        for (int i = 0; i < c.getCount(); i++) {
            ImageBookDetailClass Pdata = new ImageBookDetailClass();
            Pdata.setBook_id(c.getString(0));
            Pdata.setUser_id(c.getString(1));
            Pdata.setId(c.getString(2));
            Pdata.setBook_name(c.getString(3));
            Pdata.setCreated_on(c.getString(4));
            imagebookdetail.add(Pdata);
            c.moveToNext();
        }


        return imagebookdetail;
    }

    //  get data by parameter
    public ArrayList<BookDetailClass> getBookbyId(String book_id) {
        ArrayList<BookDetailClass> databyid = new ArrayList<BookDetailClass>();
        Cursor c = db.rawQuery("Select * from " + "BookDetail where book_id=\"" + book_id + "\"", null);
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {
            BookDetailClass Pdata = new BookDetailClass();
            Pdata.setBook_id(c.getString(0));
            Pdata.setUser_id(c.getString(1));
            Pdata.setBookName(c.getString(2));
            Pdata.setBookType(c.getString(3));
            Pdata.setBookContent(c.getString(4));
            Pdata.setAudio_path(c.getString(5));
            Pdata.setMoral(c.getString(6));
            Pdata.setPrice(c.getString(7));
            Pdata.setCreated_on(c.getString(8));
            databyid.add(Pdata);
            c.moveToNext();

        }
        return databyid;
    }



    public ArrayList<ImageBookDetailClass> getnearbyeventdetailbyid(String book_id) {
        ArrayList<ImageBookDetailClass> databyid = new ArrayList<ImageBookDetailClass>();
        Cursor c = db.rawQuery("Select * from " + "BookImageDetail where book_id=\"" + book_id + "\"", null);
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {
            ImageBookDetailClass Pdata = new ImageBookDetailClass();
            Pdata.setBook_id(c.getString(0));
            Pdata.setUser_id(c.getString(1));
            Pdata.setId(c.getString(2));
            Pdata.setBook_name(c.getString(3));
            Pdata.setCreated_on(c.getString(4));
            databyid.add(Pdata);
            c.moveToNext();

        }
        return databyid;
    }
}
