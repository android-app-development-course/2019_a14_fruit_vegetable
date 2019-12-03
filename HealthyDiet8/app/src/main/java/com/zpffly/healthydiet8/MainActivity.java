package com.zpffly.healthydiet8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.kongzue.dialog.interfaces.OnInputDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.v3.InputDialog;
import com.kongzue.dialog.v3.TipDialog;
import com.kongzue.dialog.v3.WaitDialog;
import com.zpffly.healthydiet8.NETConnect.GetDietThread;
import com.zpffly.healthydiet8.NETConnect.GetImgThread;
import com.zpffly.healthydiet8.NETConnect.NetThread;
import com.zpffly.healthydiet8.show.Acticity_show;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

import at.markushi.ui.CircleButton;
import top.wefor.circularanim.CircularAnim;

public class MainActivity extends AppCompatActivity {

    private CircleButton startCameraButton = null;
    private FloatingActionButton choiceFromAlbumButton = null;
    private FloatingActionButton inputUrlButton = null;

    private static final int TAKE_PHOTO_PERMISSION_REQUEST_CODE = 0; // 拍照的权限处理返回码
    private static final int WRITE_SDCARD_PERMISSION_REQUEST_CODE = 1; // 读储存卡内容的权限处理返回码
    private static final int NET_REQUEST_CODE = 2;

    private static final int TAKE_PHOTO_REQUEST_CODE = 3; // 拍照返回的 requestCode
    private static final int CHOICE_FROM_ALBUM_REQUEST_CODE = 4; // 相册选取返回的 requestCode
    private static final int CROP_PHOTO_REQUEST_CODE = 5; // 裁剪图片返回的 requestCode

    private Uri photoUri = null;
    private Uri photoOutputUri = Uri.parse("file:////sdcard/image_output.jpg"); // 图片最终的输出文件的 Uri
    private String res = "";

    // 识别连接处理器
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            // 关闭等待框
            WaitDialog.dismiss();
            res = msg.getData().getString("res");
            if("error".equals(res)){
                TipDialog.show(MainActivity.this, "网络出错", TipDialog.TYPE.ERROR);
            }else {
                // todo
                // 跳转
//                CircularAnim.fullActivity(MainActivity.this, inputUrlButton)
//                        .colorOrImageRes(R.color.mycolor)
//                        .go(new CircularAnim.OnAnimationEndListener(){
//                            @Override
//                            public void onAnimationEnd() {
//                                Intent display = new Intent(MainActivity.this, Acticity_show.class);
//                                display.putExtra("title", res);
//                                display.putExtra("url", photoOutputUri.toString());
//                                startActivity(display);
//                            }
//                        });
                doGetDiet(res);
//                System.out.println(res);
            }
        }
    };




    // 获取图片连接处理器
    private Handler getImgHandle = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            String status = msg.getData().getString("msg");
            // 图片读取失败
            if (!Objects.equals("success", status)){
                TipDialog.show(MainActivity.this, status, TipDialog.TYPE.ERROR);
            }else { //图片读取成功
                byte[] bytearray = msg.getData().getByteArray("arr");
                doInput(bytearray);
            }
        }
    };


    // 获取菜谱后的Handle
    private Handler caipuHandle = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            final String diet = msg.getData().getString("diet");
            CircularAnim.fullActivity(MainActivity.this, inputUrlButton)
                        .colorOrImageRes(R.color.mycolor)
                        .go(new CircularAnim.OnAnimationEndListener(){
                            @Override
                            public void onAnimationEnd() {
                                Intent display = new Intent(MainActivity.this, Acticity_show.class);
                                display.putExtra("title", res);
                                display.putExtra("url", photoOutputUri.toString());
                                display.putExtra("caipu", diet);
                                startActivity(display);
                            }
                        });
        }
    };

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

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            // 申请读写内存卡内容的权限
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.INTERNET}, NET_REQUEST_CODE);
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
                InputDialog.show(MainActivity.this, "提示", "请输入图片uri", "确定", "取消")
                        .setOnOkButtonClickListener(new OnInputDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v, final String inputStr) {
                                WaitDialog.show(MainActivity.this, "正在识别...");
                                new Thread(new GetImgThread(inputStr, getImgHandle)).start();
                                return false;
                            }
                        });
            }
        }
    };


    /**
     * 拍照按钮处理
     */
    private void startCamera() {
        /**
         * 设置拍照得到的照片的储存目录，因为我们访问应用的缓存路径并不需要读写内存卡的申请权限，
         * 因此，这里为了方便，将拍照得到的照片存在这个缓存目录中
         */
        File file = new File(getExternalCacheDir(), "image.jpg");
        try {
            if(file.exists()) {
                file.delete();
            }
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /**
         * 因 Android 7.0 开始，不能使用 file:// 类型的 Uri 访问跨应用文件，否则报异常，
         * 因此我们这里需要使用内容提供器，FileProvider 是 ContentProvider 的一个子类，
         * 我们可以轻松的使用 FileProvider 来在不同程序之间分享数据(相对于 ContentProvider 来说)
         */
        if(Build.VERSION.SDK_INT >= 24) {
            photoUri = FileProvider.getUriForFile(this, "healthydiet8", file);
        } else {
            photoUri = Uri.fromFile(file); // Android 7.0 以前使用原来的方法来获取文件的 Uri
        }
        // 打开系统相机的 Action，等同于："android.media.action.IMAGE_CAPTURE"
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 设置拍照所得照片的输出目录
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(takePhotoIntent, TAKE_PHOTO_REQUEST_CODE);
    }

    /**
     * 从相册选取
     */
    private void choiceFromAlbum() {
        // 打开系统图库的 Action，等同于: "android.intent.action.GET_CONTENT"
        Intent choiceFromAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
        // 设置数据类型为图片类型
        choiceFromAlbumIntent.setType("image/*");
        startActivityForResult(choiceFromAlbumIntent, CHOICE_FROM_ALBUM_REQUEST_CODE);
    }

    /**
     * 裁剪图片
     */
    private void cropPhoto(Uri inputUri) {
        // 调用系统裁剪图片的 Action
        Intent cropPhotoIntent = new Intent("com.android.camera.action.CROP");
        // 设置数据Uri 和类型
        cropPhotoIntent.setDataAndType(inputUri, "image/*");
        // 授权应用读取 Uri，这一步要有，不然裁剪程序会崩溃
        cropPhotoIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        // 设置图片的最终输出目录
        cropPhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                photoOutputUri = Uri.parse("file:////sdcard/image_output.jpg"));
        startActivityForResult(cropPhotoIntent, CROP_PHOTO_REQUEST_CODE);
    }

    /**
     * 获取图片连接处理器回调函数
     *
     */
    private void doInput(byte[] imgByteArray){
//        try{
//            URL url = new URL(uri);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("GET");
//            conn.setConnectTimeout(5000);
//            conn.connect();
//            if (conn.getResponseCode() != 200){
//                TipDialog.show(MainActivity.this, "图片读取错误", TipDialog.TYPE.WARNING);
//                return;
//            }
//            InputStream inputStream = conn.getInputStream();
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            byte[] temp = new byte[1024];
//            int n = 0;
//            while ((n = inputStream.read(temp, 0, 1024)) != -1){
//                out.write(temp, 0, n);
//            }
//            byte[] byteArray = out.toByteArray();
//            Thread thread = new Thread(new NetThread(byteArray, handler));
//            thread.start();
//
//        }catch (MalformedURLException e){
//            TipDialog.show(MainActivity.this, "图片URL错误", TipDialog.TYPE.ERROR);
//        }catch (IOException e){
//            TipDialog.show(MainActivity.this, "图片读取失败", TipDialog.TYPE.WARNING);
//        }
        Thread thread = new Thread(new NetThread(imgByteArray, handler));
        thread.start();
    }


    /**
     * 在这里进行用户权限授予结果处理
     * @param requestCode 权限要求码，即我们申请权限时传入的常量
     * @param permissions 保存权限名称的 String 数组，可以同时申请一个以上的权限
     * @param grantResults 每一个申请的权限的用户处理结果数组(是否授权)
     */
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
                    if (file.exists()) {
//                        Bitmap bitmap = BitmapFactory.decodeFile(photoOutputUri.getPath());
//                        // 开始处理选中的图片
//                        int bytes = bitmap.getByteCount();
//
//                        ByteBuffer buf = ByteBuffer.allocate(bytes);
//                        bitmap.copyPixelsToBuffer(buf);
//                        byte[] byteArray = buf.array();
                        try {
                            // todo
                            WaitDialog.show(MainActivity.this, "正在识别...");
                            FileInputStream inputStream = new FileInputStream(photoOutputUri.getPath());
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            byte[] temp = new byte[1024];
                            int n = 0;
                            while ((n = inputStream.read(temp, 0, 1024)) != -1){
                                out.write(temp, 0, n);
                            }
                            byte[] byteArray = out.toByteArray();
                            Thread thread = new Thread(new NetThread(byteArray, handler));
                            thread.start();
                        }catch (IOException e){
                            Toast.makeText(this, "找不到照片", Toast.LENGTH_SHORT).show();
                            return;
                        }
//                        String result = Ingredient.ingredient(byteArray);
//                        System.out.println(result);
//                        pictureImageView.setImageBitmap(bitmap);
//                        file.delete(); // 选取完后删除照片
                    } else {
                        Toast.makeText(this, "找不到照片", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }


    /**
     * 处理获取菜谱
     */
    private void doGetDiet(String res){
        try {
            System.out.println("========doGetDiet===========");
            System.out.println(res);
            System.out.println("========doGetDiet===========");
            // 解析识别json得到菜名
            JSONObject jsonobj=new JSONObject(res);
            JSONArray jsonarry=jsonobj.optJSONArray("result");

            String resName = jsonarry.optJSONObject(0).optString("name");
            GetDietThread thread = new GetDietThread(resName, caipuHandle);
            thread.start();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
