package com.hzcominfo.aptdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hzcominfo.annotation.ARouter;

@ARouter(path = "/app/MainActivity")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //下面可以通过注解生成的类进行跳转了
    public void goPersonal(View view) {
        Intent intent = new Intent(MainActivity.this, PersonalActivity$$ARouter.findTargetClass("/app/PersonalActivity"));
        startActivity(intent);
    }
}
