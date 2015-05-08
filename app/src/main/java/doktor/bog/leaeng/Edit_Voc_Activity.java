package doktor.bog.leaeng;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.lang.reflect.Array;
import java.util.ArrayList;

import doktor.bog.leaeng.base_adapter.EditListAdapter;
import doktor.bog.leaeng.base_adapter.Items;
import doktor.bog.leaeng.base_adapter.ListFActivity;
import doktor.bog.leaeng.data_base.DataBaseHelper;
import doktor.bog.leaeng.data_base.DbOptions;

public class Edit_Voc_Activity extends ActionBarActivity {

    public ArrayList<Items> itemsE = new ArrayList<Items>();
    public Toolbar toolbar;
    private EditListAdapter adapter;
    private ListView lvMain;
    public String nameTClick;
    private SQLiteDatabase sdb;
    public DbOptions dbOpt;
    private DataBaseHelper sqlHelper;
    public  Spinner spinner2;
    public  Spinner spinner;
    public String[] planets;
    EditText ed;
    String _lang1 ;
    String _lang2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__voc);
        toolbar = (Toolbar) findViewById(R.id.app_bar_up);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        lvMain = (ListView) findViewById(R.id.listView2);

        spinner = (Spinner)findViewById(R.id.spinner3);
        spinner2 = (Spinner)findViewById(R.id.spinner4);


        Resources res = getResources();
        planets = res.getStringArray(R.array.langListP);




        getSupportActionBar().setTitle("Edit");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sqlHelper = new DataBaseHelper(this);
        sdb = sqlHelper.getWritableDatabase();
        dbOpt = new DbOptions(this, sqlHelper, sdb);



        nameTClick = dbOpt.getRealName((String) getIntent().getSerializableExtra("FILES_LIST"));
        getSupportActionBar().setTitle((String) getIntent().getSerializableExtra("FILES_LIST"));
        adapter = new EditListAdapter();
        itemsE = dbOpt.getWord(nameTClick);

        String langtoPars = dbOpt.getLangvTable(nameTClick);
        _lang1 =  langtoPars.substring(0,2);
        _lang2  = langtoPars.substring(3,5);
        for(int i=0; i<planets.length;i++) {
            if (_lang1.equals(planets[i])){
                spinner.setSelection(i);
            }
            if (_lang2.equals(planets[i])){
                spinner2.setSelection(i);
            }
        }

        ed = (EditText) findViewById(R.id.editText4);
        ed.setText((String) getIntent().getSerializableExtra("FILES_LIST"));


        adapter.EListAdapter(this, itemsE);
        adapter.sortByRevers();
        lvMain.setAdapter(adapter);


    }
            @Override
            public boolean onCreateOptionsMenu(Menu menu) {

                getMenuInflater().inflate(R.menu.menu_edit__voc, menu);
                return true;
            }

            @Override
            public boolean onOptionsItemSelected(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.action_accept:
    ed.requestFocus();
                        itemsE = adapter.objects;
                    for(int i=0;i<itemsE.size();i++) {
                        String sql_update = "UPDATE "
                                + nameTClick + " SET " + sqlHelper.WORD + "='" + itemsE.get(i).name + "'" + ", " +
                                sqlHelper.WORD_TRANSLATE + "='" + itemsE.get(i).name_translate + "'" +
                                " WHERE " + sqlHelper.UID + "='" + itemsE.get(i).id + "';";
                        sdb.execSQL(sql_update);

                    }



                        int i = spinner.getSelectedItemPosition();
                        int j = spinner2.getSelectedItemPosition();
                        _lang1 = planets[i];
                        _lang2 = planets[j];

                        if(!ed.getText().toString().equals("")) {
                            String sql_update2 = "UPDATE "
                                    + sqlHelper.TABLES + " SET " + sqlHelper.LANGV + "='" + _lang1 + "-" + _lang2 + "'" + ", " +
                                    sqlHelper.NAME_TABLE_USER + "='" + ed.getText().toString() + "'" +
                                    " WHERE " + sqlHelper.TABLE_NAME + "='" + nameTClick + "';";
                            sdb.execSQL(sql_update2);
                            getSupportActionBar().setTitle(ed.getText().toString());

                            saveName(ed.getText().toString());
                            Toast.makeText(this,
                                    "Saved", Toast.LENGTH_LONG).show();
                        }else  Toast.makeText(this,
                                "Enter the name of the vocabluary!", Toast.LENGTH_LONG).show();
                        return true;
                    case R.id.action_discard:
                        ed.requestFocus();
                        int idDelate = adapter.idForDelate;
                        if(idDelate!=-1){
                        String sql_delate = "DELETE FROM "+nameTClick+
                                " WHERE " + sqlHelper.UID + "='" + itemsE.get(idDelate).id + "';";
                        sdb.execSQL(sql_delate);
                        itemsE.remove(idDelate);
                        adapter.notifyDataSetChanged();
                        idDelate=-1;}
                        return true;
                    default:
                        return super.onOptionsItemSelected(item);
                }


            }
    public SharedPreferences sPref;
    final String SAVED_NAME = "saved_name_table";
    void saveName(String name) {
        sPref = getSharedPreferences(SAVED_NAME, 0);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(SAVED_NAME+"2", name);
        ed.commit();
    }
    @Override
    public void onBackPressed() {
        Intent upIntent = NavUtils.getParentActivityIntent(this);
        if (NavUtils.shouldUpRecreateTask(this, upIntent)) {

            TaskStackBuilder.create(this)

                    .addNextIntentWithParentStack(upIntent)

                    .startActivities();
        } else {

            NavUtils.navigateUpTo(this, upIntent);
        }

    }

    }
