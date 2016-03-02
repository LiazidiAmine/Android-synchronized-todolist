package upec.com.myapplication;

import android.app.AlertDialog;
import android.app.ExpandableListActivity;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import upec.com.myapplication.DataBase.Data;

/**
 * Created by marc on 24/02/2016.
 */
public class AccueilActivity extends ListActivity {

    private Data db;
    private int choix_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);
        db = new Data(this);
        db.open();
        refreshData();

        Button launchList = (Button) findViewById(R.id.load_list_button);
        launchList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccueilActivity.this, MyList.class);
                intent.putExtra("id", choix_id);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.accueil_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.add_liste :{
                ajoutListe();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        choix_id = position;
        for(int i=0;i<listView.getChildCount();i++) {
            listView.getChildAt(i).setBackgroundColor(Color.WHITE);
        }
        listView.getChildAt(position).setBackgroundColor(Color.GRAY);
    }

    private void dataBind(Cursor c){
        startManagingCursor(c);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.item_listview2,
                c,
                new String[]{Data.COL_TITLE1},
                new int[]{R.id.title1}
        );
        setListAdapter(adapter);
    }

    private void ajoutListe() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View adb_add_liste = layoutInflater.inflate(R.layout.adb_add_liste,null);

        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setView(adb_add_liste);
        adb.setTitle("Add List");
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText adb_listET = (EditText)adb_add_liste.findViewById(R.id.adb_listET);
                //db.addInformation(idItemContextMenu,adb_noteEditT.getText().toString(),Data.COL_TITLE1);
                db.addList(adb_listET.getText().toString());
                refreshData();
            }
        });
        adb.show();
        refreshData();
    }

    private void refreshData(){
        Cursor c = db.selectAllList();
        dataBind(c);
    }

    @Override
    protected void onDestroy(){
        db.close();
        super.onDestroy();
    }
}
