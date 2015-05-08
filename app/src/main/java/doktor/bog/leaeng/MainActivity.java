package doktor.bog.leaeng;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v4.app.NavUtils;
import android.support.v4.app.ShareCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import doktor.bog.leaeng.base_adapter.*;
import doktor.bog.leaeng.data_base.DataBaseHelper;
import doktor.bog.leaeng.data_base.DbOptions;
import doktor.bog.leaeng.network.NetworkChecker;
import android.view.View.OnClickListener;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.ShareActionProvider;

import com.melnykov.fab.FloatingActionButton;

public class MainActivity extends ActionBarActivity {

    private String testJ = "0";
    public  JSONObject json;
    public  String textFor = "";
    private EditText _editText;
    private EditText _editText2;
    private Button bl1;
    private String _url = "https://translate.yandex.net/api/v1.5/tr.json/translate?key=";
    private DataBaseHelper sqlHelper;
    public ArrayList<Items> items = new ArrayList<Items>();
    public ArrayList<Items> items2 = new ArrayList<Items>();
    public ArrayList<ItemsNDFragment> _itemsNDFragments = new ArrayList<ItemsNDFragment>();
    private ListFActivity boxAdapter;
    private ItemsNDFragmantAdapter NDAdapter;
    private ListView lvMain;
    private ListView listViewFragmentAdapter;
    private CheckBox _checkBox;
    public Toolbar toolbar;
    private SQLiteDatabase sdb;
    public DbOptions dbOpt;
    private LinearLayout _ad;
    private RelativeLayout _ad2;
    private final int IDD_THREE_BUTTONS = 0;
    private final int IDD_THREE_BUTTONS_2 = 1;
    private String[] lalng;
    public  NavagationDrawerFragment drawerFragment;
    public String nameTClick;
    private SharedPreferences sPref;
    final String SAVED_NAME = "saved_name_table";
    public boolean mode = false;
    public Switch s;
    public static final int DIALOG_DOWNLOAD_JSON_PROGRESS = 2;
    private ProgressDialog mProgressDialog;
    private ShareActionProvider mShareActionProvider;
    public ImageView im1;
    public TextView tev1;
    public TextView tev2;
    public TextView tev3;
    public Button play;
    public Button bl4;
    public FloatingActionButton fab;
    public ImageView star;
    public TextView mod;
    public int cos = 0;
    public int cos2 = 0;


    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      setVolumeControlStream(AudioManager.STREAM_MUSIC);

      fab = (FloatingActionButton) findViewById(R.id.fab);

      toolbar = (Toolbar) findViewById(R.id.app_bar);
      setSupportActionBar(toolbar);

       getSupportActionBar().setDisplayShowHomeEnabled(true);

      DrawerLayout  mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
      drawerFragment = (NavagationDrawerFragment)
              getSupportFragmentManager().findFragmentById(R.id.fragment_navagation_drawer);

      drawerFragment.setUp(R.id.fragment_navagation_drawer,mDrawerLayout,toolbar);
      play = (Button)findViewById(R.id.play);
      bl4 = (Button) findViewById(R.id.button4);
      im1 = (ImageView) findViewById(R.id.im1);
      tev1 = (TextView) findViewById(R.id.textV1);
      tev2 = (TextView) findViewById(R.id.textV2);
      tev3 = (TextView) findViewById(R.id.textV10);
        star = (ImageView) findViewById(R.id.imageView2);
        mod = (TextView) findViewById(R.id.textView6);

      sqlHelper = new DataBaseHelper(this);
      sdb = sqlHelper.getWritableDatabase();
      dbOpt = new DbOptions(this,sqlHelper,sdb);
      lvMain = (ListView) findViewById(R.id.listView);
        s = (Switch) findViewById(R.id.switch1);
      listViewFragmentAdapter = (ListView) findViewById(R.id.ndf_adapter_listView);
      NDAdapter = new ItemsNDFragmantAdapter();
      _itemsNDFragments = dbOpt.getLangvArray();
      NDAdapter.NDFragmantAdapter(this,_itemsNDFragments);
      listViewFragmentAdapter.setAdapter(NDAdapter);

      if(_itemsNDFragments.size()>0){
          star.setVisibility(View.VISIBLE);
          mod.setVisibility(View.VISIBLE);
          tev3.setVisibility(View.INVISIBLE);
          fab.setVisibility(View.VISIBLE);
          s.setVisibility(View.VISIBLE);
          play.setVisibility(View.VISIBLE);
          fab.setFocusable(true);
          s.setFocusable(true);
          play.setFocusable(true);
          s.setChecked(false);
          cos = 1;
          cos2=1;
          tev3.setVisibility(View.INVISIBLE);}
      else{
          tev3.setVisibility(View.VISIBLE);
          fab.setVisibility(View.INVISIBLE);
       s.setVisibility(View.INVISIBLE);
          play.setVisibility(View.INVISIBLE);
          fab.setFocusable(false);
       s.setFocusable(false);
          play.setFocusable(false);
      }

      StrictMode.ThreadPolicy policy = new StrictMode.
              ThreadPolicy.Builder().permitAll().build();
      StrictMode.setThreadPolicy(policy);

      fab.attachToListView(lvMain);


      if(!loadName().equals("")){
          nameTClick = dbOpt.getRealName(loadName());
          getSupportActionBar().setTitle(loadName());
          boxAdapter = new ListFActivity();
          boxAdapter.LengStr(dbOpt.getLangvTable(nameTClick));
          items = dbOpt.getWord(nameTClick);
          boxAdapter.Liss(MainActivity.this,items,nameTClick,dbOpt,mode);
          boxAdapter.sortByRevers();
          lvMain.setAdapter(boxAdapter);
          drawerFragment.closeD();

          if(items.size()>0){
              im1.setVisibility(View.INVISIBLE);
              tev1.setVisibility(View.INVISIBLE);
              tev2.setVisibility(View.INVISIBLE);
          }else{
              im1.setVisibility(View.VISIBLE);
              tev1.setVisibility(View.VISIBLE);
              tev2.setVisibility(View.VISIBLE);
              tev2.setText("Create your first point!");
          }
      }




      OnClickListener oclBtn = new OnClickListener() {
          @Override
          public void onClick(View v) {
              switch (v.getId()) {

                  case R.id.button4:
                      showDialog(IDD_THREE_BUTTONS);
                      break;
                  case R.id.fab:
                      showDialog(IDD_THREE_BUTTONS_2);
                      break;
                  case R.id.play:
                      if(cos2==1){
                      Intent intent = new Intent(MainActivity.this, Game.class);
                      intent.putExtra("FILES_LIST", loadName());
                      startActivity(intent);}
                      break;

              }
          }
      };

     play.setOnClickListener(oclBtn);
     bl4.setOnClickListener(oclBtn);
      fab.setOnClickListener(oclBtn);

      s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

          @Override
          public void onCheckedChanged(CompoundButton buttonView,
                                       boolean isChecked) {

              if (isChecked) {
                  for(int i = 0; i<items.size(); i++){
                      if(items.get(i).isRatedUp==true){
                          items2.add(items.get(i));
                      }

                  }
                  mode = true;
                  boxAdapter.Liss(MainActivity.this,items2,nameTClick,dbOpt,mode);
                  boxAdapter.notifyDataSetChanged();

              } else {
                  items2.clear();
                  mode = false;
                  boxAdapter.Liss(MainActivity.this,items,nameTClick,dbOpt,mode);
                  boxAdapter.notifyDataSetChanged();
              }

          }
      });

      listViewFragmentAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              s.setChecked(false);
              nameTClick = dbOpt.getRealName(_itemsNDFragments.get(position).nameT);
              getSupportActionBar().setTitle(_itemsNDFragments.get(position).nameT);
              saveName(_itemsNDFragments.get(position).nameT);
              boxAdapter = new ListFActivity();
              boxAdapter.LengStr(dbOpt.getLangvTable(nameTClick));
              items = dbOpt.getWord(nameTClick);
              boxAdapter.Liss(MainActivity.this,items,nameTClick,dbOpt,mode);
              boxAdapter.sortByRevers();
              lvMain.setAdapter(boxAdapter);
              drawerFragment.closeD();

              if(items.size()>0){
                  im1.setVisibility(View.INVISIBLE);
                  tev1.setVisibility(View.INVISIBLE);
                  tev2.setVisibility(View.INVISIBLE);
              }else{
                  im1.setVisibility(View.VISIBLE);
                  tev1.setVisibility(View.VISIBLE);
                  tev2.setVisibility(View.VISIBLE);
              }


          }
      });
  }
    public String readBugzilla(String textForQ) {

        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(_url + getString(R.string.IDYandexTr) + "&text=" + textForQ + "&lang="+dbOpt.getLangvTable(nameTClick));
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } else {

            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    public String removeChar(String s) {
        if (s == null || s.length() == 0) {
            return s;
        }
        return s.substring(2, s.length()-2);
    }

    @Override
    public void onDestroy() {
        super.onStop();

        sqlHelper.close();
        sqlHelper.close();
        super.onDestroy();
    }

    public void AddBtn(){
        textFor = _editText.getText().toString();
        if ( _checkBox.isChecked()) {
            int len;
            do {
                len = textFor.length();
                textFor = textFor.replaceAll(" ", "%20");
                textFor = textFor.replaceAll("\"", "");

            } while (len != textFor.length());

            String input = readBugzilla(textFor);

            try {

                json = new JSONObject(input);
                testJ = json.getString("text");
                testJ = removeChar(testJ);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            testJ = _editText2.getText().toString();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.menu_share);

        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        String playStoreLink = "https://play.google.com/store/apps/details?id=" +
                getPackageName();
        String yourShareText = "Install this app " + playStoreLink;
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain").setText(yourShareText).getIntent();

        mShareActionProvider.setShareIntent(shareIntent);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
if(cos == 1) {
    Intent intent = new Intent(MainActivity.this, Edit_Voc_Activity.class);
    intent.putExtra("FILES_LIST", loadName());
    startActivity(intent);
}
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {

            case DIALOG_DOWNLOAD_JSON_PROGRESS:

                mProgressDialog = new ProgressDialog(this);

                mProgressDialog.setMessage("Translate.....");

                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

                mProgressDialog.setCancelable(true);

                mProgressDialog.show();

                return mProgressDialog;

            case IDD_THREE_BUTTONS:

            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setTitle("Create new vocabulary");

            _ad = (LinearLayout) getLayoutInflater()
                    .inflate(R.layout.dialog_create_table, null);
                  adb.setView(_ad);
                Button btL = (Button) _ad.findViewById(R.id.btL);

                btL.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String lang1;
                        String lang2;
                        Spinner spinner = (Spinner)_ad.findViewById(R.id.spinner);
                        Spinner spinner2 = (Spinner)_ad.findViewById(R.id.spinner2);
                        Resources res = getResources();
                        lalng = res.getStringArray(R.array.langListP);
                        int i = spinner.getSelectedItemPosition();
                        int j = spinner2.getSelectedItemPosition();
                        lang1 = lalng[i];
                        lang2 = lalng[j];
                        TextView tx = (TextView)_ad.findViewById(R.id.editText3);
                        if(!tx.getText().toString().equals("")){
                        dbOpt.createTable(tx.getText().toString(),lang1+"-"+lang2);

                            nameTClick = dbOpt.getRealName(tx.getText().toString());
                            getSupportActionBar().setTitle(tx.getText().toString());
                            saveName(tx.getText().toString());
                            boxAdapter = new ListFActivity();
                            boxAdapter.LengStr(dbOpt.getLangvTable(nameTClick));
                            items = dbOpt.getWord(nameTClick);
                            boxAdapter.Liss(MainActivity.this,items,nameTClick,dbOpt,mode);
                            boxAdapter.sortByRevers();
                            lvMain.setAdapter(boxAdapter);
                            drawerFragment.closeD();

                            star.setVisibility(View.VISIBLE);
                            mod.setVisibility(View.VISIBLE);
                            tev3.setVisibility(View.INVISIBLE);
                            fab.setVisibility(View.VISIBLE);
                          s.setVisibility(View.VISIBLE);
                            play.setVisibility(View.VISIBLE);
                            fab.setFocusable(true);
                          s.setFocusable(true);
                            play.setFocusable(true);
                            s.setChecked(false);
                       cos = 1;
                                im1.setVisibility(View.VISIBLE);
                                tev1.setVisibility(View.VISIBLE);
                                tev2.setVisibility(View.VISIBLE);
                            tev2.setText("Create your first point!");
                        _itemsNDFragments.add(new ItemsNDFragment(tx.getText().toString(), lang1 + "-" + lang2));
                        NDAdapter.notifyDataSetChanged();
                        removeDialog(IDD_THREE_BUTTONS);
                        drawerFragment.closeD();}else{

                        }

                    }
                });

            return adb.create();

            case IDD_THREE_BUTTONS_2:

                AlertDialog.Builder adb2 = new AlertDialog.Builder(MainActivity.this);
                adb2.setTitle("New pointe");

                _ad2 = (RelativeLayout) getLayoutInflater()
                        .inflate(R.layout.dialog_add_word, null);
                adb2.setView(_ad2);
                _editText = (EditText) _ad2.findViewById(R.id.editText);
                _checkBox = (CheckBox) _ad2.findViewById(R.id.checkBox);
                _editText2 = (EditText) _ad2.findViewById(R.id.editText2);

                if(NetworkChecker.isNetworkAvailable(this)){
                    _editText2.setEnabled(false);
                    _checkBox.setChecked(true);}
                else{_editText2.setEnabled(true);
                    _checkBox.setChecked(false);}

                bl1 = (Button) _ad2.findViewById(R.id.button);

                OnClickListener oclBtn2 = new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {

                            case R.id.button:
                                if(NetworkChecker.isNetworkAvailable(MainActivity.this)) {
                                    new DownloadJSONFileAsync().execute();
                                }
                                else{
                                    _editText2.setEnabled(true);
                                    _checkBox.setChecked(false);
                                    if(!_editText2.getText().toString().equals(""))
                                    onPost(_editText2.getText().toString());
                                }

                                if(items.size()>0){
                                    im1.setVisibility(View.INVISIBLE);
                                    tev1.setVisibility(View.INVISIBLE);
                                    tev2.setVisibility(View.INVISIBLE);
                                }else{
                                    im1.setVisibility(View.VISIBLE);
                                    tev1.setVisibility(View.VISIBLE);
                                    tev2.setVisibility(View.VISIBLE);
                                }
                                break;

                            case R.id.checkBox:

                                if (((CheckBox) v).isChecked()) {
                                    if(NetworkChecker.isNetworkAvailable(MainActivity.this)){
                                        _editText2.setEnabled(false);}else{
                                        _checkBox.setChecked(false);
                                        Toast.makeText(MainActivity.this,
                                                "Включите интернет", Toast.LENGTH_LONG).show();
                                    }
                                }else
                                    _editText2.setEnabled(true);
                                break;

                        }
                    }
                };

                bl1.setOnClickListener(oclBtn2);
                 _checkBox.setOnClickListener(oclBtn2);

                return adb2.create();

            default:
                return null;
        }

    }

    void saveName(String name) {
        sPref = getSharedPreferences(SAVED_NAME, 0);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(SAVED_NAME+"2", name);
        ed.commit();
    }

    String loadName() {
        sPref = getSharedPreferences(SAVED_NAME, 0);
        String savedName = sPref.getString(SAVED_NAME + "2", "");

        return savedName;
    }

    public class DownloadJSONFileAsync extends AsyncTask<String, Void, Void> {

        protected void onPreExecute() {

            super.onPreExecute();

            showDialog(DIALOG_DOWNLOAD_JSON_PROGRESS);

        }

        @Override

        protected Void doInBackground(String... params) {

            AddBtn();
            return null;

        }

       protected void onPostExecute(Void unused) {
           onPost(testJ);
           dismissDialog(DIALOG_DOWNLOAD_JSON_PROGRESS);

            removeDialog(DIALOG_DOWNLOAD_JSON_PROGRESS);

        }

    }

    public void onPost(String strB){

        String wordToBase = _editText.getText().toString();
        wordToBase = wordToBase.replaceAll("'", "");
        String wordToBase_translate = strB;
        wordToBase_translate = wordToBase_translate.replaceAll("'", "");
        if (!wordToBase_translate.equals("")) {
            if (!wordToBase.equals("")) {
                if (!wordToBase_translate.equals("0")) {
                    dbOpt.writeInfoInTable(nameTClick, wordToBase, wordToBase_translate);
                    if(mode){
                        items2.add(0, new Items(-1,wordToBase,
                                wordToBase_translate,true));
                        items.add(0, new Items(-1,wordToBase,
                                wordToBase_translate,true));
                        boxAdapter.notifyDataSetChanged();
                    }else{
                        items.add(0, new Items(-1,wordToBase,
                                wordToBase_translate,false));
                        boxAdapter.notifyDataSetChanged();}

                    _editText.setText("");
                    _editText2.setText("");
                    removeDialog(IDD_THREE_BUTTONS_2);
                    im1.setVisibility(View.INVISIBLE);
                    tev1.setVisibility(View.INVISIBLE);
                    tev2.setVisibility(View.INVISIBLE);
                    cos2=1;
                }else {
                    Toast.makeText(MainActivity.this,
                            "Невозможно перевести :(", Toast.LENGTH_LONG).show();
                }
            }
        }
        testJ ="0";
    }

    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }
}


