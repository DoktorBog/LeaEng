package doktor.bog.leaeng;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import doktor.bog.leaeng.base_adapter.EditListAdapter;
import doktor.bog.leaeng.base_adapter.Items;
import doktor.bog.leaeng.base_adapter.ListFActivity;
import doktor.bog.leaeng.data_base.DataBaseHelper;
import doktor.bog.leaeng.data_base.DbOptions;


public class Game extends ActionBarActivity {

    public EditText answer;
    public TextView word;
    public TextView wordInround;
    public ImageButton next;
    public Button restart;
    private SQLiteDatabase sdb;
    public DbOptions dbOpt;
    private DataBaseHelper sqlHelper;
    public ArrayList<Items> items = new ArrayList<Items>();
    public String nameTClick;
    private int i = 0;
    private View.OnClickListener oclBtn;
    private int size;
    public ArrayList<Items> itemsWrong = new ArrayList<Items>();
    public ArrayList<Items> itemsCorect = new ArrayList<Items>();
    private ListFActivity boxAdapterWrong;
    private ListFActivity boxAdapterCorect;
    public ListView lvWrong;
    public ListView lvCorect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        find();

       sqlHelper = new DataBaseHelper(this);
       sdb = sqlHelper.getWritableDatabase();
       dbOpt = new DbOptions(this, sqlHelper, sdb);

       nameTClick = dbOpt.getRealName((String) getIntent().getSerializableExtra("FILES_LIST"));
       items = dbOpt.getWord(nameTClick);
       size = items.size();
        long seed = System.nanoTime();
        Collections.shuffle(items, new Random(seed));
       word.setText(items.get(i).name);

            wordInround.setText((i + 1) + "/"+items.size());

        oclBtn= new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.buttonNext:

                                if (items.get(i).name_translate.equals(answer.getText().toString())) {
                                    itemsCorect.add(items.get(i));
                                } else {
                                    itemsWrong.add(items.get(i));
                                }
                                i++;
                            if (i < items.size()) {
                                word.setText(items.get(i).name);
                                wordInround.setText((i + 1) + "/"+items.size());
                            } else {
                                complite();}

                break;

                case R.id.button5:
                setContentView(R.layout.activity_game);
                find();
                restart();
                break;
            }
        }

       };
        next.setOnClickListener(oclBtn);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void complite(){
        setContentView(R.layout.next);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(next.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        restart = (Button) findViewById(R.id.button5);
        lvWrong = (ListView) findViewById(R.id.listViewWrong);
        lvCorect = (ListView) findViewById(R.id.listViewCorrect);
        restart.setOnClickListener(oclBtn);

        boxAdapterWrong = new ListFActivity();

        boxAdapterWrong.Liss(this,itemsWrong,nameTClick,dbOpt,false);
        boxAdapterWrong.sortByRevers();
        lvWrong.setAdapter(boxAdapterWrong);

        boxAdapterCorect = new ListFActivity();

        boxAdapterCorect.Liss(this,itemsCorect,nameTClick,dbOpt,false);
        boxAdapterCorect.sortByRevers();
        lvCorect.setAdapter(boxAdapterCorect);

    }

    public void find(){
        answer = (EditText) findViewById(R.id.edAnswer);
        word = (TextView) findViewById(R.id.edWord);
        wordInround = (TextView) findViewById(R.id.tvNumber);
        next = (ImageButton) findViewById(R.id.buttonNext);
    }

    public void restart(){
        i=0;
        wordInround.setText((i + 1) + "/" + items.size());
        itemsCorect.clear();
        itemsWrong.clear();
        long seed = System.nanoTime();
        Collections.shuffle(items, new Random(seed));

        word.setText(items.get(i).name);
        next.setOnClickListener(oclBtn);
    }

}
