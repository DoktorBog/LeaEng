package doktor.bog.leaeng.base_adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.nfc.Tag;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import doktor.bog.leaeng.R;

/**
 * Created by Hawk on 12.04.2015.
 */
public class EditListAdapter extends BaseAdapter {


    public LayoutInflater lInflater;
    public ArrayList<Items> objects;
    public ArrayList<Items> objects2 = new ArrayList<Items>();
    public Context ctx;
    public int idForDelate=0;


    public  void EListAdapter (Context context, ArrayList<Items>  items) {

        ctx =  context;
        objects = items;
        objects2 = objects;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    public EditText edittext;
    public EditText edittext2;

    Items itemsE;
    @Override
    public View getView(final int position, View convertView,ViewGroup parent) {
        View  view = convertView;

        if (view == null) {

            view = lInflater.inflate(R.layout.edit_list, parent, false);
        }

        itemsE = getItemsE(position);


        edittext= (EditText) view.findViewById(R.id.editName2);
        edittext.setText(itemsE.name);
        edittext2= (EditText) view.findViewById(R.id.editName1);
        edittext2.setText(itemsE.name_translate);


        View.OnFocusChangeListener focuseListener = new  View.OnFocusChangeListener(){
            public void onFocusChange(View v, boolean hasFocus){
                if(hasFocus){
                    switch (v.getId()) {
                        case R.id.editName2:
                          idForDelate = position;
                            break;
                        case R.id.editName1:
                            idForDelate = position;

                            break;
                    }
                } else {
                    switch (v.getId()) {
                        case R.id.editName2:
                            objects2.get(position).name = ((EditText) v.findViewById(R.id.editName2)).getText().toString();
                            break;
                        case R.id.editName1:
                            objects2.get(position).name_translate = ((EditText) v.findViewById(R.id.editName1)).getText().toString();

                            break;
                    }
                }
            }
        };

       edittext.setOnFocusChangeListener(focuseListener);
       edittext2.setOnFocusChangeListener(focuseListener);

        return view;
    }
    Items getItemsE(int position) {
        return (( Items) getItem(position));
    }

    public void sortByRevers() {
        Collections.reverse(objects);
        notifyDataSetChanged();
    }



}
