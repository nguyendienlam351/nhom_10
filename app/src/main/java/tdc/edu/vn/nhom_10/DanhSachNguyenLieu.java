package tdc.edu.vn.nhom_10;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import tdc.edu.vn.nhom_10.CustomView.CustomActionBar;
import tdc.edu.vn.nhom_10.QuanLyFragment.NhapXuatKho;
import tdc.edu.vn.nhom_10.adapter.MyRecylerViewNhanVien;
import tdc.edu.vn.nhom_10.adapter.NguyenLieuAdapter;
import tdc.edu.vn.nhom_10.model.NguyenLieu;
import tdc.edu.vn.nhom_10.model.NhanVien;

public class DanhSachNguyenLieu extends AppCompatActivity {
    private ImageButton btnThemNL;
    private SearchView svSearch;
    private RecyclerView rcvNguyenLieu;
    private NguyenLieuAdapter nguyenLieuAdapter;
    private ArrayList<NguyenLieu> data = new ArrayList<NguyenLieu>();
    private DatabaseReference myref;
    CustomActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_nguyen_lieu);
        setControl();
        setEvent();
    }

    private void setEvent() {
        actionBar.setDelegation(new CustomActionBar.ActionBarDelegation() {
            @Override
            public void backOnClick() {
                Intent intent = new Intent(getApplicationContext(),QuanLy.class);
                startActivity(intent);
            }
        });
        actionBar.setActionBarName("Danh sách nguyên liệu");

        getDatabase();
        nguyenLieuAdapter = new NguyenLieuAdapter(this, R.layout.layout_item_nguyen_lieu, data);
        rcvNguyenLieu.setAdapter(nguyenLieuAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvNguyenLieu.setLayoutManager(layoutManager);

        btnThemNL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ThemNguyenLieu.class);
                startActivity(intent);
            }
        });
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
        nguyenLieuAdapter.setDelegation(new NguyenLieuAdapter.NguyenLieuItemClickListener() {
            @Override
            public void itemClick(NguyenLieu nguyenLieu) {
                Intent intent = new Intent(DanhSachNguyenLieu.this, ChiTietNguyenLieu.class);
                Bundle bundle = new Bundle();
                bundle.putString("maNL", nguyenLieu.getMaNL());
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void iconClick(NguyenLieu nguyenLieu) {

            }
        });
    }
    private void getDatabase(){
        myref = FirebaseDatabase.getInstance().getReference("NguyenLieu");
        myref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                NguyenLieu nguyenLieu = snapshot.getValue(NguyenLieu.class);
                if (nguyenLieu != null) {
                    data.add(0,nguyenLieu);
                    nguyenLieuAdapter.notifyDataSetChanged();
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

    private void filter(String search) {
        ArrayList<NguyenLieu> filterList = new ArrayList<NguyenLieu>();
        for (NguyenLieu nguyenLieu : data) {
            if (nguyenLieu.getTenNL().toLowerCase().contains(search.toLowerCase())) {
                filterList.add(nguyenLieu);
            }
            nguyenLieuAdapter.filterList(filterList);
        }
    }

    private void setControl() {
        btnThemNL = findViewById(R.id.btnThemNL);
        rcvNguyenLieu = findViewById(R.id.rcvNguyenlieu);
        actionBar = findViewById(R.id.actionBar);
        svSearch = findViewById(R.id.svSearch);
    }
}