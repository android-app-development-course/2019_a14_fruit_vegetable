package com.zpffly.healthydiet8;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import top.wefor.circularanim.CircularAnim;

public class RegisterActivity extends AppCompatActivity {

    private Button RegisterButton = null;
    private FloatingActionButton LoginButton = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        RegisterButton = findViewById(R.id.tv_btn_register);
        LoginButton = findViewById(R.id.fab_login);
        RegisterButton.setOnClickListener(clickListener);
        LoginButton.setOnClickListener(clickListener1);

    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CircularAnim.fullActivity(RegisterActivity.this, RegisterButton)
                    .colorOrImageRes(R.color.mycolor)
                    .go(new CircularAnim.OnAnimationEndListener(){
                        @Override
                        public void onAnimationEnd() {
                            Intent RegisterIntent = new Intent(RegisterActivity.this, loginActivity.class);
                            startActivity(RegisterIntent);
                        }
                    });
        }
    };
    private View.OnClickListener clickListener1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CircularAnim.fullActivity(RegisterActivity.this, LoginButton)
                    .colorOrImageRes(R.color.mycolor)
                    .go(new CircularAnim.OnAnimationEndListener(){
                        @Override
                        public void onAnimationEnd() {
                            Intent RegisterIntent = new Intent(RegisterActivity.this, loginActivity.class);
                            startActivity(RegisterIntent);
                        }
                    });
        }
    };


}
