package com.mvit.mihajlo.onekeep.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.mvit.mihajlo.onekeep.R;
import com.mvit.mihajlo.onekeep.db.DatabaseHelper;
import com.mvit.mihajlo.onekeep.db.model.Nekretnina;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Mihajlo on 09-Oct-17.
 */

public class Notepad extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_note_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button add = (Button) findViewById(R.id.add_note);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText name = (EditText) findViewById(R.id.note_name);
                EditText bio = (EditText) findViewById(R.id.note_description);

                Nekretnina a = new Nekretnina();
                a.setmName(name.getText().toString());
                a.setmBiography(bio.getText().toString());

                try {
                    getDatabaseHelper().getNekretninaDao().create(a);
                    //REFRESH
                    refresh();

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                finish();
                //dialog.dismiss();
            }
        });
    }


    //Metoda koja komunicira sa bazom podataka
    public DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    protected void refresh() {
        ListView listview = (ListView) findViewById(R.id.nekretnine_list);
        if (listview != null){
            ArrayAdapter<Nekretnina> adapter = (ArrayAdapter<Nekretnina>) listview.getAdapter();
            if(adapter!= null)
            {
                try {
                    adapter.clear();
                    List<Nekretnina> list = getDatabaseHelper().getNekretninaDao().queryForAll();
                    adapter.addAll(list);
                    adapter.notifyDataSetChanged();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

}
