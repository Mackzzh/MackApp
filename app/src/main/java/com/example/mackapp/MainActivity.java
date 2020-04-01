package com.example.mackapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    FloatingActionButton floatingActionButton;
    private ListView listView;
    private NoteDatabase dbHelper;
    private Context context = this;
    private NoteAdapter adapter;
    private List<Note> noteList = new ArrayList<>();
    private Toolbar myToolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        floatingActionButton = findViewById(R.id.floatingActionButton2);
        myToolbar = findViewById(R.id.myToolbar);

        listView = findViewById(R.id.lv);
        adapter  = new NoteAdapter(getApplicationContext(),noteList);
        listView.setAdapter(adapter);
        refreshListView();

        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//设置toolbar取代actionbar

        myToolbar.setNavigationIcon(R.drawable.ic_menu_black_24dp);

        listView.setOnItemClickListener(this); // 监听点击listview

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,EditActivity.class);
                intent.putExtra("mode",4);
                startActivityForResult(intent,0);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int retuenMode;
        long note_id;
        retuenMode = data.getExtras().getInt("mode",-1);
        note_id = data.getExtras().getLong("id",0);

        if (retuenMode == 1){ //更新笔记
            String content = data.getExtras().getString("content");
            String time = data.getExtras().getString("time");
            int tag = data.getExtras().getInt("tag",1);
            Note newNote = new Note(content,time,tag);
            newNote.setId(note_id);
            CRUD op = new CRUD(context);
            op.open();
            op.updataNote(newNote);
            //achievement.editNote(op.getNote(note_id).getContent(),content);
            op.close();
        }else if (retuenMode == 0){  //创建新的笔记
            String content = data.getExtras().getString("content");
            String time = data.getExtras().getString("time");
            int tag = data.getExtras().getInt("tag",1);
            Note newNote = new Note(content,time,tag);
            CRUD op = new CRUD(context);
            op.open();
            op.addNote(newNote);
            //achievement.editNote(op.getNote(note_id).getContent(),content);
            op.close();
        }else if (retuenMode == 2){ //删除笔记
            Note cruNode = new Note();
            cruNode.setId(note_id);
            CRUD op = new CRUD(context);
            op.open();
            op.removeNote(cruNode);
            op.close();
        }
        else {

        }
        refreshListView();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);

        MenuItem search = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setQueryHint("Search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_clear:
                new AlertDialog.Builder(MainActivity.this).setMessage("确定要删除全部吗？").setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper = new NoteDatabase(context);
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        db.delete("notes",null,null);
                        db.execSQL("update sqlite_sequence set seq = 0 where name = 'notes'");
                        refreshListView();
                    }
                }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void refreshListView(){
        //set adapter
        CRUD op = new CRUD(context);
        op.open();
        if (noteList.size()>0){
            noteList.clear();
        }
        noteList.addAll(op.getAllNote());
        op.close();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.lv:
                Note curNote = (Note)parent.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra("content",curNote.getContent());
                intent.putExtra("time",curNote.getTime());
                intent.putExtra("id",curNote.getId());
                intent.putExtra("tag",curNote.getTag());
                intent.putExtra("mode",3);  //编辑模式
                startActivityForResult(intent,1);
                break;
        }


    }
}
