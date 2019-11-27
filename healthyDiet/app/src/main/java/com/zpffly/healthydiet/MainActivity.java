package com.zpffly.healthydiet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;

import at.markushi.ui.CircleButton;

public class MainActivity extends AppCompatActivity {

    private CircleButton startCameraButton = null;
    private FloatingActionButton choiceFromAlbumButton = null;
    private FloatingActionButton inputUrlButton = null;

    private static final int TAKE_PHOTO_PERMISSION_REQUEST_CODE = 0; // 拍照的权限处理返回码
    private static final int WRITE_SDCARD_PERMISSION_REQUEST_CODE = 1; // 读储存卡内容的权限处理返回码
    private static final int READ_SDCARD_PERMISSION_REQUEST_CODE = 2;

    private static final int TAKE_PHOTO_REQUEST_CODE = 3; // 拍照返回的 requestCode
    private static final int CHOICE_FROM_ALBUM_REQUEST_CODE = 4; // 相册选取返回的 requestCode
    private static final int CROP_PHOTO_REQUEST_CODE = 5; // 裁剪图片返回的 requestCode

    private Uri photoUri = null;
    private Uri photoOutputUri = Uri.parse("file:////sdcard/image_output.jpg"); // 图片最终的输出文件的 Uri
//    "file:////sdcard/image_output.jpg"
    //调用照相机返回图片文件
    private File tempFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 初始化按钮
        startCameraButton = findViewById(R.id.startCameraButton);
        choiceFromAlbumButton = findViewById(R.id.choiceFromAlbumButton);
        inputUrlButton = findViewById(R.id.inputUrlButton);
        startCameraButton.setOnClickListener(clickListener);
        choiceFromAlbumButton.setOnClickListener(clickListener);
        inputUrlButton.setOnClickListener(clickListener);

//         判断是否有读取内存卡权限
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // 申请读写内存卡内容的权限
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_SDCARD_PERMISSION_REQUEST_CODE);
        }

    }

    // 按钮监听器
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // 调用相机拍照
            if(v == startCameraButton) {
                // 同上面的权限申请逻辑
                if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    /*
                     * 下面是对调用相机拍照权限进行申请
                     */
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.CAMERA,}, TAKE_PHOTO_PERMISSION_REQUEST_CODE);
                } else {
                    startCamera();
                }
                // 从相册获取
            } else if(v == choiceFromAlbumButton) {
                choiceFromAlbum();
            }else if (v == inputUrlButton){

            }
        }
    };
    /**
     * 拍照
     */
    private void startCamera(){

        // 拍照文件的缓存位置
        tempFile = new File(getExternalCacheDir(), System.currentTimeMillis() + ".jpg");
        // 系统相机
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 判断安卓版本,从7.0开始不能使用 file:// 类型的 Uri 访问跨应用文件
        if(Build.VERSION.SDK_INT >= 24) {
            takePhotoIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            photoUri = FileProvider.getUriForFile(this, "com.zpffly.healthydiet", tempFile);
        } else {
            photoUri = Uri.fromFile(tempFile); // Android 7.0 以前使用原来的方法来获取文件的 Uri
        }

        // 设置拍照照片输出路径
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(takePhotoIntent, TAKE_PHOTO_REQUEST_CODE);
    }

    /**
     * 从相册选择
     */
    private void choiceFromAlbum(){
        // 打开系统图库的 Action，等同于: "android.intent.action.GET_CONTENT"
        Intent choiceFromAlbumIntent = new Intent(Intent.ACTION_PICK);
        // 设置数据类型为图片类型
        choiceFromAlbumIntent.setType("image/*");
        startActivityForResult(choiceFromAlbumIntent, CHOICE_FROM_ALBUM_REQUEST_CODE);
    }

    /**
     * 输入 URL
     */
    private void inputUrl(){

    }

    /**
     * 裁剪图片
     */
    private void cropPhoto(Uri inputUri) {
//        // 调用系统裁剪图片的 Action
//        Intent cropPhotoIntent = new Intent("com.android.camera.action.CROP");
//        // 设置数据Uri 和类型
//        cropPhotoIntent.setDataAndType(inputUri, "image/*");
//        // 授权应用读取 Uri，这一步要有，不然裁剪程序会崩溃
//        cropPhotoIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        // 设置图片的最终输出目录
//        cropPhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoOutputUri);
//        startActivityForResult(cropPhotoIntent, CROP_PHOTO_REQUEST_CODE);
        UCrop.of(inputUri, photoOutputUri).withAspectRatio(16, 9).withMaxResultSize(300, 300).start(this);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            // 调用相机拍照：
            case TAKE_PHOTO_PERMISSION_REQUEST_CODE:
                // 如果用户授予权限，那么打开相机拍照
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startCamera();
                } else {
                    Toast.makeText(this, "拍照权限被拒绝", Toast.LENGTH_SHORT).show();
                }
                break;
            // 打开相册选取：
            case WRITE_SDCARD_PERMISSION_REQUEST_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(this, "读写内存卡内容权限被拒绝", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    /**
     * 通过这个 activity 启动的其他 Activity 返回的结果在这个方法进行处理
     * 我们在这里对拍照、相册选择图片、裁剪图片的返回结果进行处理
     * @param requestCode 返回码，用于确定是哪个 Activity 返回的数据
     * @param resultCode 返回结果，一般如果操作成功返回的是 RESULT_OK
     * @param data 返回对应 activity 返回的数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // 通过返回码判断是哪个应用返回的数据
            switch (requestCode) {
                // 拍照
                case TAKE_PHOTO_REQUEST_CODE:
                    cropPhoto(photoUri);
                    break;
                // 相册选择
                case CHOICE_FROM_ALBUM_REQUEST_CODE:
                    cropPhoto(data.getData());
                    break;
                // 裁剪图片
                case CROP_PHOTO_REQUEST_CODE:
                    File file = new File(photoOutputUri.getPath());
                    // 对裁剪后的文件进行处理
                    if (file.exists()) {
                        Bitmap bitmap = BitmapFactory.decodeFile(photoOutputUri.getPath());

                        // todo
//                        file.delete(); // 选取完后删除照片
                    } else {
                        Toast.makeText(this, "找不到照片", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case UCrop.RESULT_ERROR:
                    Toast.makeText(this, "裁切图片失败", Toast.LENGTH_SHORT).show();
                    break;
                case UCrop.REQUEST_CROP:
                    File file1 = new File(photoOutputUri.getPath());
                    // 对裁剪后的文件进行处理
                    if (file1.exists()) {
                        Bitmap bitmap = BitmapFactory.decodeFile(photoOutputUri.getPath());

                        // todo
//                        file.delete(); // 选取完后删除照片
                    } else {
                        Toast.makeText(this, "找不到照片", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }
}
