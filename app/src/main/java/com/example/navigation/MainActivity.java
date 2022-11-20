package com.example.navigation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.example.navigation.R.id;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.NotNull;


public class MainActivity extends AppCompatActivity {


    BottomNavigationView bottomNavigationView;
    DashboardFragment dashboardFragment = new DashboardFragment();
    PaymentFragment paymentFragment = new PaymentFragment();
    ProteinFragment proteinFragment = new ProteinFragment();
    NewsFeedFragment newsFeedFragment = new NewsFeedFragment();
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





//***************************************************************************************Bottom Navigation*******************************************************************
        mFirebaseAuth = FirebaseAuth.getInstance();
       bottomNavigationView = findViewById(id.bottom_nav);
       //Replace Container with dashboard Fragment
       getSupportFragmentManager().beginTransaction().replace(R.id.container, dashboardFragment).commit();

       bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(MenuItem item) {
               switch (item.getItemId()){
                   case id.navigation_dashboard:
                       getSupportFragmentManager().beginTransaction().replace(R.id.container, dashboardFragment).commit();
                        return true;
                   case id.navigation_payment:
                       getSupportFragmentManager().beginTransaction().replace(R.id.container, paymentFragment).commit();
                       return true;
                   case id.navigation_protein:
                       getSupportFragmentManager().beginTransaction().replace(R.id.container, proteinFragment).commit();
                       return true;
                   case id.navigation_news:
                       getSupportFragmentManager().beginTransaction().replace(R.id.container, newsFeedFragment).commit();
                       return true;
               }
               return false;
           }
       });
 //**************************************************************************************************************Navigation Drawer*******************************************

        MaterialToolbar toolbar = findViewById(id.topAppBar);
        DrawerLayout drawerLayout = findViewById(R.id.trainee_drawer_layout);
        NavigationView navigationView = findViewById(R.id.trainee_nav_view);
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
                        Toast.makeText(MainActivity.this, "Home is clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_chat:
                        Toast.makeText(MainActivity.this, "Chat is clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_payment_history:
                        Toast.makeText(MainActivity.this, "Payment is clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_feedback:
                        Toast.makeText(MainActivity.this, "Feedback is clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_share:
                        Toast.makeText(MainActivity.this, "Share is clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_rate:
                        Toast.makeText(MainActivity.this, "Rate is clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_privacy:
                        Toast.makeText(MainActivity.this, "Privacy is clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_logout:
                        Toast.makeText(MainActivity.this, "Successfully Logout", Toast.LENGTH_SHORT).show();
                        mFirebaseAuth.signOut();
                        finish();
                        break;
                    case R.id.nav_setting:
                        Toast.makeText(MainActivity.this, "Setting is clicked", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        return true;
                }
                return true;
            }
        });

//*************************************************************************************************************************************************************************






    }
}