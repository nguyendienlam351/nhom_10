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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;

import tdc.edu.vn.nhom_10.CustomView.CustomActionBar;
import tdc.edu.vn.nhom_10.adapter.ChiTietThanhToanAdapter;
import tdc.edu.vn.nhom_10.model.DonHang;
import tdc.edu.vn.nhom_10.model.ChiTietDonHang;
import tdc.edu.vn.nhom_10.model.HoaDon;

public class ChiTietThanhToan extends AppCompatActivity {
    RecyclerView lvDanhSachMon;
    TextView tvTong;
    TextView tvHoTen;
    TextView tvEmail;
    Button btnThanhToan;
    ChiTietThanhToanAdapter chiTietThanhToanAdapter;
    DatabaseReference database;
    DonHang donHang;
    ArrayList<ChiTietDonHang> chiTietDonHangArrayList;
    CustomActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_thanh_toan);

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

        actionBar.setActionBarName("Đơn hàng của bàn");

        chiTietDonHangArrayList = new ArrayList<ChiTietDonHang>();
        database = FirebaseDatabase.getInstance().getReference();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            String maBan = bundle.getString("maBan", "");

            getDataDonHang(maBan);
        }

        chiTietThanhToanAdapter = new ChiTietThanhToanAdapter(ChiTietThanhToan.this, R.layout.layout_item_mon_3, chiTietDonHangArrayList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(ChiTietThanhToan.this);

        layoutManager.setOrientation(RecyclerView.VERTICAL);

        lvDanhSachMon.setLayoutManager(layoutManager);

        lvDanhSachMon.setAdapter(chiTietThanhToanAdapter);

        getNhanVien();

        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chiTietDonHangArrayList.size() != 0){
                    Calendar calendar = Calendar.getInstance();
                    final int year = calendar.get(Calendar.YEAR);
                    final int month = calendar.get(Calendar.MONTH);
                    final int day = calendar.get(Calendar.DAY_OF_MONTH);

                    DatabaseReference myRef = database.child("HoaDon");

                    String maHoaDon = myRef.push().getKey();
                    HoaDon hoaDon = new HoaDon();
                    hoaDon.setMaHoaDon(maHoaDon);
                    hoaDon.setDonHang(donHang);
                    hoaDon.setNgayThang(day + "/" + (month + 1) + "/" + year);
                    hoaDon.setTong(tongDonHang());
                    hoaDon.setEmail(tvEmail.getText().toString());
                    hoaDon.setHoTen(tvHoTen.getText().toString());

                    myRef.child(maHoaDon).setValue(hoaDon).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            database.child("Ban").child(String.valueOf(donHang.getMaBan())).child("chiTietDonHang").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(ChiTietThanhToan.this, "Thanh toán thành công", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
                else {
                    Toast.makeText(ChiTietThanhToan.this, "Hãy chọn thêm món", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getNhanVien(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String maNhanVien = user.getUid();

        database.child("NhanVien/"+maNhanVien).child("hoTen").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                tvHoTen.setText("Họ tên: "+ value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        database.child("NhanVien/"+maNhanVien).child("email").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                tvEmail.setText("Email: " + value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
                    chiTietDonHangArrayList.addAll(filter(donHang.getChiTietDonHang()));
                    chiTietThanhToanAdapter.notifyDataSetChanged();
                    NumberFormat formatter = new DecimalFormat("#,###,###");
                    tvTong.setText("Tổng: " + formatter.format(tongDonHang())+ " đ");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public int tongDonHang() {
        int tong = 0;
        for (ChiTietDonHang item : chiTietDonHangArrayList) {
            tong += item.getGia() * item.getSoLuong();
        }

        return tong;
    }

    private ArrayList<ChiTietDonHang> filter(ArrayList<ChiTietDonHang> arrayList) {
        ArrayList<ChiTietDonHang> filterList = new ArrayList<ChiTietDonHang>();
        for (ChiTietDonHang chiTietDonHang : arrayList) {
            if (chiTietDonHang.getTrangThai().equals("xong")) {
                filterList.add(chiTietDonHang);
            }
        }

        return filterList;
    }

    private void setControl() {
        lvDanhSachMon = findViewById(R.id.lvDanhSachMon);
        tvTong = findViewById(R.id.tvTong);
        tvHoTen = findViewById(R.id.tvHoTen);
        tvEmail = findViewById(R.id.tvEmail);
        btnThanhToan = findViewById(R.id.btnThanhToan);
        actionBar = findViewById(R.id.actionBar);
    }
}