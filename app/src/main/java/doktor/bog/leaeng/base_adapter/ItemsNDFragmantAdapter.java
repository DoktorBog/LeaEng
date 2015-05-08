package doktor.bog.leaeng.base_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import doktor.bog.leaeng.R;

/**
 * Created by Hawk on 05.04.2015.
 */
public class ItemsNDFragmantAdapter extends BaseAdapter {

    public LayoutInflater lInflater;
    public ArrayList<ItemsNDFragment> objects;
    public Context ctx;

    public  void NDFragmantAdapter (Context context, ArrayList<ItemsNDFragment>  items) {

        ctx =  context;
        objects = items;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item_nd_fragment_adapter, parent, false);
        }

        ItemsNDFragment NDFragment = getNDFragment(position);

        ((TextView) view.findViewById(R.id.ndf_adapter_view_nameT)).setText(NDFragment.nameT);
        ((TextView) view.findViewById(R.id.ndf_adapter_view_lang)).setText(NDFragment.lang);

        return view;
    }
    ItemsNDFragment getNDFragment(int position) {
        return (( ItemsNDFragment) getItem(position));
    }
}
