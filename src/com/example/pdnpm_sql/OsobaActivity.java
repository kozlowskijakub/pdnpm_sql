package com.example.pdnpm_sql;

import android.app.ActionBar;
import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class OsobaActivity extends Activity {

    private EditText et_id;
    private EditText et_nazwa;
    private EditText et_rok_urodzenia;
    DatabaseHandler dbHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.osoba);

        this.et_nazwa = (EditText) findViewById(R.id.et_nazwa);
        this.et_rok_urodzenia = (EditText) findViewById(R.id.et_rokUrodzenia);
//        this.et_id = (EditText) findViewById(R.id.et)


        dbHandler = new DatabaseHandler(this);
        dbHandler.deleteDatabase(this);

    }


    // factory can be null
    // mode = MODE_PRIVATE
    public void addOsoba(View v) {
        Osoba osoba = new Osoba();
        osoba.rok_ur = Integer.parseInt(et_rok_urodzenia.getText().toString());
        osoba.nazwa = et_nazwa.getText().toString();
        dbHandler.addOsoba(osoba);

        updateListView();

    }

    public void deleteOsoba(View v) {
        Osoba osoba = new Osoba();
        osoba.rok_ur = Integer.parseInt(et_rok_urodzenia.getText().toString());
        osoba.nazwa = et_nazwa.getText().toString();
        dbHandler.deleteOsoba(osoba);
        updateListView();
    }

    public void deleteAllOsoba() {

    }

    private void updateListView() {

        List<Osoba> listaOsob = new ArrayList<Osoba>();
        listaOsob = dbHandler.getAllOsoby();

        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.ll_listaOsob);

        if(mainLayout.getChildCount() > 0)
            mainLayout.removeAllViews();

        for (Osoba osoba : listaOsob) {
            LinearLayout temp_ll = new LinearLayout(this);
            TextView id = new TextView(this);
            TextView nazwa = new TextView(this);
            TextView rok = new TextView(this);
            nazwa.setText(osoba.nazwa);

            rok.setText(String.valueOf(osoba.rok_ur));

            LinearLayout.LayoutParams params = null;

            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.weight = 1;
            nazwa.setLayoutParams(params);
            rok.setLayoutParams(params);
            nazwa.setTextColor(Color.WHITE);
            rok.setTextColor(Color.WHITE);

            temp_ll.addView(nazwa);
            temp_ll.addView(rok);



            mainLayout.addView(temp_ll);
        }
        Toast.makeText(this, listaOsob.size() + " emenets..", Toast.LENGTH_LONG).show();
    }

}
