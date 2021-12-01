package tdc.edu.vn.nhom_10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;


import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import tdc.edu.vn.nhom_10.PhucVuFragment.DatMon;

public class PhucVu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static int Fragment_order = 0;
    private static int Fragment_calendar = 1;

    private int CurrentFragment = Fragment_order;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    Toolbar toolbar;

    private TextView ten;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phuc_vu);
        navigationView = findViewById(R.id.navigation_view);
        ten = navigationView.getHeaderView(0).findViewById(R.id.tennhanvien);
        getInfoUser();
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_navigation_drawer,R.string.close_navigation_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);

        replaceFragment(new DatMon());
        navigationView.getMenu().findItem(R.id.order).setChecked(true);

    }
    private void getInfoUser(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String maNV = user.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("NhanVien/"+maNV);
        myRef.child("hoTen").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                ten.setText(String.valueOf(value));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.order){
            if(CurrentFragment != Fragment_order){
                replaceFragment(new DatMon());
                CurrentFragment = Fragment_order;
                toolbar.setTitle("Đạt món");
            }
        }else if (id==R.id.calendar){
            if(CurrentFragment != Fragment_calendar){
                replaceFragment(new LichLamViec());
                CurrentFragment = Fragment_calendar;
                toolbar.setTitle("Lịch làm việc");
            }
        }else{
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(),DangNhap.class);
            startActivity(intent);

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }
    private void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame,fragment);
        transaction.commit();
    }
}