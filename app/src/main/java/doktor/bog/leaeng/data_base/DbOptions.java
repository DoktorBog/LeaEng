package doktor.bog.leaeng.data_base;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import doktor.bog.leaeng.base_adapter.Items;
import doktor.bog.leaeng.base_adapter.ItemsNDFragment;

public class DbOptions {

    public DataBaseHelper _sqlHelper;
    public SQLiteDatabase _sdb;
    public Context _ctx;

    public DbOptions(Context ctx,DataBaseHelper sqlHelper,SQLiteDatabase sdb){
        _sqlHelper = sqlHelper;
        _sdb = sdb;
        _ctx = ctx;
    }

    public void createTable(String nameTUser, String langv){

        String str = autoSetNameT();
       String sql_create_new_table = "CREATE TABLE "
                + str + " (" + _sqlHelper.UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + _sqlHelper.WORD + " TEXT, "
                + _sqlHelper.WORD_TRANSLATE + " TEXT, "
                + _sqlHelper.STAR + " TEXT);";
        _sdb.execSQL(sql_create_new_table);



        insertInfoAbTables(str,nameTUser,langv);
    }

    public void writeInfoInTable(String nameT,String wordToBase, String wordToBase_translate){
        String insertQuery = "INSERT INTO " + nameT
                + " (" + _sqlHelper.WORD + ", " + _sqlHelper.WORD_TRANSLATE + ", " + _sqlHelper.STAR +") VALUES ('"
                + wordToBase + "', '" + wordToBase_translate  +"', '" + "false" +
                ""  +"')";
        _sdb.execSQL(insertQuery);
    }

    public ArrayList<Items> getWord(String nameT){
        ArrayList<Items> items = new ArrayList<Items>();
        Cursor cursor = _sdb.query(nameT, new String[]{
                        _sqlHelper.UID, _sqlHelper.WORD, _sqlHelper.WORD_TRANSLATE, _sqlHelper.STAR}, null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(_sqlHelper.UID));
            String word = cursor.getString(cursor
                    .getColumnIndex(_sqlHelper.WORD));
            String worw_translate = cursor.getString(cursor
                    .getColumnIndex(_sqlHelper.WORD_TRANSLATE));
            String star = cursor.getString(cursor
                    .getColumnIndex(_sqlHelper.STAR));

            items.add(new Items(id,word,
                    worw_translate,Boolean.parseBoolean(star)));

        }
        cursor.close();
        return items;
    }

    public String getLangvTable(String nameT){
        String WHERE=_sqlHelper.TABLE_NAME+" LIKE'%"+nameT+"%'";
        Cursor cursor = _sdb.query(_sqlHelper.TABLES, new String[]{_sqlHelper.TABLE_NAME,_sqlHelper.NAME_TABLE_USER,
                        _sqlHelper.LANGV}, WHERE,
                null,
                null,
                null,
                null
        );
        String sLangv = null;
        while (cursor.moveToNext()) {

       sLangv = cursor.getString(cursor
                .getColumnIndex(_sqlHelper.LANGV));
        }
        cursor.close();
        return sLangv;

    }

    private void insertInfoAbTables(String nameTNew,String nameTUser,String langv){

        String insertQuery = "INSERT INTO " + _sqlHelper.TABLES
                + " (" + _sqlHelper.TABLE_NAME + ", " + _sqlHelper.NAME_TABLE_USER +
                ", " +_sqlHelper.LANGV+") VALUES ('"
                + nameTNew + "', '" + nameTUser + "', '" +langv+ "')";
        _sdb.execSQL(insertQuery);
    }

    private String autoSetNameT(){

        int i = -1;
        Cursor cursor = _sdb.query(_sqlHelper.TABLES, new String[]{_sqlHelper.TABLE_NAME}, null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
           i++;
        }
        cursor.close();

        return _sqlHelper.TABLE_WORDS+(i+1);
    }

    public  ArrayList<ItemsNDFragment> getLangvArray (){
        ArrayList<ItemsNDFragment> itemsND = new ArrayList<ItemsNDFragment>();
        Cursor cursor = _sdb.query(_sqlHelper.TABLES, new String[]{_sqlHelper.NAME_TABLE_USER,
                        _sqlHelper.LANGV}, null,
                null,
                null,
                null,
                null
        );
        int i = -1;
        String sLangv;String nameUser;
        while (cursor.moveToNext()) {


                sLangv = cursor.getString(cursor
                        .getColumnIndex(_sqlHelper.LANGV));
                nameUser = cursor.getString(cursor
                        .getColumnIndex(_sqlHelper.NAME_TABLE_USER));

            itemsND.add(new ItemsNDFragment(nameUser,sLangv));

        }
        cursor.close();

return itemsND;
    }

    public String getRealName(String nameT){
        String name =null;
        String WHERE=_sqlHelper.NAME_TABLE_USER+" LIKE'%"+nameT+"%'";
        Cursor cursor = _sdb.query(_sqlHelper.TABLES, new String[]{_sqlHelper.TABLE_NAME,_sqlHelper.NAME_TABLE_USER,
                        _sqlHelper.LANGV}, WHERE,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
               name = cursor.getString(cursor
                        .getColumnIndex(_sqlHelper.TABLE_NAME));
        }
        cursor.close();

        return name;
    }
    public void writeStar(String nameT,int id,String value){
        String sql_update = "UPDATE "
                + nameT + " SET " + _sqlHelper.STAR + "='" + value + "'" +
                " WHERE " + _sqlHelper.UID + "='" + id + "';";
        _sdb.execSQL(sql_update);
    }

}
