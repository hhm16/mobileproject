package com.example.chat.util;

import android.content.Context;
import android.widget.Toast;

public class Util {
    public static final String ws = "ws://echo.websocket.org";  //websocket测试地址
    public static final String wsNhy = "ws://106.54.118.148:8080/websocket/9";  //websocket测试地址

    public static void showToast(Context ctx, String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
    }
}
