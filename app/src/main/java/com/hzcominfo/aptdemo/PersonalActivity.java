package com.hzcominfo.aptdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hzcominfo.annotation.ARouter;

@ARouter(path = "/app/PersonalActivity")
public class PersonalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
    }

    public void goOrder(View view) {
        Intent intent = new Intent(PersonalActivity.this, OrderActivity$$ARouter.findTargetClass("/app/OrderActivity"));
        startActivity(intent);
    }
}
