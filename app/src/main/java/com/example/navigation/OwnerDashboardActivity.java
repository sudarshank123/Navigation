package com.example.navigation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.NotNull;


public class OwnerDashboardActivity extends AppCompatActivity {

    private FirebaseAuth logout;



    BottomNavigationView ownerbottomNavigationView;
    OwnerDashboardFragment ownerdashboardFragment = new OwnerDashboardFragment();
    OwnerPaymentFragment ownerpaymentFragment = new OwnerPaymentFragment();
    OwnerProteinFragment ownerproteinFragment = new OwnerProteinFragment();
    OwnerNewsFeedFragment ownernewsFeedFragment = new OwnerNewsFeedFragment();
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_dashboard);

//******************************************************Owner Bottom Navigation*****************************************************************************
        mFirebaseAuth = FirebaseAuth.getInstance();
        ownerbottomNavigationView = findViewById(R.id.owner_bottom_nav);
        //Replace Container with dashboard Fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.owner_container, ownerdashboardFragment).commit();

        ownerbottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_dashboard_owner:
                        getSupportFragmentManager().beginTransaction().replace(R.id.owner_container, ownerdashboardFragment).commit();
                        return true;
                    case R.id.navigation_payment_owner:
                        getSupportFragmentManager().beginTransaction().replace(R.id.owner_container, ownerpaymentFragment).commit();
                        return true;
                    case R.id.navigation_protein_owner:
                        getSupportFragmentManager().beginTransaction().replace(R.id.owner_container, ownerproteinFragment).commit();
                        return true;
                    case R.id.navigation_news_owner:
                        getSupportFragmentManager().beginTransaction().replace(R.id.owner_container, ownernewsFeedFragment).commit();
                        return true;
                }
                return false;
            }
        });
//*******************************************************************************Navigation Drawer*****************************************************************************
        logout = FirebaseAuth.getInstance();
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        DrawerLayout drawerLayout = findViewById(R.id.owner_drawer_layout);
        NavigationView navigationView = findViewById(R.id.owner_nav_view);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                int id = item.getItemId();
                drawerLayout.closeDrawer(GravityCompat.START);
                switch (id){

                    case R.id.nav_profile:
                        //Toast.makeText(OwnerDashboardActivity.this, "Home is clicked", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(OwnerDashboardActivity.this, OwnerProfileActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_chat:
                        Toast.makeText(OwnerDashboardActivity.this, "Chat is clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_payment_history:
                        Toast.makeText(OwnerDashboardActivity.this, "Payment is clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_feedback:
                        Toast.makeText(OwnerDashboardActivity.this, "Feedback is clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_share:
                        Toast.makeText(OwnerDashboardActivity.this, "Share is clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_rate:
                        Toast.makeText(OwnerDashboardActivity.this, "Rate is clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_privacy:
                        Toast.makeText(OwnerDashboardActivity.this, "Privacy is clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_logout:
                        Toast.makeText(OwnerDashboardActivity.this, "Successfully Logout", Toast.LENGTH_SHORT).show();
                        finish();
                        logout.signOut();
                        break;
                    case R.id.nav_setting:
                        Toast.makeText(OwnerDashboardActivity.this, "Setting is clicked", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        return true;
                }
                return true;
            }
        });
//***********************************************************************************************************************************************************************


    }
}