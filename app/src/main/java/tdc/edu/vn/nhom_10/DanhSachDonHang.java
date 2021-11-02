package tdc.edu.vn.nhom_10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;

import tdc.edu.vn.nhom_10.adater.ChiTietDonHangAdater;
import tdc.edu.vn.nhom_10.adater.ChiTietThanhToanAdapter;
import tdc.edu.vn.nhom_10.adater.DanhSachDonHangAdapter;
import tdc.edu.vn.nhom_10.model.ChiTietDonHang;
import tdc.edu.vn.nhom_10.model.DonHang;
import tdc.edu.vn.nhom_10.model.HoaDon;

public class DanhSachDonHang extends AppCompatActivity {
    RecyclerView lvDanhSachMon;
    TextView tvTenBan;
    Button btnXong;
    DanhSachDonHangAdapter danhSachDonHangAdapter;
    DatabaseReference database;
    DonHang donHang = new DonHang();
    ArrayList<ChiTietDonHang> chiTietDonHangArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_don_hang);
        getSupportActionBar().setTitle("Đơn hàng của bàn");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setControl();
        setEvent();
    }

    private void setEvent() {
        chiTietDonHangArrayList = new ArrayList<ChiTietDonHang>();
        database = FirebaseDatabase.getInstance().getReference();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            String maBan = bundle.getString("maBan", "");

            getDataDonHang(maBan);
        }

        danhSachDonHangAdapter = new DanhSachDonHangAdapter(DanhSachDonHang.this, R.layout.layout_danh_sach_don_hang, chiTietDonHangArrayList);

        danhSachDonHangAdapter.setDelegation(new DanhSachDonHangAdapter.DanhSachDonHangClickListener() {
            @Override
            public void iconClick(ChiTietDonHang chiTietDonHang, int position) {
                database.child("Ban").child(donHang.getMaBan())
                        .child("chiTietDonHang").child(String.valueOf(position))
                        .child("trangThai").setValue("hủy");

            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(DanhSachDonHang.this);

        layoutManager.setOrientation(RecyclerView.VERTICAL);

        lvDanhSachMon.setLayoutManager(layoutManager);

        lvDanhSachMon.setAdapter(danhSachDonHangAdapter);

        btnXong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (ChiTietDonHang item : chiTietDonHangArrayList){
                    if(item.getTrangThai().equals("chờ")){
                        item.setTrangThai("xong");
                    }
                }
                database.child("Ban").child(donHang.getMaBan())
                        .child("chiTietDonHang").setValue(chiTietDonHangArrayList);
            }
        });

    }

    private void getDataDonHang(String maBan) {
        database.child("Ban").child(maBan).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DonHang nDonHang = snapshot.getValue(DonHang.class);
                if (nDonHang != null) {
                    donHang = nDonHang;
                    chiTietDonHangArrayList.clear();
                    chiTietDonHangArrayList.addAll(donHang.getChiTietDonHang());
                    danhSachDonHangAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setControl() {
        lvDanhSachMon = findViewById(R.id.lvDanhSachMon);
        tvTenBan = findViewById(R.id.tvTenBan);
        btnXong = findViewById(R.id.btnXong);
    }
}