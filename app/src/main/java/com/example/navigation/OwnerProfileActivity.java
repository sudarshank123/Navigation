package com.example.navigation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


import de.hdodenhof.circleimageview.CircleImageView;


public class OwnerProfileActivity extends AppCompatActivity {

    private TextView textViewWelcome, textViewFullName, textViewEmail, textViewDoB, textViewGender,textViewMobile, textViewAadhaar;
    private ProgressBar progressBar;
    private String fullName, email, doB, gender, mobile, aadhaar;
    private ImageView imageView;
    private FirebaseAuth authProfile;
    FloatingActionButton button;
    CircleImageView profileImage;
    private final int PICK_IMAGE_CODE= 172;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_profile);


//********************************************************upload profile image*********************************

        button = findViewById(R.id.fabUpload);
        profileImage = findViewById(R.id.profile_img);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkP();
            }
        });
//**************************Back Button**********************************
        Button button = findViewById(R.id.back_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OwnerProfileActivity.this, OwnerDashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });

//**********************************************************************************


     //   getSupportActionBar().setTitle("Profile");                //title of toolbar top
        textViewWelcome = findViewById(R.id.textView_show_welcome);
        textViewFullName = findViewById(R.id.textView_show_full_name);
        textViewEmail = findViewById(R.id.textView_show_email);
        textViewDoB = findViewById(R.id.textView_show_dob);
        textViewGender = findViewById(R.id.textView_show_gender);
        textViewMobile = findViewById(R.id.textView_show_number);
        textViewAadhaar = findViewById(R.id.textView_show_aadhaar);
        progressBar = findViewById(R.id.progressBar);
        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser =authProfile.getCurrentUser();

        if(firebaseUser == null){
            Toast.makeText(OwnerProfileActivity.this, "Something went wrong! Owner Details are not available at the Moment", Toast.LENGTH_SHORT).show();

        }else {
            checkIfEmailVerified(firebaseUser);
            progressBar.setVisibility(View.VISIBLE);
            showOwnerProfile(firebaseUser);
        }
    }

    //******************************************************profileImage****************************************************************************
        private void checkP(){
            Dexter.withContext(OwnerProfileActivity.this)
                    .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            startActivityForResult(intent,PICK_IMAGE_CODE);
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();
                        }
                    }).check();
        }
//BitmapFactory.decodeByteArray(Base64.decode(oo,Base64.DEFAULT), 0, Base64.decode(oo,Base64.DEFAULT).length)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== PICK_IMAGE_CODE && resultCode ==RESULT_OK){
            if(data!=null){
                Uri imageUri = data.getData();
                try {
                    Bitmap original = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    original.compress(Bitmap.CompressFormat.JPEG,10,stream);
                    byte[] imageByte = stream.toByteArray();
                    SaveImage(original);
                    profileImage.setImageBitmap(original);
                    Log.d("SudarshanApp", String.valueOf(original));
                    uploadImage(imageByte);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void uploadImage(byte[] imageByte) {
        ProgressDialog progressDialog = new ProgressDialog(OwnerProfileActivity.this);
        progressDialog.setMessage("Image Uploading......");
        progressDialog.show();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("Images").child(System.currentTimeMillis()+".jpeg");
        storageReference.putBytes(imageByte).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                Toast.makeText(OwnerProfileActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(OwnerProfileActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static void SaveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        File myDir = new File(root + "/saved_images");
        Log.d("sudarshanroot",root);
        myDir.mkdirs();

        String fname = "sudarshan135.jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //*******************************************************************************************************************************************8
    //User coming to UserProfileActivity after successful registration
    private void checkIfEmailVerified(FirebaseUser firebaseUser) {
        if(!firebaseUser.isEmailVerified()){
            showAlertDialog();
        }

    }

    private void showAlertDialog() {
        //Setup the Alert Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(OwnerProfileActivity.this);
        builder.setTitle("Email Not Verified");
        builder.setMessage("Please verify your email now. You can not login without email verification next time");

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

    //Obtain owner details from firebase
    private void showOwnerProfile( FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();
        //Extracting user reference from Database for Registered Owners
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered GymOwners");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if(readUserDetails !=null){
                    fullName = firebaseUser.getDisplayName();
                    email = readUserDetails.email;
                    doB = readUserDetails.doB;
                    gender = readUserDetails.gender;
                    mobile = readUserDetails.mobile;
                    aadhaar = readUserDetails.aadhaarNumber;

                    textViewWelcome.setText(fullName);
                    textViewFullName.setText(fullName);
                    textViewEmail.setText(email);
                    textViewDoB.setText(doB);
                    textViewGender.setText(gender);
                    textViewMobile.setText(mobile);
                    textViewAadhaar.setText(aadhaar);

                    //*****************************************************************************************

                }
                //**************************************************************************************************************8
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OwnerProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });


    }
}