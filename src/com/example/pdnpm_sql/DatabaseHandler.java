package com.example.pdnpm_sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jakub
 * Date: 10/19/13
 * Time: 9:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private String create_osoba_table = "CREATE TABLE IF NOT EXISTS" +
            " OSOBA(id INTEGER PRIMARY KEY AUTOINCREMENT," +
            " nazwa VARCHAR," +
            " rok_ur INT(3))";

    private String create_ksiazka_table = "CREATE TABLE IF NOT EXISTS" +
            " KSIAZKA(id INTEGER PRIMARY KEY AUTOINCREMENT," +
            " id_osoba INTEGER," +
            " tytul VARCHAR," +
            " FOREIGN KEY(id_osoba) REFERENCES OSOBA(id))";

    private String insert_osoba = "INSERT INTO OSOBA(nazwa, rok_ur) VALUES ('Na', 1335)";
    private String insert_ksiazka = "INSERT INTO KSIAZKA(id , id_osoba , tytul ) VALUES (24, 33, 'tytul ksiazki')";
    private String TABLE;
//    private String delete_from_osoba = "DELETE * FROM OSOBA";
//    private String delete_from_ksiazka = "DELETE * FROM KSIAZKA";

    public void deleteDatabase(Context context) {
        context.deleteDatabase("manager");
    }

    public DatabaseHandler(Context context) {
        super(context, "manager", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_osoba_table);
        db.execSQL(create_ksiazka_table);
        db.execSQL("PRAGMA foreign_keys=ON;");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //todo
    }

    public void addOsoba(Osoba osoba) {

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("nazwa", osoba.nazwa);
            values.put("rok_ur", osoba.rok_ur);
            db.insert("OSOBA", null, values);
            db.close();
        } catch (Exception e) {
            Log.e("myCustomException", e.toString());
        }

    }

    public void addKsiazka(Ksiazka ksiazka) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
//        values.put("id", ksiazka.id);
        values.put("id_osoba", ksiazka.id_osoba);
        values.put("tytul", ksiazka.tytul);
        db.insert("KSIAZKA", null, values);
        db.close();
    }

    public Osoba getOsoba(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("OSOBA", new String[]{"id", "nazwa", "rok_ur"}, "id" + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Osoba osoba = new Osoba();
        osoba.id = Integer.parseInt(cursor.getString(0));
        osoba.nazwa = cursor.getString(1);
        osoba.rok_ur = Integer.parseInt(cursor.getString(2));
        return osoba;
    }

    public List<Osoba> getAllOsoby() {
        List<Osoba> listaOsob = new ArrayList<Osoba>();
        String selectQuery = "SELECT * FROM OSOBA";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Osoba osoba = new Osoba();
                osoba.id = Integer.parseInt(cursor.getString(0));
                osoba.nazwa = cursor.getString(1);
                osoba.rok_ur = Integer.parseInt(cursor.getString(2));
                listaOsob.add(osoba);
            } while (cursor.moveToNext());
        }
        return listaOsob;
    }

    public List<Ksiazka> getAllKsiazki() {
        List<Ksiazka> listaKsiazek = new ArrayList<Ksiazka>();
        String selectQuery = "SELECT * FROM KSIAZKA";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Ksiazka ksiazka = new Ksiazka();
                ksiazka.id = Integer.parseInt(cursor.getString(0));
                ksiazka.id_osoba = Integer.parseInt(cursor.getString(1));
                ksiazka.tytul = cursor.getString(2);
                listaKsiazek.add(ksiazka);
            } while (cursor.moveToNext());
        }
        return listaKsiazek;
    }


//    values.put("nazwa", osoba.nazwa);
//    values.put("rok_ur", osoba.rok_ur);
//    db.insert("OSOBA", null, values);

    public void deleteOsoba(Osoba osoba) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("OSOBA", "nazwa=? AND rok_ur=?", new String[]{osoba.nazwa, String.valueOf(osoba.rok_ur)});
        db.close();
    }

    public void deleteKsiazka(Ksiazka ksiazka) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("KSIAZKA", "tytul=?", new String[]{String.valueOf(ksiazka.tytul)});
        db.close();
    }

    public int getLastOsobaID(){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT id FROM OSOBA WHERE ID = (SELECT MAX(id) FROM OSOBA)", null);
        cursor.moveToFirst();

        db.close();
        return  cursor.getInt(0);
    }

}
