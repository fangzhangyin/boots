package autoUcrop;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.appcompat.app.AppCompatActivity;

import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.File;
import java.util.UUID;

public class myCrop {

    static int  REQUEST_PICTURE_CHOOSE=1;//选择图片
    static int REQUEST_CAMERA_IMAGE=2;//照相
    public static File mPictureFile;
    public static Uri imageUri;
    private final static String CROPIMAGEROOT = Environment.getExternalStorageDirectory() + "/boots/";
    public static String filepath;
    /**
     * 从相册选择图片
     *
     * @param activity
     */
    public static void choosePhoto(Activity activity) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);//打开图库
        //REQUEST_PICTURE_CHOOSE表示请求参数，是个常量
        activity.startActivityForResult(intent, REQUEST_PICTURE_CHOOSE);
    }

    /**
     * 打开相机拍照
     *
     * @param activity
     * @return
     */
    public static void openCamera(Activity activity) {
        //private final static String CROPIMAGEROOT = Environment.getExternalStorageDirectory() + "/myAppName/";
        //当然这个目录需要自己创建，可以下载demo查看
        //mPictureFile 拍照后图片保存的文件。
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mPictureFile = new File(CROPIMAGEROOT,UUID.randomUUID() + ".jpg");
        imageUri = Uri.fromFile(mPictureFile);
        //存入图片
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        activity.startActivityForResult(intent, REQUEST_CAMERA_IMAGE);
    }

    /**
     * 裁剪图片
     *
     * @param sourceUri
     */
    public static void startUCrop(Uri sourceUri, AppCompatActivity activity) {
        UCrop.Options options = new UCrop.Options();
        //裁剪后图片保存在文件夹中
        Uri destinationUri = Uri.fromFile(new File(CROPIMAGEROOT, UUID.randomUUID()+"uCrop.jpg"));
        filepath=destinationUri.getPath();
//        destinationUri= Uri.parse(filepath);      //路径转为URI
        System.out.println(filepath);
        UCrop uCrop = UCrop.of(sourceUri, destinationUri);//第一个参数是裁剪前的uri,第二个参数是裁剪后的uri
        uCrop.withAspectRatio(1, 1);//设置裁剪框的宽高比例
        //下面参数分别是缩放,旋转,裁剪框的比例
        options.setAllowedGestures(com.yalantis.ucrop.UCropActivity.ALL, com.yalantis.ucrop.UCropActivity.NONE, com.yalantis.ucrop.UCropActivity.ALL);
        options.setToolbarTitle("移动和缩放");//设置标题栏文字
        options.setCropGridStrokeWidth(2);//设置裁剪网格线的宽度  (我这网格设置不显示，所以没效果)
        //options.setCropFrameStrokeWidth(1);//设置裁剪框的宽度
        options.setMaxScaleMultiplier(6);//设置最大缩放比例
        options.setHideBottomControls(false);//隐藏下边控制栏
        options.setShowCropGrid(false);  //设置是否显示裁剪网格
        //options.setOvalDimmedLayer(true);//设置是否为圆形裁剪框
        options.setShowCropFrame(true); //设置是否显示裁剪边框(true为方形边框)
        options.setToolbarWidgetColor(Color.parseColor("#ffffff"));//标题字的颜色以及按钮颜色
        options.setDimmedLayerColor(Color.parseColor("#AA000000"));//设置裁剪外颜色
        options.setToolbarColor(Color.parseColor("#000000")); // 设置标题栏颜色
        options.setStatusBarColor(Color.parseColor("#000000"));//设置状态栏颜色
        options.setCropGridColor(Color.parseColor("#ffffff"));//设置裁剪网格的颜色
        options.setCropFrameColor(Color.parseColor("#ffffff"));//设置裁剪框的颜色

        /*//裁剪后保存到文件中
        Uri destinationUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/myxmpp/" + "test1.jpg"));
        UCrop uCrop = UCrop.of(sourceUri, destinationUri);
        UCrop.Options options = new UCrop.Options();

        //设置toolbar颜色
        options.setToolbarColor(ActivityCompat.getColor(this, R.color.orange2));
        //设置状态栏颜色
        options.setStatusBarColor(ActivityCompat.getColor(this, R.color.orange2));*/
        //是否能调整裁剪框
        options.setFreeStyleCropEnabled(true);
        //设置裁剪图片可操作的手势
//        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
//        options.setToolbarWidgetColor(Color.parseColor("#ffffff"));//标题字的颜色以及按钮颜色
//        options.setDimmedLayerColor(Color.parseColor("#AA000000"));//设置裁剪外颜色
//        options.setToolbarColor(Color.parseColor("#000000")); // 设置标题栏颜色
//        options.setStatusBarColor(Color.parseColor("#000000"));//设置状态栏颜色
//        options.setCropGridColor(Color.parseColor("#ffffff"));//设置裁剪网格的颜色
//        options.setCropFrameColor(Color.parseColor("#ffffff"));//设置裁剪框的颜色
//        //options.setShowCropFrame(false); //设置是否显示裁剪边框(true为方形边框)
//        uCrop.withOptions(options);
        uCrop.withOptions(options);
        uCrop.start(activity);
    }

}
