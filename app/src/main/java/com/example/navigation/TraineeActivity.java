package com.example.navigation;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.RenderNode;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ViewFlipper;


public class TraineeActivity extends AppCompatActivity {


    private EditText editTextLoginPwd;
    boolean passwordVisible;
    ViewFlipper flipper;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainee);


//***********************************************************************************************************************************************
        //image slider code
        int imagearray[] ={R.drawable.traineeimg3, R.drawable.traineeimg2,R.drawable.traineeimg6,R.drawable.traineeimg4,R.drawable.traineeimg5};
        flipper = findViewById(R.id.flipper);

        for(int i = 0;i<imagearray.length;i++){
            showimage(imagearray[i]);
        }



//***************************************************************************************************************************************************
        //Show Hide Password using Eye Icon
        editTextLoginPwd = findViewById(R.id.editText_login_passKey);
        editTextLoginPwd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int Right = 2;
                if(event.getAction() == MotionEvent.ACTION_UP){
                    if(event.getRawX()>=editTextLoginPwd.getRight()-editTextLoginPwd.getCompoundDrawables()[Right].getBounds().width()){
                        int selection = editTextLoginPwd.getSelectionEnd();
                        if(passwordVisible){
                            //set drawable image here
                            editTextLoginPwd.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_baseline_visibility_off_24, 0);
                            //for hide password
                            editTextLoginPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordVisible = false;
                        }else{
                            //set drawable image here
                            editTextLoginPwd.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_baseline_visibility_24, 0);
                            //for show password
                            editTextLoginPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordVisible = true;

                        }
                        editTextLoginPwd.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });



//****************************************************************************************************************************
        //Button code from trainee to MainActivity
        Button button =findViewById(R.id.editTextButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TraineeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
//******************************************************************************************************************************


    }

//**************************************************************************************************************
    //image slider code
    public void showimage(int img) {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(img);

        flipper.addView(imageView);
        flipper.setFlipInterval(2000);
        flipper.setAutoStart(true);
        flipper.setInAnimation(this, android.R.anim.slide_in_left);
        flipper.setOutAnimation(this, android.R.anim.slide_out_right);
    }

//*******************************************************************************************************************8




}


