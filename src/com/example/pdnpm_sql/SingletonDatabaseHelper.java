package com.example.pdnpm_sql;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created with IntelliJ IDEA.
 * User: jakub
 * Date: 10/21/13
 * Time: 1:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class SingletonDatabaseHelper extends SQLiteOpenHelper {

    private static SingletonDatabaseHelper mInstance;
    private static SQLiteDatabase myWritableDb;
    private final Context myContext;

    public SingletonDatabaseHelper(Context context) {
        super(context, "databaseName", null, 1);
        this.myContext = context;
    }

    public static SingletonDatabaseHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SingletonDatabaseHelper(context);
        }
        return mInstance;
    }




    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
