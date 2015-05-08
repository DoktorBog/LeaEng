package doktor.bog.leaeng.base_adapter;

/**
 * Created by Grechanuyk B.O on 26.01.2015.
 */

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import doktor.bog.leaeng.MainActivity;
import doktor.bog.leaeng.R;
import doktor.bog.leaeng.data_base.DataBaseHelper;
import doktor.bog.leaeng.data_base.DbOptions;


public class ListFActivity extends BaseAdapter {


    String _strL;
    Button _button2;
    Button _button3;
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Items> objects;
    TTSManager ttsm;
    MainActivity mA;
    Drawable myDrawable1;
    Drawable myDrawable2;
    DbOptions db;
    String tableN;
   boolean mode;

    public ListFActivity() {
    }

    public  void Liss (Context context, ArrayList<Items>  items,String _tableN,DbOptions _db,boolean _mode) {
        mode =_mode;
        db=_db;
     tableN=_tableN;
      ctx =  context;
      objects = items;
      lInflater = (LayoutInflater) ctx
              .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // кол-во
    @Override
    public int getCount() {
        return objects.size();
    }

    // позициия
    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
       mA = new MainActivity();
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item, parent, false);
        }

        Items f = getFlowers(position);


        ((TextView) view.findViewById(R.id.textView)).setText(f.name);
        ((TextView) view.findViewById(R.id.textView2)).setText(f.name_translate + "");

     //   mTTS = new TextToSpeech(ctx, this);

        _button2 = (Button)  view.findViewById(R.id.button2);
        _button3 = (Button)  view.findViewById(R.id.button3);

        f.bl2 = _button2;
        f.bl1 = _button3;


        Intent installIntent = new Intent();
        installIntent.setAction(
                TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
        Resources res = ctx.getResources();
        ImageView ratingUp = (ImageView) view.findViewById(R.id.imageView);
        myDrawable1 = res.getDrawable(R.drawable.ic_action_not_important);
        myDrawable2= res.getDrawable(R.drawable.ic_star);
        ttsm = new TTSManager();
       ttsm.init(ctx);

        if (getFlowers(position).isRatedUp){
            ratingUp.setImageDrawable(myDrawable2);

        }
        else{
            ratingUp.setImageDrawable(myDrawable1);
        }

        OnClickListener oclBtnOk = new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.button2:
                        String text2 = objects.get(position).name_translate.toString();
                       ttsm.addQueue(text2,new Locale( _strL.substring(3,5)));
                        break;

                    case R.id.button3:
                        String text3 = objects.get(position).name.toString();
                     ttsm.addQueue(text3,new Locale(_strL.substring(0,2)));
                        break;
                    case R.id.imageView:
                    if(!getFlowers(position).isRatedUp){
                        ((ImageView)v).setImageDrawable(myDrawable2);
                        getFlowers(position).setRatedUp(true);
                        db.writeStar(tableN,objects.get(position).id,"true");


                    }else{

                        ((ImageView)v).setImageDrawable(myDrawable1);
                        getFlowers(position).setRatedUp(false);
                        db.writeStar(tableN,objects.get(position).id,"false");
                        if(mode){
                            objects.remove(position);
                            notifyDataSetChanged();
                        }
                    }
                        break;
                }
            }
        };
        _button2.setOnClickListener(oclBtnOk);
        _button3.setOnClickListener(oclBtnOk);




        ratingUp.setOnClickListener(oclBtnOk);


        return view;


    }


    Items getFlowers(int position) {
        return (( Items) getItem(position));
    }

 public void sortByRevers() {
     Collections.reverse(objects);
     notifyDataSetChanged();
 }
    public void LengStr(String strL){
        _strL = strL;
    }

 public int gater(int i){
        return i;
    }

}
