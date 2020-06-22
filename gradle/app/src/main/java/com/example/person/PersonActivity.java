package com.example.person;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.data.GlobalData;
import com.example.method.GetRealFilePath;
import com.example.my.R;
import com.example.view.CircleImageView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class PersonActivity extends AppCompatActivity {
    private CircleImageView cImageView;
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;
    private static final String IMAGE_FILE_NAME = "temp_head_image.jpg";
    private File tempFile;
    private String cropFileName;
    private Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        cImageView = findViewById(R.id.circleimageview_personal_photo);
        cImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        tempFile = new File(getExternalCacheDir()+"/"+System.currentTimeMillis()+".jpg");
        if(!tempFile.exists())
        {
            try {
                tempFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //OrderBox orderBox = new OrderBox(MainActivity.this);
        //container.addView(orderBox,4);
        //OrderBox orderBox1 = new OrderBox(MainActivity.this);
        //container.addView(orderBox1,5);

    }

    void chooseImage()
    {
        askForWritePermission();
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    void askForWritePermission()
    {
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions)
            {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                }
            }
        }
    }
    void askForReadPermission()
    {
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions)
            {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                }
            }
        }
    }

    void displayImg(String imgPath)
    {
        if(new File(imgPath).exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
            cImageView.setImageBitmap(bitmap);
        }
    }


    private void crop(Uri uri) {
        // 裁剪图片意图
        GlobalData globalData = (GlobalData)getApplication();
        Intent intent = new Intent("com.android.camera.action.CROP");
        //intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, "image/*");
        //intent.setDataAndType(Uri.parse(path), "image/*");
        //intent.setDataAndType(Uri.fromFile(new File(path)), "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        // 图片格式
        //intent.putExtra("output",Uri.fromFile(loadingFile));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        //imageUri = Uri.parse("file://"+"/"+getExternalCacheDir().getPath()+"/"+System.currentTimeMillis()+".jpg");
        intent.putExtra("return-data", false);// true:不返回uri，false：返回uri
        imageUri = Uri.fromFile(tempFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        if(intent.resolveActivity(getPackageManager())!=null)
        {
            startActivityForResult(intent, PHOTO_REQUEST_CUT);
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_CANCELED) {

            return;
        }

        if (requestCode == PHOTO_REQUEST_GALLERY) {

            if (data != null) {

                Uri uri = data.getData();
                //String path = GetRealFilePath.getFilePathFromContentUri(uri,getContentResolver());
                //displayImg(path);
                try {
                    crop(uri);
                } catch (Exception e) {
                    // TODO: handle exception
                }

            }

        } else if (requestCode == PHOTO_REQUEST_CUT) {
            if (data != null) {
                ContentResolver contentResolver = this.getContentResolver();
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri));
                    cImageView.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}
