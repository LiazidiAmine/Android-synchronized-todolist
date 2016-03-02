package upec.com.myapplication.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by ghost Amine on 06/02/16.
 */
public class Data {

    public final static String TABLE_NAME_LIST= "List";
    public final static String COL_ID1 = "_id";
    public final static String COL_TITLE1 = "TitleList";

    public final static String TABLE_NAME_ITEM= "ToBuy";
    public final static String COL_ID2 = "_id";
    public final static String COL_TITLE2 = "Title";
    public final static String COL_TYPE = "Type";
    public final static String COL_DATE = "Date";
    public final static String COL_NOTIF = "Notification";
    public final static String COL_NOTE = "Note";
    public final static String COL_LISTID = "ID_List";

    public final static String TABLE_NAME_USER = "User";
    public final static String COL_ID_USER = "_id";
    public final static String COL_EMAIL = "email";
    public final static String COL_PASSWORD = "password";
    public final static String COL_USERNAME = "username";

    private final String DATABASE_NAME = "t.db";
    private final int DATABASE_VERSION = 1;

    public final static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "+TABLE_NAME_ITEM+"("+
            COL_ID2+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            COL_TITLE2+" TEXT NOT NULL, "+
            COL_TYPE+" TEXT, "+
            COL_DATE+" DATETIME NOT NULL DEFAULT (CURRENT_TIME), "+
            COL_NOTIF+" TEXT, "+
            COL_NOTE+" TEXT, "+
            COL_LISTID+" INTEGER, "+
            "FOREIGN KEY("+COL_LISTID+") REFERENCES "+TABLE_NAME_LIST+"("+COL_ID1+"))";
    public final static String CREATE_TABLE1 = "CREATE TABLE IF NOT EXISTS "+TABLE_NAME_LIST+"("+
            COL_ID1+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            COL_TITLE1+" TEXT NOT NULL)";

    private final static String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS "+ TABLE_NAME_USER+" ( "+
            COL_ID_USER+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            COL_EMAIL+" TEXT NOT NULL, "+
            COL_PASSWORD+" TEXT NOT NULL, "+
            COL_USERNAME+" TEXT NOT NULL )";


    public final static String DROP_TABLE1 = "DROP TABLE IF EXISTS " + TABLE_NAME_LIST;
    public final static String DROP_TABLE2 = "DROP TABLE IF EXISTS " + TABLE_NAME_ITEM;
    public final static String DROP_TABLE_USER = "DROP TABLE IF EXISTS "+TABLE_NAME_USER+";";

    private Context context;
    private SQLiteDatabase db;
    private DataHelper dataHelper;

    private class DataHelper extends SQLiteOpenHelper {

        public DataHelper(Context context){
            super(context,DATABASE_NAME , null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
            db.execSQL(CREATE_TABLE1);
            db.execSQL(CREATE_TABLE_USER);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Toast.makeText(context,"Data update", Toast.LENGTH_SHORT).show();
            db.execSQL(DROP_TABLE1);
            db.execSQL(DROP_TABLE2);
            db.execSQL(DROP_TABLE_USER);
            onCreate(db);
        }
    }

    public Data(Context context){
        this.context = context;
        dataHelper = new DataHelper(context);
    }

    public Data open(){
        db = dataHelper.getWritableDatabase();
        return this;
    }
    public void close(){
        db.close();
    }

    ////////////////////////////////////////////////////////////////////

    public void deleteAll(){
        db.execSQL("DELETE FROM "+TABLE_NAME_ITEM);
        db.execSQL("DELETE FROM "+TABLE_NAME_LIST);
    }

    public long addInformation(long id, String arg1, String arg2){
        ContentValues values = new ContentValues();
        values.put(arg2, arg1);
        return db.update(TABLE_NAME_ITEM,values,COL_ID2+" = "+id,null);
    }

    public long addItem(String title, String type, String date, String notif, String note, int listId){
        ContentValues values = new ContentValues();
        values.put(COL_TITLE2,title);
        values.put(COL_TYPE,type);
        values.put(COL_DATE,date);
        values.put(COL_NOTIF, notif);
        values.put(COL_NOTE, note);
        values.put(COL_LISTID, listId);

        return db.insert(TABLE_NAME_ITEM, null, values);
    }

    public long addList(String title){
        ContentValues values = new ContentValues();
        values.put(COL_TITLE1,title);

        return db.insert(TABLE_NAME_LIST, null, values);
    }

    public long addUser(String email, String password, String username, String date){
        ContentValues values = new ContentValues();
        values.put(COL_EMAIL, email);
        values.put(COL_PASSWORD, password);
        values.put(COL_USERNAME, username);
        //values.put(COL_DATE, date);

        return db.insert(TABLE_NAME_USER, null, values);
    }

    public boolean deleteItem(long id){
        return db.delete(TABLE_NAME_ITEM, COL_ID2+" = "+id, null)>0;
    }

    public void autoDelete(){
        //aprés une semaine ?
        String sql = "DELETE FROM "+TABLE_NAME_ITEM+" WHERE "+COL_DATE+
                "<= datetime('%Y-%m-%d %H:%M','now','- day2')";
        db.execSQL(sql);
    }

    public Cursor selectAllItem(){
        return db.query(TABLE_NAME_ITEM, new String[]{
                COL_ID2,
                COL_TITLE2,
                COL_TYPE,
                COL_DATE,
                COL_NOTIF,
                COL_NOTE,
                COL_LISTID
        },null,null,null,null,null,null);
    }

    public Cursor selectAllList(){
        //return db.rawQuery("select * from sqlite_master where tbl_name='"+TABLE_NAME1+"'",null);
        return db.query(TABLE_NAME_LIST, new String[]{
                COL_ID1,
                COL_TITLE1
        },null,null,null,null,null,null);
    }

    public Cursor selectItemsByList(int list_id){
        String sql = " SELECT * FROM "+TABLE_NAME_ITEM+" WHERE "+COL_LISTID+"="+Integer.toString(list_id)+";";

        return db.rawQuery(sql, null);
    }


    public Cursor selectItemBy(String arg){
        return db.rawQuery("SELECT * FROM " + TABLE_NAME_ITEM + " ORDER BY " + arg + " DESC", null);
    }

    public Cursor selectWhere(String arg){
        String w=null;
        if(arg.length() > 0){
             w = "'%"+arg+"%'";
        }
        String query = "SELECT * FROM "+TABLE_NAME_ITEM+" WHERE "+COL_NOTE+" LIKE "+w+" OR "+
                COL_TITLE2+" LIKE "+w+" OR "+COL_TYPE+" LIKE "+w+";";
        Cursor c = db.rawQuery(query,null);
        if(c!=null){
            return c;
        }else {
            return selectAllItem();
        }
    }

    public boolean userExist(String email){
        String sql = "SELECT "+COL_ID_USER+", "+COL_USERNAME+" FROM "+TABLE_NAME_USER+" WHERE "+COL_EMAIL+" = '"+email+"';";
        Cursor cursor = db.rawQuery(sql,null);
        if(cursor != null){
            return true;
        }
        return false;
    }

    public boolean userPassword(String pswd, String email){
        String sql = "SELECT "+COL_ID_USER+" FROM "+TABLE_NAME_USER+" WHERE "+COL_EMAIL+" = '"+email+"' " +
                " AND "+COL_PASSWORD+" = '"+pswd+"';";
        Cursor cursor = db.rawQuery(sql,null);
        if(cursor!=null){
            return true;
        }
        return false;
    }

}
