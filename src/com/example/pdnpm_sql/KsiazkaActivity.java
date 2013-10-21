package com.example.pdnpm_sql;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jakub
 * Date: 10/19/13
 * Time: 9:39 AM
 * To change this template use File | Settings | File Templates.
 */
public class KsiazkaActivity extends Activity {

    private EditText et_nazwa;
    private int id_osoba;
    private DatabaseHandler dbHandler;
    private EditText et_ksiazka;
    private String LOG = "myException";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ksiazka);
        id_osoba = getIntent().getIntExtra("id_user", -1);
        dbHandler = DatabaseHandler.getInstance(this);
        this.et_ksiazka = (EditText) findViewById(R.id.et_tytul);

        Toast.makeText(this, dbHandler.getAllKsiazki().size() + " emenets..", 5000).show();
        updateListView();
    }

    public void addKsiazka(View v) {
        Ksiazka ksiazka = new Ksiazka();
        ksiazka.id_osoba = this.id_osoba;
        ksiazka.tytul = et_ksiazka.getText().toString();
        dbHandler.addKsiazka(ksiazka);
        updateListView();
    }
    public void deleteKsiazka(View v) {
        Ksiazka ksiazka = new Ksiazka();
        ksiazka.id_osoba = this.id_osoba;
        ksiazka.tytul = et_ksiazka.getText().toString();
        dbHandler.deleteKsiazka(ksiazka);
        updateListView();
    }

    private void updateListView() {

        List<Ksiazka> listaKsiazek = new ArrayList<Ksiazka>();
        listaKsiazek = dbHandler.getAllOsobaKsiazki(this.id_osoba);

        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.ll_listaKsiazek);
        int x = mainLayout.getChildCount();
        if (mainLayout.getChildCount() > 0)
            mainLayout.removeAllViews();

        for (Ksiazka ksiazka : listaKsiazek) {
            LinearLayout temp_ll2 = new LinearLayout(this);

            TextView id_osoba = new TextView(this);
            TextView id_ksiazka = new TextView(this);
            TextView tytul = new TextView(this);

            id_osoba.setText(String.valueOf(ksiazka.id_osoba));
            id_ksiazka.setText(String.valueOf(ksiazka.id));
            tytul.setText(ksiazka.tytul);

            LinearLayout.LayoutParams params = null;
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.weight = 1;

            id_osoba.setLayoutParams(params);
            id_ksiazka.setLayoutParams(params);
            tytul.setLayoutParams(params);


            id_osoba.setTextColor(Color.BLACK);
            id_ksiazka.setTextColor(Color.BLACK);
            tytul.setTextColor(Color.BLACK);

            temp_ll2.addView(id_ksiazka);
            temp_ll2.addView(id_osoba);
            temp_ll2.addView(tytul);

            registerForContextMenu(temp_ll2);


            mainLayout.addView(temp_ll2);
        }
//        Toast.makeText(this, listaKsiazek.size() + " emenets..", Toast.LENGTH_LONG).show();
    }
}