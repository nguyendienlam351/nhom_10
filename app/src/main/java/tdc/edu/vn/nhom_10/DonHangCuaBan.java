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

import tdc.edu.vn.nhom_10.CustomView.CustomActionBar;
import tdc.edu.vn.nhom_10.CustomView.CustomAlertDialog;
import tdc.edu.vn.nhom_10.adapter.ChiTietDonHangAdater;
import tdc.edu.vn.nhom_10.model.DonHang;
import tdc.edu.vn.nhom_10.model.ChiTietDonHang;

public class DonHangCuaBan extends AppCompatActivity {
    RecyclerView lvDanhSachMon;
    TextView tvTenBan;
    TextView tvTong;
    Button btnDat;
    ImageButton btnThemMon;
    ChiTietDonHangAdater chiTietDonHangAdater;
    DatabaseReference database;
    DonHang donHang = new DonHang();
    ArrayList<ChiTietDonHang> chiTietDonHangArrayList;
    CustomActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_don_hang_cua_ban);
        setControl();
        setEvent();
    }

    private void setEvent() {
        //actionbar
        actionBar.setDelegation(new CustomActionBar.ActionBarDelegation() {
            @Override
            public void backOnClick() {
                Intent intent = new Intent(DonHangCuaBan.this, PhucVu.class);
                startActivity(intent);
            }
        });
        actionBar.setActionBarName("Đơn hàng của bàn");

        database = FirebaseDatabase.getInstance().getReference();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            //Lấy dữ liệu bàn theo tên
            String maBan = bundle.getString("maBan", "");
            getDataDonHang(maBan);
        }

        chiTietDonHangArrayList = new ArrayList<ChiTietDonHang>();
        chiTietDonHangAdater = new ChiTietDonHangAdater(DonHangCuaBan.this, R.layout.layout_item_mon_4, chiTietDonHangArrayList);
        chiTietDonHangAdater.setDelegation(new ChiTietDonHangAdater.ChiTietDonHangClickListener() {

            @Override
            public void iconClick(ChiTietDonHang chiTietDonHang) {
                donHang.getChiTietDonHang().remove(chiTietDonHang);
                database.child("Ban").child(donHang.getMaBan()).setValue(donHang);
            }

            @Override
            public void tangSoLuong(ChiTietDonHang chiTietDonHang) {
                if(chiTietDonHang.getTrangThai().equals("")) {
                    if (chiTietDonHang.getSoLuong() < 30) {
                        chiTietDonHang.setSoLuong(chiTietDonHang.getSoLuong() + 1);
                        database.child("Ban").child(donHang.getMaBan()).setValue(donHang);
                    }
                }
            }

            @Override
            public void giamSoLuong(ChiTietDonHang chiTietDonHang) {
                if (chiTietDonHang.getTrangThai().equals("")) {
                    if (chiTietDonHang.getSoLuong() > 1) {
                        chiTietDonHang.setSoLuong(chiTietDonHang.getSoLuong() - 1);
                        database.child("Ban").child(donHang.getMaBan()).setValue(donHang);
                    }
                }
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(DonHangCuaBan.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        lvDanhSachMon.setLayoutManager(layoutManager);
        lvDanhSachMon.setAdapter(chiTietDonHangAdater);

        //Sự kiện nút thêm món
        btnThemMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DonHangCuaBan.this, DanhSachMon.class);
                Bundle bundle = new Bundle();
                bundle.putString("maBan", donHang.getMaBan());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        //Sự kiện nút đặt món
        btnDat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean kiemTra = false;
                //Kiểm tra trạng thái tất cả các món khác rỗng
                for (ChiTietDonHang item : donHang.getChiTietDonHang()) {
                    if (item.getTrangThai().equals("")) {
                        kiemTra = true;
                        break;
                    }
                }
                if(kiemTra) {
                    //Set trạng thái món sang chờ
                    for (ChiTietDonHang item : donHang.getChiTietDonHang()) {
                        if (item.getTrangThai().equals("")) {
                            item.setTrangThai("chờ");
                        }
                    }
                    database.child("Ban").child(donHang.getMaBan()).setValue(donHang).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            new CustomAlertDialog(DonHangCuaBan.this, "Thành công", "Đặt món thành công.",CustomAlertDialog.SUCCESS).show();
                        }
                    });
                }
                else {
                    new CustomAlertDialog(DonHangCuaBan.this,
                            "Hãy chọn thêm món",
                            "Danh sách trống hoặc các món đã được đặt.\nHãy chọn thêm món.",
                            CustomAlertDialog.ERROR).show();
                }
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
                    NumberFormat formatter = new DecimalFormat("#,###,###");
                    tvTong.setText("Tổng: " + formatter.format(tongDonHang()) + " đ");
                    chiTietDonHangArrayList.clear();
                    chiTietDonHangArrayList.addAll(donHang.getChiTietDonHang());
                    chiTietDonHangAdater.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public int tongDonHang() {
        int tong = 0;
        for (ChiTietDonHang item : donHang.getChiTietDonHang()) {
            if (!item.getTrangThai().equals("hủy")) {
                tong += item.getGia() * item.getSoLuong();
            }
        }

        return tong;
    }

    private void setControl() {
        lvDanhSachMon = findViewById(R.id.lvDanhSachMon);
        tvTenBan = findViewById(R.id.tvTenBan);
        btnThemMon = findViewById(R.id.btnThemMon);
        tvTong = findViewById(R.id.tvTong);
        btnDat = findViewById(R.id.btnDat);
        actionBar = findViewById(R.id.actionBar);
    }
}