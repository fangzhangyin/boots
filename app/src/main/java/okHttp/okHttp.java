package okHttp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.boots.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import Toast.ToastUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class okHttp {

    //get请求
    public void getmethods(final Activity activity, final TextView textView) {
        OkHttpClient client = new OkHttpClient();
        //构造Request对象
        //采用建造者模式，链式调用指明进行Get请求,传入Get的请求地址
        Request request = new Request.Builder().get().url("https://fzy.xiaomy.net/boots/hello").build();
        Call call = client.newCall(request);
        //异步调用并设置回调函数
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showToast(activity, "Get 失败");
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String responseStr = response.body().string();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(responseStr);
                    }
                });
            }
        });
    }

    //传统post表单形式
    public void postParameter(final Activity activity, final TextView textView) {
        OkHttpClient client = new OkHttpClient();
        //构建FormBody，传入要提交的参数
        FormBody formBody = new FormBody
                .Builder()
                .add("name", "fzy")
                .add("password", "257041")
                .build();
        final Request request = new Request.Builder()
                .url("https://fzy.xiaomy.net/boots/useradd")
                .post(formBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showToast(activity, "Post Parameter 失败");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseStr = response.body().string();
                ToastUtil.showToast(activity, "Code：" + String.valueOf(response.code()));
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(responseStr);
                    }
                });
            }
        });
    }

    //文件上传
    public void postFile(final Activity activity, final TextView textView,String filepath) {
        OkHttpClient client = new OkHttpClient();
        final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");//文件形式
        File file = new File(filepath);
        String filename= file.getName()+UUID.randomUUID();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(),
                        RequestBody.create(MediaType.parse("multipart/form-data"), file))
                .addFormDataPart("username","fzy")
                .build();
        Request request = new Request.Builder()
                .url("https://fzy.xiaomy.net/Springmvc_war/upfile")
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showToast(activity, "Post File 失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseStr = response.body().string();
                ToastUtil.showToast(activity, "Code：" + String.valueOf(response.code()));
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(responseStr);
                    }
                });
            }
        });
    }

    //文件下载
    public void downImage(Activity activity, ImageView imageView) {
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request
                .Builder()
                .get()
                .url("https://fzy.xiaomy.net/JT/cover/admin2020-02-10%2010_45_55lxtp.png")
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showToast(activity, "下载图片失败");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream inputStream = response.body().byteStream();
                //将图片显示到ImageView中
                final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(bitmap);
                    }
                });
//                //将图片保存到本地存储卡中
                File file = new File(Environment.getExternalStorageDirectory(), "image.png");
                System.out.println(file.getParent());
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                byte[] temp = new byte[128];
                int length;
                while ((length = inputStream.read(temp)) != -1) {
                    fileOutputStream.write(temp, 0, length);
                }
                fileOutputStream.flush();
                fileOutputStream.close();
                inputStream.close();
            }
        });
    }

    //
}
