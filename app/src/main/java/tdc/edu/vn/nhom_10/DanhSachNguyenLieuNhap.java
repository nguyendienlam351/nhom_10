package tdc.edu.vn.nhom_10;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import tdc.edu.vn.nhom_10.CustomView.CustomActionBar;
import tdc.edu.vn.nhom_10.adapter.MyRecylerViewAdapterNguyenLieuNhap;
import tdc.edu.vn.nhom_10.model.DanhMucKho;
import tdc.edu.vn.nhom_10.model.NhanVien;

public class DanhSachNguyenLieuNhap extends AppCompatActivity {
    ImageButton btnThem;
    RecyclerView lvNhapKho;
    ArrayList<DanhMucKho> list = new ArrayList<DanhMucKho>();
    MyRecylerViewAdapterNguyenLieuNhap adapter;
    DatabaseReference mData;
    SearchView svSearch;
    CustomActionBar actionBar;
    int viTri = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_nguyen_lieu_nhap);

        setControl();
        setEvent();

    }


    private void setEvent() {
        actionBar.setDelegation(new CustomActionBar.ActionBarDelegation() {
            @Override
            public void backOnClick() {
                finish();
            }
        });

        actionBar.setActionBarName("Quản lý nhân viên");

        //Gọi firebase
        getFirebase();

        //
        adapter = new MyRecylerViewAdapterNguyenLieuNhap(this, R.layout.layout_item_nguyen_lieu_nhap, list);
        lvNhapKho.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        lvNhapKho.setLayoutManager(layoutManager);


        //Search
        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });

        //Chuyển màn hình thêm
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), ThemNhapKho.class);
                startActivity(intent);
            }
        });
    }

    //Hàm lọc theo tên
    private void filter(String search) {
        ArrayList<DanhMucKho> filterList = new ArrayList<>();
        for (DanhMucKho danhMucKho : list) {
            if (danhMucKho.getTen().toLowerCase().contains(search.toLowerCase())) {
                filterList.add(danhMucKho);
            }
            adapter.filterList(filterList);
        }
    }

    //Hàm đọc firebase
    private void getFirebase() {
        // Write a message to the database
        mData = FirebaseDatabase.getInstance().getReference("DanhMucKho");
//        for(int i= 1; i < 6;i++){
//            DanhMucKho danhMucKho = new DanhMucKho();
//            danhMucKho.setMaDanhMucKho(mData.push().getKey().toString());
//            danhMucKho.setTen("quy");
//            danhMucKho.setGia(10);
//            danhMucKho.setDonVi("kg");
//            mData.child(danhMucKho.getMaDanhMucKho()).setValue(danhMucKho);
//        }
        mData.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                DanhMucKho danhMucKho = snapshot.getValue(DanhMucKho.class);
                if (danhMucKho != null) {
                    list.add(0,danhMucKho);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                NhanVien nhanVien = snapshot.getValue(NhanVien.class);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void setControl() {
        btnThem = findViewById(R.id.btnThem);
        lvNhapKho = findViewById(R.id.lvNhapKho);
        svSearch = findViewById(R.id.svSearch);
        actionBar = findViewById(R.id.actionBar);
    }
}