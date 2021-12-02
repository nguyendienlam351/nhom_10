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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import tdc.edu.vn.nhom_10.CustomView.CustomActionBar;
import tdc.edu.vn.nhom_10.adapter.DanhSachDonHangAdapter;
import tdc.edu.vn.nhom_10.model.ChiTietDonHang;
import tdc.edu.vn.nhom_10.model.DonHang;

public class DanhSachDonHang extends AppCompatActivity {
    RecyclerView lvDanhSachMon;
    TextView tvTenBan;
    Button btnXong;
    DanhSachDonHangAdapter danhSachDonHangAdapter;
    DatabaseReference database;
    DonHang donHang = new DonHang();
    ArrayList<ChiTietDonHang> chiTietDonHangArrayList;
    CustomActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_don_hang);

        setControl();
        setEvent();
    }

    private void setEvent() {
        //Actionbar
        actionBar.setDelegation(new CustomActionBar.ActionBarDelegation() {
            @Override
            public void backOnClick() {
                finish();
            }
        });
        actionBar.setActionBarName("Đơn hàng của bàn");

        chiTietDonHangArrayList = new ArrayList<ChiTietDonHang>();
        database = FirebaseDatabase.getInstance().getReference();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            //Lấy dữ liệu đơn hàng theo mã bàn
            String maBan = bundle.getString("maBan", "");
            getDataDonHang(maBan);
        }

        danhSachDonHangAdapter = new DanhSachDonHangAdapter(DanhSachDonHang.this, R.layout.layout_item_mon_2, chiTietDonHangArrayList);

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

    //Lấy dữ liệu đơn hàng theo mã bàn
    private void getDataDonHang(String maBan) {
        database.child("Ban").child(maBan).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DonHang nDonHang = snapshot.getValue(DonHang.class);
                if (nDonHang != null) {
                    donHang = nDonHang;
                    tvTenBan.setText(donHang.getTenBan());
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
        actionBar = findViewById(R.id.actionBar);
    }
}