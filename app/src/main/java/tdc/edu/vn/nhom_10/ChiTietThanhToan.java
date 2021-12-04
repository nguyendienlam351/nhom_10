package tdc.edu.vn.nhom_10;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import tdc.edu.vn.nhom_10.CustomView.CustomActionBar;
import tdc.edu.vn.nhom_10.CustomView.CustomAlertDialog;
import tdc.edu.vn.nhom_10.CustomView.MaGiamGiaDialog;
import tdc.edu.vn.nhom_10.ThuNganFragment.ThanhToan;
import tdc.edu.vn.nhom_10.adapter.ChiTietThanhToanAdapter;
import tdc.edu.vn.nhom_10.model.DonHang;
import tdc.edu.vn.nhom_10.model.ChiTietDonHang;
import tdc.edu.vn.nhom_10.model.HoaDon;
import tdc.edu.vn.nhom_10.model.MaGiamGia;

public class ChiTietThanhToan extends AppCompatActivity {
    RecyclerView lvDanhSachMon;
    EditText edtGiamGia;
    TextView tvTong;
    TextView tvHoTen;
    TextView tvEmail;
    Button btnThanhToan;
    ImageButton btnClear;
    ChiTietThanhToanAdapter chiTietThanhToanAdapter;
    DatabaseReference database;
    DonHang donHang;
    ArrayList<ChiTietDonHang> chiTietDonHangArrayList;
    CustomActionBar actionBar;
    String hoTen;
    String email;
    int tong = 0;
    NumberFormat formatter = new DecimalFormat("#,###,###");
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    MaGiamGia maGiamGia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_thanh_toan);

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
            //Lấy danh sách đơn hàng theo mã bàn
            String maBan = bundle.getString("maBan", "");
            getDataDonHang(maBan);
        }

        chiTietThanhToanAdapter = new ChiTietThanhToanAdapter(ChiTietThanhToan.this, R.layout.layout_item_mon_3, chiTietDonHangArrayList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(ChiTietThanhToan.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        lvDanhSachMon.setLayoutManager(layoutManager);
        lvDanhSachMon.setAdapter(chiTietThanhToanAdapter);

        //Lấy thông tin nhân viên
        getNhanVien();

        //Thanh toán đơn hàng
        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chiTietDonHangArrayList.size() != 0) {
                    //Tạo ngày cho đơn hàng
                    Calendar calendar = Calendar.getInstance();
                    final int year = calendar.get(Calendar.YEAR);
                    final int month = calendar.get(Calendar.MONTH);
                    final int day = calendar.get(Calendar.DAY_OF_MONTH);

                    DatabaseReference myRef = database.child("HoaDon");

                    //Tạo đơn hàng
                    String maHoaDon = myRef.push().getKey();
                    HoaDon hoaDon = new HoaDon();
                    hoaDon.setMaHoaDon(maHoaDon);
                    hoaDon.setDonHang(donHang);
                    hoaDon.setNgayThang(day + "/" + (month + 1) + "/" + year);
                    hoaDon.setTong(tong);
                    hoaDon.setEmail(email);
                    hoaDon.setHoTen(hoTen);

                    myRef.child(maHoaDon).setValue(hoaDon).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            if (edtGiamGia.getError() == null) {
                                database.child("MaGiamGia").child(maGiamGia.getMaGiamGia())
                                        .child("soLuong").setValue(maGiamGia.getSoLuong() - 1);
                            }
                            database.child("Ban").child(String.valueOf(donHang.getMaBan())).child("chiTietDonHang").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Intent intent = new Intent(ChiTietThanhToan.this, ThuNgan.class);
                                    startActivity(intent);
                                    new CustomAlertDialog(ChiTietThanhToan.this,
                                            "Hãy chọn món khác",
                                            "Món đã có trong đơn hàng", CustomAlertDialog.ERROR);
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            new CustomAlertDialog(ChiTietThanhToan.this,
                                    "Lỗi",
                                    e.getMessage(), CustomAlertDialog.ERROR);
                        }
                    });
                } else {
                    new CustomAlertDialog(ChiTietThanhToan.this,
                            "Hãy chọn thêm món",
                            "Danh sách trống hoặc các món đã được đặt.\nHãy chọn thêm món.",
                            CustomAlertDialog.ERROR).show();
                }
            }
        });

        edtGiamGia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaGiamGiaDialog(ChiTietThanhToan.this, new MaGiamGiaDialog.MaGiamGiaDiaLogListener() {
                    @Override
                    public void itemClick(MaGiamGia giamGia) {
                        if(kiemTraMaGiamGia(giamGia)){
                            maGiamGia = giamGia;
                            int iGiamGia = giamGia.getPhanTramGiamGia() * tong / 100;
                            tong = tong - iGiamGia;
                            tvTong.setText("Tổng: " + formatter.format(tong) + " đ ( -" + formatter.format(iGiamGia) + " đ)");
                            edtGiamGia.setError(null);
                        }
                        else {
                            maGiamGia = null;
                            tongDonHang();
                            tvTong.setText("Tổng: " + formatter.format(tong) + " đ");
                        }
                        edtGiamGia.setText(giamGia.getTenMaGiamGia());
                        btnClear.setVisibility(View.VISIBLE);
                    }
                }).show();
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtGiamGia.setText(null);
                edtGiamGia.setError(null);
                maGiamGia = null;
                btnClear.setVisibility(View.INVISIBLE);
            }
        });
    }

    private boolean kiemTraMaGiamGia(MaGiamGia maGiamGia) {
        if (maGiamGia.getSoLuong() == 0) {
            edtGiamGia.setError("Mã giảm giá hết lượt sử dụng");
            return false;
        }
        if (maGiamGia.getGiaTriApDung() > tong) {
            edtGiamGia.setError("Giá trị áp dụng chưa đủ");
            return false;
        }
        Date todayDate = new Date();
        Date ngayBatDau = null;
        try {
            ngayBatDau = dateFormat.parse(maGiamGia.getNgayBatDau());
        } catch (ParseException e) {
            edtGiamGia.setError(e.getMessage());
            return false;
        }
        if (todayDate.compareTo(ngayBatDau) < 0) {
            edtGiamGia.setError("Mã giảm giá chưa được áp dụng");
            return false;
        }
        Date ngayKetThuc = null;
        try {
            ngayKetThuc = dateFormat.parse(maGiamGia.getNgayKetThuc());
        } catch (ParseException e) {
            edtGiamGia.setError(e.getMessage());
            return false;
        }
        if (todayDate.compareTo(ngayKetThuc) > 0) {
            edtGiamGia.setError("Mã giảm giá đã hết được áp dụng");
            return false;
        }
        return true;
    }


    //Lấy thông tin nhân viên
    private void getNhanVien() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String maNhanVien = user.getUid();

        //Lấy họ tên nhân viên
        database.child("NhanVien/" + maNhanVien).child("hoTen").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                hoTen = value;
                tvHoTen.setText("Họ tên: " + hoTen);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Lấy email nhân viên
        database.child("NhanVien/" + maNhanVien).child("email").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                email = value;
                tvEmail.setText("Email: " + email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
                    chiTietDonHangArrayList.clear();
                    chiTietDonHangArrayList.addAll(filter(donHang.getChiTietDonHang()));
                    chiTietThanhToanAdapter.notifyDataSetChanged();
                    tongDonHang();
                    tvTong.setText("Tổng: " + formatter.format(tong) + " đ");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //Tính tổng đơn hàng
    public void tongDonHang() {
        int tong = 0;
        for (ChiTietDonHang item : chiTietDonHangArrayList) {
            tong += item.getGia() * item.getSoLuong();
        }
        this.tong = tong;
    }

    //Lọc dữ liệu đơn hàng
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
        edtGiamGia = findViewById(R.id.edtGiamGia);
        tvTong = findViewById(R.id.tvTong);
        tvHoTen = findViewById(R.id.tvHoTen);
        tvEmail = findViewById(R.id.tvEmail);
        btnThanhToan = findViewById(R.id.btnThanhToan);
        actionBar = findViewById(R.id.actionBar);
        btnClear = findViewById(R.id.btnClear);
    }
}