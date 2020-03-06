package com.example.boots;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.UUID;

import autoUcrop.myCrop;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.internal.Utils;
import me.rosuh.filepicker.config.FilePickerManager;
import okHttp.okHttp;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.image)
    ImageView imageView;
    okHttp okHttp=new okHttp();
    @BindView(R.id.t1)
    TextView textView;
    @BindView(R.id.button)
    Button button;
    @BindView(R.id.btn1)
    Button btn1;
    @BindView(R.id.bnt2)
    Button btn2;
    @BindView(R.id.btn3)
    Button btn3;
    @BindView(R.id.btn4)
    Button btn4;
    @BindView(R.id.btn5)
    Button btn5;
    @BindView(R.id.bnt6)
    Button btn6;
    @BindView(R.id.btn7)
    Button btn7;
    static String path;
    static Uri imageUri=null;
    static  File file=null;

//    使用指定的Header和Footer
//    方法一：全局设置（优先级最低，会被下面两种方法取代)
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);//注意位置 否则报错

        //安卓7以上版本，打开手机摄像问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

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
                    for(String l:list){
                        l=textView.getText()+l;
                        textView.setText(l);
                    }
                    if(list.size()==1){
                        for(String l:list){
                            path=l;
                            String fux=path.substring(path.lastIndexOf(".")+1);
                            if("jpg".equals(fux)||"png".equals(fux)||"jpeg".equals(fux)){
                                imageView.setImageURI(Uri.fromFile(new File(path)));
                            }
                        }
                    }
                }else{
                    Toast.makeText(MainActivity.this,"文件选择失败",Toast.LENGTH_SHORT).show();
                }
                break;
            //相册
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri sourceUri = data.getData();
                    myCrop.startUCrop(sourceUri,MainActivity.this);
                } else {
                    Toast.makeText(this, "选图失败！", Toast.LENGTH_SHORT).show();
                }
                break;
            //照相
            case 2:
                if (resultCode == RESULT_OK) {
                    file=myCrop.mPictureFile;
                    imageUri=Selftools.fileChoose.getFileUri(file,MainActivity.this);
                    myCrop.startUCrop(imageUri,MainActivity.this);
                    } else {
                        Toast.makeText(this, "拍照失败！", Toast.LENGTH_SHORT).show();
                }
                break;
            //裁剪后的效果
            case UCrop.REQUEST_CROP:
                if (resultCode == RESULT_OK) {
                    Uri resultUri = UCrop.getOutput(data);
                    System.out.println(resultUri.toString());
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(resultUri));
                        imageView.setImageBitmap(bitmap);
                        if(file!=null&&file.exists()){
                            file.delete();//删除拍照文件，防止消耗手机内存
                            file=null;
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            //错误裁剪的结果
            case UCrop.RESULT_ERROR:
                if (resultCode == RESULT_OK) {
                    final Throwable cropError = UCrop.getError(data);
                    Toast.makeText(this, "裁剪失败！", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    @OnClick({R.id.button,R.id.btn1,R.id.bnt2,R.id.btn3,R.id.btn4,R.id.btn5,R.id.bnt6,R.id.btn7})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                //文件选择器
                FilePickerManager.INSTANCE.from(MainActivity.this).forResult(FilePickerManager.REQUEST_CODE);
                break;
            case R.id.btn1:
                if(imageView.getDrawable()==null){
                    Toast.makeText(MainActivity.this,"没有选择图片",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(path==null){
                        path=myCrop.filepath;
                    }
                    okHttp.postFile(MainActivity.this,textView,path);
                }
                break;
            case R.id.bnt2:
                okHttp.downImage(MainActivity.this,imageView);
                break;
            case R.id.btn3:
               myCrop.choosePhoto(this);
                break;
            case R.id.btn4:
                myCrop.openCamera(MainActivity.this);
                break;
            case R.id.btn5:
                okHttp.downfile(MainActivity.this,textView);
                break;
            case R.id.bnt6:
                Intent intent=new Intent(MainActivity.this,SmartRefresh.class);
                startActivity(intent);
                break;
            case R.id.btn7:
                Intent intent1=new Intent(MainActivity.this,MPandroidchart.class);
                startActivity(intent1);
                break;
                default:
        }
    }
}
