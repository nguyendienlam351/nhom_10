package tdc.edu.vn.nhom_10;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import tdc.edu.vn.nhom_10.CustomView.CustomActionBar;
import tdc.edu.vn.nhom_10.adapter.MaGiamGiaAdapter;
import tdc.edu.vn.nhom_10.model.DonHang;
import tdc.edu.vn.nhom_10.model.MaGiamGia;

public class QuanLyMaGiamGia extends AppCompatActivity {
    RecyclerView lvDanhSachGiamGia;
    DatabaseReference database;
    CustomActionBar actionBar;
    ArrayList<MaGiamGia> maGiamGiaArrayList;
    MaGiamGiaAdapter maGiamGiaAdapter;
    SearchView edtTimKiem;
    ImageButton btnThem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_ma_giam_gia);
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

        actionBar.setActionBarName("Mã giảm giá");

        database = FirebaseDatabase.getInstance().getReference("MaGiamGia");
        maGiamGiaArrayList = new ArrayList<MaGiamGia>();
        maGiamGiaAdapter = new MaGiamGiaAdapter(QuanLyMaGiamGia.this, R.layout.layout_item_ma_giam_gia, maGiamGiaArrayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(QuanLyMaGiamGia.this);

        layoutManager.setOrientation(RecyclerView.VERTICAL);

        lvDanhSachGiamGia.setLayoutManager(layoutManager);

        lvDanhSachGiamGia.setAdapter(maGiamGiaAdapter);

        getDataMaGiamGia();

        edtTimKiem.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuanLyMaGiamGia.this, ThemMaGiamGia.class);
                startActivity(intent);
            }
        });
    }

    private void filter(String tenMa) {
        ArrayList<MaGiamGia> filterList = new ArrayList<MaGiamGia>();
        for (MaGiamGia maGiamGia : maGiamGiaArrayList) {
            if (maGiamGia.getTenMaGiamGia().toLowerCase().contains(tenMa.toLowerCase())) {
                filterList.add(maGiamGia);
            }
        }

        maGiamGiaAdapter.filterList(filterList);
    }

    private void getDataMaGiamGia() {

        database.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MaGiamGia maGiamGia = snapshot.getValue(MaGiamGia.class);
                maGiamGiaArrayList.add(0, maGiamGia);
                maGiamGiaAdapter.notifyDataSetChanged();
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
        actionBar = findViewById(R.id.actionBar);
        lvDanhSachGiamGia = findViewById(R.id.lvDanhSachGiamGia);
        edtTimKiem = findViewById(R.id.edtTimKiem);
        btnThem = findViewById(R.id.btnThem);
    }
}