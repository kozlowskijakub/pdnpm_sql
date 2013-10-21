package com.example.pdnpm_sql;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.*;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class OsobaActivity extends Activity {

    private EditText et_nazwa;
    private EditText et_rok_urodzenia;
    DatabaseHandler dbHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.osoba);

        this.et_nazwa = (EditText) findViewById(R.id.et_nazwa);
        this.et_rok_urodzenia = (EditText) findViewById(R.id.et_rokUrodzenia);
//        dbHandler.deleteDatabase(this);
        dbHandler = DatabaseHandler.getInstance(this);
        updateListView();


    }

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

        if (mainLayout.getChildCount() > 0)
            mainLayout.removeAllViews();

        for (Osoba osoba : listaOsob) {
            LinearLayout temp_ll = new LinearLayout(this);

            TextView id = new TextView(this);
            TextView nazwa = new TextView(this);
            TextView rok = new TextView(this);

            id.setText(String.valueOf(osoba.id));
            nazwa.setText(osoba.nazwa);
            rok.setText(String.valueOf(osoba.rok_ur));

            LinearLayout.LayoutParams params = null;
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.weight = 1;

            id.setLayoutParams(params);
            nazwa.setLayoutParams(params);

            rok.setLayoutParams(params);

            nazwa.setTextColor(Color.BLACK);
            rok.setTextColor(Color.WHITE);
            id.setTextColor(Color.GREEN);

            temp_ll.addView(id);
            temp_ll.addView(nazwa);
            temp_ll.addView(rok);

            registerForContextMenu(temp_ll);
            mainLayout.addView(temp_ll);
        }
        Toast.makeText(this, listaOsob.size() + " emenets..", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "delete") {
            Osoba osoba = new Osoba();
            osoba.rok_ur = Integer.parseInt(et_rok_urodzenia.getText().toString());
            osoba.nazwa = et_nazwa.getText().toString();
            dbHandler.deleteOsoba(osoba);
            updateListView();

        } else if (item.getTitle() == "add book") {
            item.getItemId();
            Intent intent = new Intent(this, KsiazkaActivity.class);
            intent.putExtra("id_user", item.getItemId());
            startActivity(intent);

        } else {
            return false;
        }
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        LinearLayout linearLayout = (LinearLayout) v;
        int id = Integer.parseInt(((TextView) linearLayout.getChildAt(0)).getText().toString());
        int id2 = v.getId();

        ((LinearLayout) v).setId(id);
        menu.setHeaderTitle("Context Menu");
        menu.add(0, id, 0, "delete");
        menu.add(0, id, 0, "add book");
        menu.add(0, id, 0, "show books");
    }

}
