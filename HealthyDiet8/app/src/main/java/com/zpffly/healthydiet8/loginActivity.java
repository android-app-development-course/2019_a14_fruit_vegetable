package com.zpffly.healthydiet8;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import top.wefor.circularanim.CircularAnim;

public class loginActivity extends AppCompatActivity {

    private Button loginButton = null;
    private FloatingActionButton registerButton = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton = findViewById(R.id.tv_btn_login);
        registerButton = findViewById(R.id.fab_register);
        loginButton.setOnClickListener(clickListener);
        registerButton.setOnClickListener(clickListener1);
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

    private View.OnClickListener clickListener1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CircularAnim.fullActivity(loginActivity.this, registerButton)
                    .colorOrImageRes(R.color.mycolor)
                    .go(new CircularAnim.OnAnimationEndListener(){
                        @Override
                        public void onAnimationEnd() {
                            Intent loginIntent = new Intent(loginActivity.this, RegisterActivity.class);
                            startActivity(loginIntent);
                        }
                    });

        }
    };

}
