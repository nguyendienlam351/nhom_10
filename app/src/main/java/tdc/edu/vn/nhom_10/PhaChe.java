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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import tdc.edu.vn.nhom_10.PhaCheFragment.XemHoaDon;
import tdc.edu.vn.nhom_10.PhaCheFragment.XuatKho;
import tdc.edu.vn.nhom_10.PhucVuFragment.DatMon;
import tdc.edu.vn.nhom_10.model.NhanVien;

public class PhaChe extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static int Fragment_seenorder = 0;
    private static int Fragment_xuatkho = 1;
    private static int Fragment_calendar = 2;

    private int CurrentFragment = Fragment_seenorder;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    Toolbar toolbar;
    ImageView imageView;
    FirebaseAuth mAuth;
    private TextView ten;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pha_che);
        navigationView = findViewById(R.id.navigation_view);
        imageView = navigationView.getHeaderView(0).findViewById(R.id.imgAnh);
        ten = navigationView.getHeaderView(0).findViewById(R.id.tennhanvien);
        getInfoUser();
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_navigation_drawer,R.string.close_navigation_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        replaceFragment(new XemHoaDon());
        navigationView.getMenu().findItem(R.id.xemhoadon).setChecked(true);

        imageView = navigationView.getHeaderView(0).findViewById(R.id.imgAnh);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhaChe.this, ThongTinNhanVien.class);

                startActivity(intent);

            }
        });

    }
    private void getInfoUser(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String maNV = user.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("NhanVien/"+maNV);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                NhanVien nhanVien = snapshot.getValue(NhanVien.class);
                ten.setText(nhanVien.getHoTen());
                getAnhNhanVien(nhanVien.getAnh());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // lay anh
    private void getAnhNhanVien(String anh) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference("NhanVien");
        int dot = anh.lastIndexOf('.');
        String base = (dot == -1) ? anh : anh.substring(0, dot);
        String extension = (dot == -1) ? "" : anh.substring(dot + 1);
        try {
            final File file = File.createTempFile(base, extension);
            storageRef.child(anh).getFile(file)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                            imageView.setImageBitmap(bitmap);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.xemhoadon){
            if(CurrentFragment != Fragment_seenorder){
                replaceFragment(new XemHoaDon());
                CurrentFragment = Fragment_seenorder;
                toolbar.setTitle(R.string.xemhoadon);
            }
        }else if (id==R.id.xuatkho){
            if(CurrentFragment != Fragment_xuatkho){
                replaceFragment(new XuatKho());
                CurrentFragment = Fragment_xuatkho;
                toolbar.setTitle(R.string.xuatkho);
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