package com.hzcominfo.aptdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hzcominfo.annotation.ARouter;

@ARouter(path = "/app/OrderActivity")
public class OrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
    }

    public void goMain(View view) {
        Intent intent = new Intent(OrderActivity.this, MainActivity$$ARouter.findTargetClass("/app/MainActivity"));
        startActivity(intent);
    }
}
