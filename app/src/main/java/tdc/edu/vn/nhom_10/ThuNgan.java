package tdc.edu.vn.nhom_10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import tdc.edu.vn.nhom_10.PhaCheFragment.XemHoaDon;
import tdc.edu.vn.nhom_10.PhaCheFragment.XuatKho;
import tdc.edu.vn.nhom_10.PhucVuFragment.DatMon;
import tdc.edu.vn.nhom_10.PhucVuFragment.LichLamViec;
import tdc.edu.vn.nhom_10.ThuNganFragment.ThanhToan;
import tdc.edu.vn.nhom_10.ThuNganFragment.ThuChi;

public class ThuNgan extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static int Fragment_thanhtoan = 0;
    private static int Fragment_thuchi = 1;
    private static int Fragment_calendar = 2;

    private int CurrentFragment = Fragment_thanhtoan;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    Toolbar toolbar;

    private TextView ten,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thu_ngan);
        navigationView = findViewById(R.id.navigation_view);
        ten = navigationView.getHeaderView(0).findViewById(R.id.tennhanvien);
        email = navigationView.getHeaderView(0).findViewById(R.id.emailnhanvien);
        getInfoUser();
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_navigation_drawer,R.string.close_navigation_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);

        replaceFragment(new ThanhToan());
        navigationView.getMenu().findItem(R.id.thanhtoan).setChecked(true);

    }
    private void getInfoUser(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String emaill = user.getEmail();
        email.setText(emaill);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.thanhtoan){
            if(CurrentFragment != Fragment_thanhtoan){
                replaceFragment(new ThanhToan());
                CurrentFragment = Fragment_thuchi;
                toolbar.setTitle(R.string.thanhtoan);
            }
        }else if (id==R.id.thuchi){
            if(CurrentFragment != Fragment_thuchi){
                replaceFragment(new ThuChi());
                CurrentFragment = Fragment_thuchi;
                toolbar.setTitle(R.string.thuchi);
            }
        }else if(id==R.id.calendar){
            if(CurrentFragment != Fragment_calendar){
                replaceFragment(new LichLamViec());
                CurrentFragment = Fragment_calendar;
                toolbar.setTitle(R.string.lich);
            }
        }
        else{
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