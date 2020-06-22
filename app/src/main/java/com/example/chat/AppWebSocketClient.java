package com.example.chat;

import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class AppWebSocketClient extends WebSocketClient {
    public AppWebSocketClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Log.e("AppWebSocketClient", "onOpen()");
    }

    @Override
    public void onMessage(String message) {
        Log.e("AppWebSocketClient", "onMessage()");
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Log.e("AppWebSocketClient", "onClose()");
    }

    @Override
    public void onError(Exception ex) {
        Log.e("AppWebSocketClient", "onError()");
    }
}
