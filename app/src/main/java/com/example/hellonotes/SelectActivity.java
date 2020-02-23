package com.example.hellonotes;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.print.PrinterId;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;

public class SelectActivity extends Activity implements View.OnClickListener {

    private ImageView s_img;

    private VideoView s_video;

    private TextView s_tv;

    private NotesDB notesDB;

    private SQLiteDatabase dbwriter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_activity);
        System.out.println(getIntent().getStringExtra(NotesDB.CONTENT));
        Button delete = findViewById(R.id.delete);
        Button back = findViewById(R.id.back);
        s_img = findViewById(R.id.s_img);
        s_video = findViewById(R.id.s_video);
        s_tv = findViewById(R.id.s_text);
        notesDB = new NotesDB(this);
        dbwriter = this.notesDB.getWritableDatabase();
        delete.setOnClickListener(this);
        back.setOnClickListener(this);
//        if (getIntent().getStringExtra(NotesDB.PATH).equals("null")){
//            s_img.setVisibility(View.GONE);
//        }else {
//            s_img.setVisibility(View.VISIBLE);
//        }
//        if (getIntent().getStringExtra(NotesDB.VIDEO).equals("null")){
//            s_video.setVisibility(View.GONE);
//        }else {
//            s_video.setVisibility(View.VISIBLE);
//        }
        if (getIntent().getStringExtra(NotesDB.PATH) == null) {
            s_img.setVisibility(View.GONE);
        } else {
            s_img.setVisibility(View.VISIBLE);
            Bitmap bitmap = BitmapFactory.decodeFile(getIntent().getStringExtra(NotesDB.PATH));
            s_img.setImageBitmap(bitmap);
        }
        if (getIntent().getStringExtra(NotesDB.VIDEO) == null) {
            s_video.setVisibility(View.GONE);
        } else {
            s_video.setVisibility(View.VISIBLE);
            s_video.setVideoURI(Uri
                    .parse(getIntent().getStringExtra(NotesDB.VIDEO)));
            s_video.start();
        }
        s_tv.setText(getIntent().getStringExtra(NotesDB.CONTENT));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.delete:
                deleteDatabase();
                finish();
                break;
            case R.id.back:
                finish();
                break;
                default:
                    break;
        }
    }

    public void deleteDatabase(){
        dbwriter.delete(NotesDB.TABLE_NAME,"_id=" + getIntent().getIntExtra(NotesDB.ID,0)
                ,null);
    }
}
