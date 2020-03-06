package Selftools;

import android.app.Activity;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import java.io.File;

public class fileChoose {

    public static Uri getFileUri(File file ,Activity activity){
        Uri fileUri=null;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N)//android7.0以上获取文件的URI；安卓7.0以上读取文件不共享
        {
            fileUri=FileProvider.getUriForFile(activity, "com.example.admin.custmerviewapplication", file);
        }else{
            fileUri=Uri.fromFile(file);
        }
        return fileUri;
    }

}
