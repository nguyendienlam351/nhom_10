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
import tdc.edu.vn.nhom_10.adapter.NhapKhoAdapter;
import tdc.edu.vn.nhom_10.model.NhanVien;
import tdc.edu.vn.nhom_10.model.NhapKho;

public class DanhSachNhapKho extends AppCompatActivity {
    ImageButton btnBack;
    ImageButton btnThem;
    RecyclerView lvNhapKho;
    ArrayList<NhapKho> list = new ArrayList<NhapKho>();
    NhapKhoAdapter adapter;
    ArrayList<String> chucVu = new ArrayList<String>();
    DatabaseReference mData;
    SearchView svSearch;
    CustomActionBar actionBar;
    int viTri = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_nhap_kho);

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

        actionBar.setActionBarName("Danh sách nhập kho");

        //Gọi firebase
        getFirebase();

        //
        adapter = new NhapKhoAdapter(this, R.layout.layout_item_nhap_kho, list);
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

                Intent intent = new Intent(getApplicationContext(), DanhSachNguyenLieuNhap.class);
                startActivity(intent);
            }
        });
    }

    //Hàm lọc theo tên
    private void filter(String search) {
        ArrayList<NhapKho> filterList = new ArrayList<>();
        for (NhapKho nhapKho : list) {
            if (nhapKho.getTenNhapKho().toLowerCase().contains(search.toLowerCase())) {
                filterList.add(nhapKho);
            }
            adapter.filterList(filterList);
        }
    }

    //Hàm đọc firebase
    private void getFirebase() {
        // Write a message to the database
        mData = FirebaseDatabase.getInstance().getReference("NhapKho");
        mData.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                NhapKho nhapKho = snapshot.getValue(NhapKho.class);
                if (nhapKho != null) {
                    list.add(0,nhapKho);
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