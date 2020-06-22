package com.example.chat;

import androidx.annotation.NonNull;

public class Word {
    @NonNull
    private String mWord;

    public Word(@NonNull String word) {
        this.mWord = word;
    }

    public String getWord() {
        return this.mWord;
    }
}
