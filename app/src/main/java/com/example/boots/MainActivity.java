package com.example.boots;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import me.rosuh.filepicker.config.FilePickerManager;
import okHttp.okHttp;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView=(TextView) findViewById (R.id.t1);
        ImageView imageView=(ImageView)findViewById(R.id.image);
        Button button=(Button)findViewById(R.id.button);
        Button bnt1=(Button)findViewById(R.id.btn1);
        Button btn2=(Button)findViewById(R.id.bnt2);
        button.setOnClickListener(this);
        bnt1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        okHttp okHttp=new okHttp();

        //文件选择器





       okHttp.postFile(MainActivity.this,textView,"");
//       okHttp.getmethods(MainActivity.this,textView);
//        okHttp.postParameter(MainActivity.this,textView);
//        okHttp.downImage(MainActivity.this,imageView);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<String>list;
        switch (requestCode){
            case FilePickerManager.REQUEST_CODE:
                if(resultCode== Activity.RESULT_OK){
                    list=FilePickerManager.INSTANCE.obtainData();
                }else{
                    Toast.makeText(MainActivity.this,"文件选择失败",Toast.LENGTH_SHORT).show();
                }
            default:
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                FilePickerManager.INSTANCE.from(MainActivity.this).forResult(FilePickerManager.REQUEST_CODE);
                break;
            case R.id.btn1:
                break;
            case R.id.bnt2:
                break;
                default:
        }
    }
}
