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

    private static String create_osoba_table = "CREATE TABLE IF NOT EXISTS" +
            " OSOBA(id INTEGER PRIMARY KEY AUTOINCREMENT," +
            " nazwa VARCHAR," +
            " rok_ur INT(3))";

    private static String create_ksiazka_table = "CREATE TABLE IF NOT EXISTS" +
            " KSIAZKA(id INTEGER PRIMARY KEY AUTOINCREMENT," +
            " id_osoba INTEGER," +
            " tytul VARCHAR," +
            " FOREIGN KEY(id_osoba) REFERENCES OSOBA(id))";

    private static String insert_osoba = "INSERT INTO OSOBA(nazwa, rok_ur) VALUES ('Na', 1335)";
    private static String insert_ksiazka = "INSERT INTO KSIAZKA(id , id_osoba , tytul ) VALUES (24, 33, 'tytul ksiazki')";
    private static String TABLE;

    private static DatabaseHandler dbhInstance;
    private static SQLiteDatabase mySQLiteDatabase;
    private final Context context;

    public static void deleteDatabase(Context context) {
        context.deleteDatabase("manager");
    }

    private DatabaseHandler(Context context) {
        super(context, "manager", null, 1);
        this.context = context;
    }

    public static DatabaseHandler getInstance(Context context) {
        if (dbhInstance == null) {
            dbhInstance = new DatabaseHandler(context);
        }
        return dbhInstance;
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
            SQLiteDatabase db = this.getMyWritableDb();
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
        SQLiteDatabase db = this.getMyWritableDb();
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
        SQLiteDatabase db = this.getMyWritableDb();
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
        SQLiteDatabase db = this.getMyWritableDb();
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

    public List<Ksiazka> getAllOsobaKsiazki(int id_osoba) {
        List<Ksiazka> listaKsiazek = new ArrayList<Ksiazka>();
        String selectQuery = "SELECT * FROM KSIAZKA WHERE id_osoba=" + id_osoba;
        SQLiteDatabase db = this.getMyWritableDb();
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

    public void deleteBooksForPerson(ArrayList<Osoba> personsList, SQLiteDatabase db) {
        for (Osoba osoba : personsList) {
            Osoba o = osoba;
            db.delete("KSIAZKA", "id_osoba=?", new String[]{String.valueOf(o.id)});
        }
    }

    public void deleteOsoba(Osoba osoba) {
        SQLiteDatabase db = this.getMyWritableDb();
        ArrayList<Osoba> personsList = new ArrayList<Osoba>();
        Cursor cursor = db.rawQuery("SELECT id FROM OSOBA WHERE nazwa='" + osoba.nazwa + "' AND rok_ur='" + osoba.rok_ur + "'", null);
        if (cursor.moveToFirst()) {
            do {
                Osoba tmpOsoba = new Osoba();
                tmpOsoba.id = cursor.getInt(cursor.getColumnIndex("id"));
                personsList.add(tmpOsoba);
            } while (cursor.moveToNext());
        }

        deleteBooksForPerson(personsList, db);
        db.delete("OSOBA", "nazwa=? AND rok_ur=?", new String[]{osoba.nazwa, String.valueOf(osoba.rok_ur)});
        db.close();
    }

    public void deleteKsiazka(Ksiazka ksiazka) {
        SQLiteDatabase db = this.getMyWritableDb();
        db.delete("KSIAZKA", "tytul=? AND id_osoba=?", new String[]{String.valueOf(ksiazka.tytul), String.valueOf(ksiazka.id_osoba)});

        db.close();
    }

    private int getLastOsobaID() {
        SQLiteDatabase db = this.getMyWritableDb();

        Cursor cursor = db.rawQuery("SELECT id FROM OSOBA WHERE ID = (SELECT MAX(id) FROM OSOBA)", null);
        cursor.moveToFirst();

        db.close();
        return cursor.getInt(0);
    }

    private SQLiteDatabase getMyReadableDb() {
        if ((this.mySQLiteDatabase == null) || (!mySQLiteDatabase.isOpen())) {
            mySQLiteDatabase = this.getReadableDatabase();
        }
        return mySQLiteDatabase;
    }

    private SQLiteDatabase getMyWritableDb() {
        if ((this.mySQLiteDatabase == null) || (!mySQLiteDatabase.isOpen())) {
            mySQLiteDatabase = this.getWritableDatabase();
        }
        return mySQLiteDatabase;
    }

    @Override
    public void close() {
        super.close();
        if (mySQLiteDatabase != null) {
            mySQLiteDatabase.close();
            mySQLiteDatabase = null;
        }
    }

}
