package com.example.chat;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.data.GlobalData;
import com.example.login.LogFragment;
import com.example.my.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    List<Word> words;
    ChatListRecycleView adapter;
    String sentenceToSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chat);
        words = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new ChatListRecycleView(this);
        RecyclerView recyclerView = findViewById(R.id.circle_edit);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void sendMessage(View view) {
        EditText chatBox = findViewById(R.id.et_content);
        sentenceToSend = chatBox.getText().toString();
        words.add(new Word(sentenceToSend));

        adapter.setWords(words);
        ChatThread chatThread = new ChatThread();
        chatThread.start();
    }

    private class ChatThread extends Thread {
        @Override
        public void run() {
            try {
                String url;
                GlobalData globalData = (GlobalData) getApplication();
                int ID = globalData.getSessionID();
                url = "http://106.54.118.148:8080/websocket/" + ID;
                URL mUrl = new URL(url);
                HttpURLConnection mHttpURLConnection = (HttpURLConnection) mUrl.openConnection();
                mHttpURLConnection.setRequestMethod("PUT");
                OutputStream output = mHttpURLConnection.getOutputStream();
                String content = "receiverID:" + String.valueOf(1) + ", content:" + sentenceToSend;
                output.write(content.getBytes());
                output.flush();
                output.close();
                //mHttpURLConnection.connect();
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        mHttpURLConnection.getInputStream()));
                final StringBuffer buffer = new StringBuffer();
                String str = null;
                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                }
                reader.close();
            } catch (Exception e) {

            }
        }
    }
}