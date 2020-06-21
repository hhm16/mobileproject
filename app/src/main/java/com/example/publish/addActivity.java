package com.example.publish;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import androidx.gridlayout.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.data.GlobalData;
import com.example.login.LogActivity;
import com.example.map.MapActivity;
import com.example.method.GetRealFilePath;
import com.example.my.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

public class addActivity extends AppCompatActivity {
    private int RESULT_GET_PLACE = 0;
    private int RESULT_GET_PLACE2 = 3;
    private int PHOTO_REQUEST_GALLERY = 1;
    private int PHOTO_REQUEST_CUT = 2;
    private GridLayout gridLayout;
    private File tempFile;
    private ArrayList<Uri>imageUriList;
    private Uri imageUri;
    private int numOfImage = 0;
    private EditText startPlaceEdit;
    private EditText endPlaceEdit;
    private EditText titleEdit;
    private EditText contentEdit;
    private Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_order);
        final TextView etDate = findViewById(R.id.dateInput);
        gridLayout = findViewById(R.id.girdLayout);
        startPlaceEdit = findViewById(R.id.editText2);
        endPlaceEdit = findViewById(R.id.editText5);
        titleEdit = findViewById(R.id.titleText);
        contentEdit = findViewById(R.id.contentEdit);
        spinner = findViewById(R.id.spinner);
        imageUriList = new ArrayList<>();
        tempFile = new File(getExternalCacheDir()+"/"+System.currentTimeMillis()+".jpg");
        if(!tempFile.exists())
        {
            try {
                tempFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
        Intent sendIntent = new Intent();
        sendIntent.setClass(addActivity.this, MapActivity.class);
        if(sendIntent.resolveActivity(getPackageManager())!=null)
        {
            if(editText.getId()==R.id.editText2) {
                startActivityForResult(sendIntent, RESULT_GET_PLACE);
            }
            else if(editText.getId()==R.id.editText5)
            {
                startActivityForResult(sendIntent,RESULT_GET_PLACE2);
            }
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
        else if(requestCode == RESULT_GET_PLACE2)
        {
            if(data!=null)
            {
                EditText editText = findViewById(R.id.editText5);
                editText.setText(data.getStringExtra("str"));
            }
        }
        else if(requestCode == PHOTO_REQUEST_GALLERY)
        {
            if(data!=null)
            {
                Uri uri = data.getData();
                imageUri = uri;
                crop(uri);
                //String img_path = GetRealFilePath.getFilePathFromContentUri(uri,getContentResolver());
                //displayImg(img_path);
            }
        }
        else if(requestCode == PHOTO_REQUEST_CUT) {
            if (data != null) {
                ContentResolver contentResolver = this.getContentResolver();
                try {
                    //获取的Bitmap
                    Bitmap bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri));
                    ImageView cImageView = new ImageView(this);
                    cImageView.setImageBitmap(bitmap);
                    gridLayout.addView(cImageView);
                    numOfImage ++;
                    Button btn = findViewById(R.id.addImageBtn);
                    btn.setText("发布图片("+numOfImage+"/"+"4"+")");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
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
        if(numOfImage<4) {
            askForWritePermission();
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
        }
    }

    private void crop(Uri uri) {
        // 裁剪图片意图
        // 裁剪图片意图
        GlobalData globalData = (GlobalData)getApplication();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
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
        askForWritePermission();
        //imageUri = Uri.parse("file://"+"/"+getExternalCacheDir().getPath()+"/"+System.currentTimeMillis()+".jpg");
        intent.putExtra("return-data", false);// true:不返回uri，false：返回uri
        imageUri = Uri.fromFile(tempFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        if(intent.resolveActivity(getPackageManager())!=null)
        {
            askForWritePermission();
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
        HttpThread httpThread = new HttpThread();
        httpThread.start();
    }

    private class HttpThread extends Thread
    {
        @Override
        public void run() {
            try {
                String url;
                url = "http://106.54.118.148:8080/order/add/";
                URL mUrl = new URL(url);
                HttpURLConnection mHttpURLConnection = (HttpURLConnection) mUrl.openConnection();
                mHttpURLConnection.setRequestMethod("PUT");
                OutputStream output = mHttpURLConnection.getOutputStream();
                String title = titleEdit.getText().toString();
                String content = contentEdit.getText().toString();
                String startPos = startPlaceEdit.getText().toString();
                String endPos = endPlaceEdit.getText().toString();
                String price = (String) spinner.getSelectedItem();
                String data = "title="+title +"&content="+content+"&price="+price+"&startPos="+startPos+"&endPos="+endPos;
                output.write(data.getBytes());
                output.flush();
                output.close();
                //mHttpURLConnection.connect();
                GlobalData globalData = (GlobalData) getApplication();
                String jID = "JSESSIONID="+globalData.sessionID;
                mHttpURLConnection.setRequestProperty("Cookie",jID);
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        mHttpURLConnection.getInputStream()));
                final StringBuffer buffer = new StringBuffer();
                String str = null;
                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                }
            }
            catch (Exception e)
            {

            }
        }
    }
}
