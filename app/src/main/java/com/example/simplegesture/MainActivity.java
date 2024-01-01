package com.example.simplegesture;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.BitmapFactory;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BitmapTransformView view = new BitmapTransformView(this);
        view.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.turtle));
        setContentView(view);
    }
}