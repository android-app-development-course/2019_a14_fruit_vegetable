package com.zpffly.healthydiet8;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import top.wefor.circularanim.CircularAnim;

public class loginActivity extends AppCompatActivity {

    private Button loginButton = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton = findViewById(R.id.tv_btn_login);
        loginButton.setOnClickListener(clickListener);

    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CircularAnim.fullActivity(loginActivity.this, loginButton)
                    .colorOrImageRes(R.color.mycolor)
                    .go(new CircularAnim.OnAnimationEndListener(){
                        @Override
                        public void onAnimationEnd() {
                            Intent loginIntent = new Intent(loginActivity.this, FragmentMeActivity.class);
                            startActivity(loginIntent);
                        }
                    });
        }
    };
}
