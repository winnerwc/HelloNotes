package com.example.hellonotes;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddContent extends Activity implements View.OnClickListener {

    private SQLiteDatabase dbwriter;

    private NotesDB notesDB;

    private Button saveBth,deleteBtn;

    private EditText editText;

    private String val;

    private ImageView c_img;

    private VideoView c_video;

    private File phoneFile,videoFile;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity);
        initPhotoError();
        val = getIntent().getStringExtra("flag");
        saveBth = findViewById(R.id.save);
        deleteBtn = findViewById(R.id.delete);
        editText = findViewById(R.id.ettext);
        c_img =  findViewById(R.id.c_img);
        c_video = findViewById(R.id.c_video);
        notesDB = new NotesDB(this);
        dbwriter= notesDB.getWritableDatabase();
        saveBth.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
        initView();
    }

    public void initView(){
        if (val.equals("1")){//文字
            c_img.setVisibility(View.GONE);
            c_video.setVisibility(View.GONE);
        }else if(val.equals("2")){
            c_img.setVisibility(View.VISIBLE);
            c_video.setVisibility(View.GONE);
            Intent iimg = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            phoneFile = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+ "/" + getTime() +".jpg");
            iimg.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(phoneFile));
            if (ActivityCompat.checkSelfPermission(AddContent.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(AddContent.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            }
            startActivityForResult(iimg,1);
        }else if (val.equals("3")){
            c_img.setVisibility(View.GONE);
            c_video.setVisibility(View.VISIBLE);
            Intent video = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            videoFile = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+ "/" + getTime() +".jpg");
            video.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(videoFile));
            if (ActivityCompat.checkSelfPermission(AddContent.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(AddContent.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            }
            startActivityForResult(video,2);
        }
    }
    public void addDB(){
        ContentValues cv = new ContentValues();
        cv.put(NotesDB.CONTENT,editText.getText().toString());
        cv.put(NotesDB.TIME,getTime());
        cv.put(NotesDB.PATH,phoneFile + "");
        cv.put(NotesDB.VIDEO,videoFile+"");
        Log.d("addDB" ,String.valueOf(cv));
        long result = dbwriter.insert(NotesDB.TABLE_NAME,null,cv);
        Log.d("addDb",String.valueOf(result));
    }

    public String getTime(){
        SimpleDateFormat formate = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date cur = new Date();
        String str = formate.format(cur);
        Log.d("getTime" , str);
        return str;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.save:
                addDB();
                finish();
                break;
            case R.id.delete:
                finish();
                break;
                default:
                    break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("result",String.valueOf(resultCode));
        if (requestCode == 1){
            Log.d("result","1111");
            Bitmap bitmap = BitmapFactory.decodeFile(phoneFile.getAbsolutePath());
            c_img.setImageBitmap(bitmap);
        }
        if (requestCode == 2){
            c_video.setVideoURI(Uri.fromFile(videoFile));
            c_video.start();
        }
    }
    private void initPhotoError(){
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }
    }

}
