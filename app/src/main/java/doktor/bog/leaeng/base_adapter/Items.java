package doktor.bog.leaeng.base_adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * Created by Grechanuyk B.O on 26.01.2015.
 */

public class Items {

    public  int id;
    public String name;
    public String name_translate;
    Button bl1;
    Button bl2;
    public boolean isRatedUp=false;

    public Items(int Id,String _describe, String _name_translate,boolean rate) {
        name = _describe;
        name_translate=_name_translate;
        id=Id;
        isRatedUp = rate;
    }


    public boolean isRatedUp(){
        return this.isRatedUp;
    }
    public void setRatedUp(boolean israted){
        this.isRatedUp=israted;
    }

}