package upec.com.myapplication;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.Date;

import upec.com.myapplication.DataBase.Data;

/**
 * Created by ghost Amine on 06/02/16.
 */
public class MyList extends ListActivity {

    private Data db;
    private Button button_AddItem;
    private EditText editText_AddItem;
    private long idItemContextMenu;
    private int list_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list);
        getListView().setOnCreateContextMenuListener(this);
        button_AddItem =(Button) (findViewById(R.id.button_Additem));
        editText_AddItem =(EditText) (findViewById(R.id.editText_AddItem));

        getListId();

        db = new Data(this);
        db.open();

        refreshData();

        button_AddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem(editText_AddItem);
                refreshData();
            }
        });

        Button button_retour =(Button) (findViewById(R.id.return_button));

        button_retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyList.this, AccueilActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy(){
        db.close();
        super.onDestroy();
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo){
        super.onCreateContextMenu(contextMenu,view,contextMenuInfo);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)contextMenuInfo;
        idItemContextMenu = info.id;

        contextMenu.add(0,100,0,"Edit");
        contextMenu.add(0,200,0,"Delete");
        contextMenu.add(0, 300, 0, "Add notification");
        contextMenu.add(0, 400, 0, "Add note");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        switch (item.getItemId()){
            case 100:{
                LayoutInflater layoutInflater = LayoutInflater.from(this);
                final View adb_edit = layoutInflater.inflate(R.layout.adb_edit, null);

                AlertDialog.Builder adb = new AlertDialog.Builder(this);
                adb.setView(adb_edit);
                adb.setTitle("Edit");
                adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText adb_EditT = (EditText)adb_edit.findViewById(R.id.adb_EditText);
                        db.deleteItem(idItemContextMenu);
                        addItem(adb_EditT);
                    }
                });

                adb.show();

                break;
            }
            case 200:{

                db.deleteItem(idItemContextMenu);
                refreshData();

                break;
            }
            case 300:{

                break;
            }
            case 400:{
                LayoutInflater layoutInflater = LayoutInflater.from(this);
                final View adb_note = layoutInflater.inflate(R.layout.adb_note,null);

                AlertDialog.Builder adb = new AlertDialog.Builder(this);
                adb.setView(adb_note);
                adb.setTitle("Add note");
                adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText adb_noteEditT = (EditText)adb_note.findViewById(R.id.adb_noteEditT);
                        db.addInformation(idItemContextMenu,adb_noteEditT.getText().toString(),Data.COL_NOTE);
                        refreshData();
                    }
                });
                adb.show();
                break;
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.selectByDate :{

                return true;
            }
            case R.id.selectWhere :{

                return true;
            }
            case R.id.help :{

                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id){
        super.onListItemClick(listView,view,position,id);
        Cursor cursor = (Cursor)listView.getAdapter().getItem(position);

        String title = cursor.getString(cursor.getColumnIndex(Data.COL_TITLE2));
        String type = cursor.getString(cursor.getColumnIndex(Data.COL_TYPE));
        String date = cursor.getString(cursor.getColumnIndex(Data.COL_DATE));
        String notif = cursor.getString(cursor.getColumnIndex(Data.COL_NOTIF));
        String note = cursor.getString(cursor.getColumnIndex(Data.COL_NOTE));

        displayItemOnToast(title, type, date, note);
    }

    private void dataBind(Cursor c){
        startManagingCursor(c);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.item_listview, c, new String[]{
                Data.COL_TITLE2, Data.COL_TYPE, Data.COL_DATE, Data.COL_NOTIF, Data.COL_NOTIF},
                new int[]{R.id.title, R.id.type,R.id.date,R.id.note
        });
        setListAdapter(adapter);
    }

    private void addItem(EditText editText){
        String item = editText.getText().toString();
        String typeItem = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        if(item.length()==0){
            Toast.makeText(MyList.this, "Try again", Toast.LENGTH_SHORT).show();
        }else{
            //c'est comme le # de twitter, le # permettra de regrouper des courses dans notre liste selon leur type, cool non ? lol
            if(item.indexOf("#") > 0){
                int index = item.indexOf("#");
                typeItem = item.substring(index);
            }
            Date presentDate = new Date();
            String presentDateAsString = dateFormat.format(presentDate);
            db.addItem(item,typeItem,presentDateAsString,null,null,list_id);
            refreshData();
            editText.setText(null);
        }
    }

    private void refreshData(){
        Cursor c = db.selectItemsByList(list_id);
        dataBind(c);
    }

    private void displayItemOnToast(String title, String type, String date, String note){
        TextView text, text1, text2, text3;
        LayoutInflater layoutInflater = getLayoutInflater();
        View toastLayout = layoutInflater.inflate(R.layout.toast_custom,(ViewGroup)findViewById(R.id.toastcustom));

        text = (TextView)toastLayout.findViewById(R.id.textView1);
        text.setText("Item "+title);
        text1 = (TextView)toastLayout.findViewById(R.id.textView2);
        text1.setText("Type "+type);
        text2 = (TextView)toastLayout.findViewById(R.id.textView3);
        text2.setText("Date "+date);
        text3 = (TextView)toastLayout.findViewById(R.id.textView4);
        text3.setText("Note "+note);

        Toast toast = new Toast(getApplicationContext());
        toast.setView(toastLayout);
        toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    private void getListId(){
        int listId = 0;
        Intent intent = getIntent();
        if(intent != null){
            listId = intent.getIntExtra("id",0);
        }
        this.list_id = listId;
    }
}
