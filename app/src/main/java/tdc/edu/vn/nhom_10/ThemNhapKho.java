package tdc.edu.vn.nhom_10;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import gun0912.tedbottompicker.TedBottomPicker;
import tdc.edu.vn.nhom_10.CustomView.CustomActionBar;
import tdc.edu.vn.nhom_10.model.ChiThu;
import tdc.edu.vn.nhom_10.model.HoatDongTrongNgay;
import tdc.edu.vn.nhom_10.model.NguyenLieu;
import tdc.edu.vn.nhom_10.model.NhanVien;
import tdc.edu.vn.nhom_10.model.NhapKho;

public class ThemNhapKho extends AppCompatActivity {
    EditText edtSoLuong;
    TextView tvHoTen, tvEmail, tvTen, tvDonVi, tvGia, tvSoLuong, tvMoTa, tvNgay;
    CustomActionBar actionBar;
    Button btnThem;
    String Ngay;
    NguyenLieu nguyenLieu;
    DatabaseReference mData;
    String hoTen;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_nhap_kho);
        setControl();
        setEvent();

    }

    private void setEvent() {
        mData = FirebaseDatabase.getInstance().getReference();
        actionBar.setDelegation(new CustomActionBar.ActionBarDelegation() {
            @Override
            public void backOnClick() {
                Intent intent = new Intent(getApplicationContext(), DanhSachNguyenLieuNhap.class);
                startActivity(intent);
            }
        });

        actionBar.setActionBarName("Th??m nh???p kho");

        //
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String MaNguyenLieu = bundle.getString("MaNL", "");
            getDataNguyenLieu(MaNguyenLieu);
        }
        //T???o ng??y, th??ng, n??m sinh
        final Context context = this;
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        if (day < 10) {
            Ngay = "0" + day + "/" + (month + 1) + "/" + year;
        } else {
            Ngay = day + "/" + (month + 1) + "/" + year;
        }

        tvNgay.setText("Ng??y: " + Ngay);
        //G???i h??m NV
        getNhanVien();
        //
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData = FirebaseDatabase.getInstance().getReference();
                String HoTen = hoTen;
                String Email = email;
                String Ten = tvTen.getText().toString().trim();
                int SoLuong = Integer.parseInt(edtSoLuong.getText().toString().trim());
                String maNhapKho = mData.push().getKey();
                NhapKho nhapKho = new NhapKho(nguyenLieu, HoTen, Email, maNhapKho, Ten, Ngay, SoLuong, nguyenLieu.getDonVi());
                nhapKho.setMaNhapKho(maNhapKho);
                HoatDongTrongNgay hoatDongTrongNgay = new HoatDongTrongNgay("Nh???p kho", Ngay, "???? nh???p " + SoLuong + nguyenLieu.getDonVi() + " " + nguyenLieu.getTenNL());
                mData.child("NhapKho").child(maNhapKho).setValue(nhapKho).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        mData.child("NguyenLieu").child(nguyenLieu.getMaNL())
                                .child("soLuong").setValue(SoLuong + nguyenLieu.getSoLuong()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ThuChi");
                                ChiThu chiThu = new ChiThu();
                                String maThuChi = reference.push().getKey();
                                chiThu.setMaThuChi(maThuChi);
                                chiThu.setLoaiThuChi("Chi");
                                chiThu.setLoai("Nh???p t??? ?????ng");
                                chiThu.setNgayNhap(Ngay);
                                chiThu.setNguoiNhap(hoTen);
                                chiThu.setMoTa("Nh???p " + edtSoLuong.getText() + " " + nguyenLieu.getDonVi() + " " + nguyenLieu.getTenNL());
                                int tien = nguyenLieu.getGia() * nhapKho.getSoLuong();
                                chiThu.setSoTien(tien);
                                reference.child(maThuChi).setValue(chiThu).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        //Chuy???n m??n h??nh
                                        Intent intent = new Intent(getApplicationContext(), DanhSachNhapKho.class);
                                        startActivity(intent);
                                    }
                                });

                                //Chuy???n m??n h??nh
                                mData.child("HoatDongTrongNgay").push().setValue(hoatDongTrongNgay).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Intent intent = new Intent(getApplicationContext(), DanhSachNhapKho.class);
                                        startActivity(intent);
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                mData.child("NhapKho").child(maNhapKho).removeValue();
                                Toast.makeText(ThemNhapKho.this, e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ThemNhapKho.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    //H??m l???y d??? li???u t??? m??n h??nh RV
    private void getDataNguyenLieu(String MaNguyenLieu) {
        mData.child("NguyenLieu").child(MaNguyenLieu).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                NguyenLieu nNguyenLieu = snapshot.getValue(NguyenLieu.class);
                if (nNguyenLieu != null) {
                    nguyenLieu = nNguyenLieu;
                    tvTen.setText(nguyenLieu.getTenNL());
                    tvDonVi.setText("????n v???: " + nguyenLieu.getDonVi());
                    NumberFormat formatter = new DecimalFormat("#,###,###");
                    tvGia.setText("Gi??: " + formatter.format(nguyenLieu.getGia()) + " ??");
                    tvSoLuong.setText("S??? l?????ng: " + nguyenLieu.getSoLuong());
                    tvMoTa.setText("M?? t???: " + nguyenLieu.getMoTa());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void getNhanVien() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String maNhanVien = user.getUid();

        mData.child("NhanVien/" + maNhanVien).child("hoTen").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                hoTen = value;
                tvHoTen.setText("H??? t??n: " + hoTen);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mData.child("NhanVien/" + maNhanVien).child("email").addValueEventListener(new ValueEventListener() {
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

    private void setControl() {
        tvHoTen = findViewById(R.id.tvHoTen);
        tvEmail = findViewById(R.id.tvEmail);
        tvTen = findViewById(R.id.tvTen);
        tvNgay = findViewById(R.id.tvNgay);
        tvDonVi = findViewById(R.id.tvDonVi);
        tvGia = findViewById(R.id.tvGia);
        tvSoLuong = findViewById(R.id.tvSoLuong);
        tvMoTa = findViewById(R.id.tvMoTa);
        edtSoLuong = findViewById(R.id.edtSoLuong);
        btnThem = findViewById(R.id.btnThem);
        actionBar = findViewById(R.id.actionBar);
    }
}