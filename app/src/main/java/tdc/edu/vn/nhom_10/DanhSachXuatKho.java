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
import tdc.edu.vn.nhom_10.adapter.XuatKhoAdapter;
import tdc.edu.vn.nhom_10.model.XuatKho;

public class DanhSachXuatKho extends AppCompatActivity {
    ImageButton btnThem;
    RecyclerView lvXuatKho;
    ArrayList<XuatKho> list = new ArrayList<XuatKho>();
    XuatKhoAdapter adapter;
    DatabaseReference mData;
    SearchView svSearch;
    CustomActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_xuat_kho);

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

        actionBar.setActionBarName("Danh sách xuất kho");

        //Gọi firebase
        getFirebase();

        //
        adapter = new XuatKhoAdapter(this, R.layout.layout_item_xuat_kho, list);
        lvXuatKho.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        lvXuatKho.setLayoutManager(layoutManager);


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

                Intent intent = new Intent(getApplicationContext(), DanhSachNguyenLieuXuat.class);
                startActivity(intent);
            }
        });
    }

    //Hàm lọc theo tên
    private void filter(String search) {
        ArrayList<XuatKho> filterList = new ArrayList<>();
        for (XuatKho xuatKho : list) {
            if (xuatKho.getTenXuatKho().toLowerCase().contains(search.toLowerCase())) {
                filterList.add(xuatKho);
            }
            adapter.filterList(filterList);
        }
    }

    //Hàm đọc firebase
    private void getFirebase() {
        // Write a message to the database
        mData = FirebaseDatabase.getInstance().getReference("XuatKho");
        mData.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                XuatKho xuatKho = snapshot.getValue(XuatKho.class);
                if (xuatKho != null) {
                    list.add(0,xuatKho);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
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
        lvXuatKho = findViewById(R.id.lvXuatKho);
        svSearch = findViewById(R.id.svSearch);
        actionBar = findViewById(R.id.actionBar);
    }
}