package com.example.publish;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import androidx.gridlayout.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.login.LogActivity;
import com.example.map.MapActivity;
import com.example.method.GetRealFilePath;
import com.example.my.R;

import java.io.File;
import java.util.Calendar;

public class addActivity extends AppCompatActivity {
    private int RESULT_GET_PLACE = 0;
    private int PHOTO_REQUEST_GALLERY = 1;
    private int PHOTO_REQUEST_CUT = 2;
    private GridLayout gridLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_order);
        final TextView etDate = findViewById(R.id.dateInput);
        gridLayout = findViewById(R.id.girdLayout);
        /*showDatePickDialog(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                etDate.setText(year + "-" + (month + 1) + "-" + day);
            }
        }, etDate.getText().toString());*/

    }

    public void chooseDate(View view)
    {
        final EditText editText = (EditText) view;
        showDatePickDialog(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                editText.setText(year + "-" + (month + 1) + "-" + day);
            }
        }, editText.getText().toString());
    }

    public void chooseTime(View view)
    {
        final EditText editText = (EditText) view;
        showTimeDialog(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                editText.setText(hourOfDay+":"+minute);
            }
        }, editText.getText().toString());
    }

    public void choosePlace(View view)
    {
        final EditText editText = (EditText)view;
        // Get the string indicating a location. Input is not validated; it is
        // passed to the location handler intact.
        String str = Uri.encode("清华大学");
        Uri location = Uri.parse("geo:0,0?q="+str);
        Intent sendIntent = new Intent();
        sendIntent.setClass(addActivity.this, MapActivity.class);
        if(sendIntent.resolveActivity(getPackageManager())!=null)
        {
            startActivityForResult(sendIntent,RESULT_GET_PLACE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==RESULT_GET_PLACE)
        {
            if(data!=null)
            {
                EditText editText = findViewById(R.id.editText2);
                editText.setText(data.getStringExtra("str"));
            }
        }
        else if(requestCode == PHOTO_REQUEST_GALLERY)
        {
            if(data!=null)
            {
                Uri uri = data.getData();
                crop(uri);
                //String img_path = GetRealFilePath.getFilePathFromContentUri(uri,getContentResolver());
                //displayImg(img_path);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void showTimeDialog(TimePickerDialog.OnTimeSetListener listener, String curTime)
    {
        Calendar calendar = Calendar.getInstance();
        int hour = 0, minute = 0;
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,TimePickerDialog.THEME_HOLO_LIGHT,listener,hour,minute,true);
        timePickerDialog.show();
    }

    public void showDatePickDialog(DatePickerDialog.OnDateSetListener listener, String curDate) {
        Calendar calendar = Calendar.getInstance();
        int year = 0,month = 0,day = 0;
        try {
            year =Integer.parseInt(curDate.substring(0,curDate.indexOf("-"))) ;
            month =Integer.parseInt(curDate.substring(curDate.indexOf("-")+1,curDate.lastIndexOf("-")))-1 ;
            day =Integer.parseInt(curDate.substring(curDate.lastIndexOf("-")+1,curDate.length())) ;
        } catch (Exception e) {
            e.printStackTrace();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day=calendar.get(Calendar.DAY_OF_MONTH);
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,DatePickerDialog.THEME_HOLO_LIGHT,listener, year,month , day);
        datePickerDialog.show();
    }

    public void addImage(View view)
    {
        askForWritePermission();
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        String path = GetRealFilePath.getFilePathFromContentUri(uri,getContentResolver());
        //intent.setDataAndType(Uri.parse(path), "image/*");
        //intent.setDataAndType(Uri.fromFile(new File(path)), "image/*");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 10);
        intent.putExtra("outputY", 10);
        // 图片格式
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);// true:不返回uri，false：返回uri
        if(intent.resolveActivity(getPackageManager())!=null)
        {
            startActivityForResult(intent, PHOTO_REQUEST_CUT);
        }
    }
    void displayImg(String imgPath)
    {
        if(new File(imgPath).exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
            ImageView imageView = new ImageView(this);
            imageView.setImageBitmap(bitmap);
            gridLayout.addView(imageView);
        }
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

    public void publish(View view) {

    }
}
