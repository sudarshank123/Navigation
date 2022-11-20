package com.example.navigation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;


public class OwnerActivity<textMobile> extends AppCompatActivity {


    TextView textView;
    private EditText editTextLoginEmail, editTextLoginPwd;
    private ProgressBar progressBar;
    private FirebaseAuth authProfile;
    private static final String TAG = "OwnerActivity";
    boolean passwordVisible;
    ViewFlipper flipper;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner);

//***************************************************************************************************************************
        //image slider code
        int imagearray[] ={R.drawable.traineeimg6, R.drawable.traineeimg2,R.drawable.traineeimg3,R.drawable.traineeimg4,R.drawable.traineeimg5};
        flipper = findViewById(R.id.flipper);

        for(int i = 0;i<imagearray.length;i++){
            showimage(imagearray[i]);
        }


        //*********************************************************************Create account TextButton
        textView = findViewById(R.id.createAcc);
        String text = "Create new Account?" ;
        SpannableString ss = new SpannableString(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent intent = new Intent(OwnerActivity.this, CreateActivity.class);
                startActivity(intent);
          //      finish();
            }
        };
        ss.setSpan(clickableSpan,0,19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());




        //*******************************************************************Forgot Password TextButton
        textView = findViewById(R.id.forgot_pwd);
        String text2 = "Forgot Password?";
        SpannableString ss1 = new SpannableString(text2);
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent intent = new Intent(OwnerActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
             //   finish();
            }
        };
        ss1.setSpan(clickableSpan1,0,16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(ss1);
        textView.setMovementMethod(LinkMovementMethod.getInstance());



        //*********************************************************************************************

        editTextLoginEmail = findViewById(R.id.editText_login_Email);
        editTextLoginPwd = findViewById(R.id.editText_login_pwd);
        progressBar = findViewById(R.id.progressBar);

        authProfile = FirebaseAuth.getInstance();

        //**************************************************************************************************

        //Show Hide Password using Eye Icon
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


        //******************************************************************************************************

        //Login User
        Button buttonLogin = findViewById(R.id.button_login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textEmail = editTextLoginEmail.getText().toString();
                String textPwd = editTextLoginPwd.getText().toString();


                if(TextUtils.isEmpty(textEmail)){
                    Toast.makeText(OwnerActivity.this, "Please enter your Email", Toast.LENGTH_SHORT).show();
                    editTextLoginEmail.setError("Email is required");
                    editTextLoginEmail.requestFocus();
                }else if(!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()) {
                    Toast.makeText(OwnerActivity.this, "Please re-enter your Email", Toast.LENGTH_SHORT).show();
                    editTextLoginEmail.setError("Valid Email is required");
                    editTextLoginEmail.requestFocus();
                }else if(TextUtils.isEmpty(textPwd)){
                    Toast.makeText(OwnerActivity.this, "Please enter your Password", Toast.LENGTH_SHORT).show();
                    editTextLoginPwd.setError("Password is Required");
                    editTextLoginPwd.requestFocus();
                }else{
                    progressBar.setVisibility(View.VISIBLE);
                    loginOwner(textEmail, textPwd);
                }
            }
        });

    }
//***********************************************************************************************************************************
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

    //End of OnCreate Method*****************************************************************************************************

    private void loginOwner(String email, String pwd) {
        authProfile.signInWithEmailAndPassword(email, pwd).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){


                    //Get instance of the current User
                    FirebaseUser firebaseUser = authProfile.getCurrentUser();

                    //Check if email is verified before user can access their profile
                    if(firebaseUser.isEmailVerified()){
                        Toast.makeText(OwnerActivity.this, "You are Logged in now", Toast.LENGTH_SHORT).show();
                        //Open user Profile
                        Intent intent = new Intent(OwnerActivity.this, OwnerDashboardActivity.class);
                        startActivity(intent);
                    //    finish();
                    }else{
                        firebaseUser.sendEmailVerification();
                        authProfile.signOut();  //Sign out user
                        showAlertDialog();
                    }



                }else{
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e){
                        editTextLoginEmail.setError("User does not exists or is no longer valid. Please register gain");
                        editTextLoginEmail.requestFocus();
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        editTextLoginEmail.setError("Invalid credentials. Kindly, check and re-enter");
                        editTextLoginEmail.requestFocus();
                    }catch (Exception e){
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(OwnerActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    //*************************************************************************************************************************************

    private void showAlertDialog() {
        //Setup the Alert Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(OwnerActivity.this);
        builder.setTitle("Email Not Verified");
        builder.setMessage("Please verify your email now. You can not login without email verification");

        //Open Email Apps if user clicks/taps continue button
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);        //To open email app in new window and not within our app
                startActivity(intent);
            }
        });
        //Create the alertDialog
        AlertDialog alertDialog = builder.create();

        //Show the AlertDialog
        alertDialog.show();

    }

    //**********************************************************************************************************************************************

    // check if User is already logged in. In such case, straightaway take user to the User's profile
    @Override
    protected void onStart() {
        super.onStart();
        if(authProfile.getCurrentUser() != null){
            Toast.makeText(OwnerActivity.this, "Already Logged In!", Toast.LENGTH_SHORT).show();
            //Start the UserProfileActivity
            Intent intent = new Intent(OwnerActivity.this, OwnerDashboardActivity.class);
            startActivity(intent);
         //   finish();
        }else{
            Toast.makeText(OwnerActivity.this, "You can Login know", Toast.LENGTH_SHORT).show();
        }
    }
}
