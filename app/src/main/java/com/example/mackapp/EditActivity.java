package com.example.mackapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;

public class EditActivity extends AppCompatActivity {

    EditText editText ;
    private String old_content = "";
    private String old_time = "";
    private int old_tag = 1;
    private long id = 0;
    private int openMode = 0;
    private int tag = 1;
    public Intent intent = new Intent();  //message to be sent
    private boolean tagChange = false;
    //private String content;
    //private String time;
    Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_activity);
        myToolbar = findViewById(R.id.myToolbar2);
        editText = findViewById(R.id.editText);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//设置toolbar取代actionbar

        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoSetMessage();
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        Intent getIntent = getIntent();
         openMode = getIntent.getIntExtra("mode",0);
        if (openMode == 3){
            id = getIntent.getLongExtra("id",0);
            old_content = getIntent.getStringExtra("content");
            old_time = getIntent.getStringExtra("time");
            old_tag = getIntent.getIntExtra("tag",1);
            editText.setText(old_content);
            editText.setSelection(old_content.length());

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override // 点击删除键
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete:
                new AlertDialog.Builder(EditActivity.this).setMessage("确定删除吗？").setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (openMode == 4){
                            intent.putExtra("mode",-1); //新创建的笔记，不管
                            setResult(RESULT_OK,intent);
                        }
                        else{ //删除存在笔记
                            intent.putExtra("mode",2);
                            intent.putExtra("id",id);
                            setResult(RESULT_OK,intent);
                        }
                        finish();
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

    public boolean onKeyDown(int keyCode, KeyEvent Event){

        if (keyCode == Event.KEYCODE_HOME){
            return true;
        }
        else if (keyCode == Event.KEYCODE_BACK){
            autoSetMessage();
            setResult(RESULT_OK,intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode,Event);
    }

    private void autoSetMessage() {
        if (openMode == 4){
            if (editText.getText().toString().length() == 0){
                intent.putExtra("mode",-1); //如果什么都没有写入就什么都不干
            }else {
                intent.putExtra("mode",0);
                intent.putExtra("content",editText.getText().toString());
                intent.putExtra("time",dataToStr());
                intent.putExtra("tag",tag);
            }
        }else{
            if (editText.getText().toString().equals(old_content) && !tagChange){
                intent.putExtra("mode",-1); //如果没修改则什么都不干
            }else {
                intent.putExtra("mode",1); //修改了
                intent.putExtra("content",editText.getText().toString());
                intent.putExtra("time",dataToStr());
                intent.putExtra("tag",tag);
                intent.putExtra("id",id);
            }
        }
    }

    public String dataToStr(){
        Date data = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
        return simpleDateFormat.format(data);
    }
}
