package com.example.hellonotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.slice.Slice;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button textBtn,imgBtn,videoBtn;

    private ListView lv;

    private MyAdapter adapter;

    private NotesDB notesDB;

    private SQLiteDatabase dbreader;

    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        textBtn.setOnClickListener(this);
        imgBtn.setOnClickListener(this);
        videoBtn.setOnClickListener(this);
        notesDB = new NotesDB(this);
        dbreader  = notesDB.getReadableDatabase();

    }

    private void initView() {
        lv = findViewById(R.id.list);
        textBtn = findViewById(R.id.text);
        imgBtn = findViewById(R.id.img);
        videoBtn = findViewById(R.id.video);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursor.moveToPosition(position);
//                Intent intent = new Intent(MainActivity.this,SelectActivity.class);
//                intent.putExtra(NotesDB.ID,cursor.getInt(cursor.getColumnIndex(NotesDB.ID)));
//                intent.putExtra(NotesDB.CONTENT,cursor.getString(cursor.getColumnIndex(NotesDB.CONTENT)));
//                intent.putExtra(NotesDB.TIME,cursor.getString(cursor.getColumnIndex(NotesDB.TIME)));
//                intent.putExtra(NotesDB.PATH,cursor.getString(cursor.getColumnIndex(NotesDB.PATH)));
//                intent.putExtra(NotesDB.VIDEO,cursor.getString(cursor.getColumnIndex(NotesDB.VIDEO)));
//                startActivity(intent);
                Intent i = new Intent(MainActivity.this, SelectActivity.class);
                i.putExtra(NotesDB.ID,
                        cursor.getInt(cursor.getColumnIndex(NotesDB.ID)));
                i.putExtra(NotesDB.CONTENT, cursor.getString(cursor
                        .getColumnIndex(NotesDB.CONTENT)));
                i.putExtra(NotesDB.TIME,
                        cursor.getString(cursor.getColumnIndex(NotesDB.TIME)));
                i.putExtra(NotesDB.PATH,
                        cursor.getString(cursor.getColumnIndex(NotesDB.PATH)));
                i.putExtra(NotesDB.VIDEO,
                        cursor.getString(cursor.getColumnIndex(NotesDB.VIDEO)));
                startActivity(i);

            }
        });
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, AddContent.class);
        switch(v.getId()){
            case R.id.text:
                intent.putExtra("flag","1");
                startActivity(intent);
                break;
            case R.id.img:
                intent.putExtra("flag","2");
                startActivity(intent);
                break;
            case R.id.video:
                intent.putExtra("flag","3");
                startActivity(intent);
                break;
                default:
                    break;
        }
    }

    public void  selectDB(){
        cursor = dbreader.query(NotesDB.TABLE_NAME,null,null,null,null,null,null);
        Log.d("selectDB",String.valueOf(cursor));
        Log.d("SelectedDB" ,"1111");
        adapter = new MyAdapter(this, cursor);
        lv.setAdapter(adapter);
       //Log.d("SelectedDB" ,"1111");
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectDB();
    }
}
