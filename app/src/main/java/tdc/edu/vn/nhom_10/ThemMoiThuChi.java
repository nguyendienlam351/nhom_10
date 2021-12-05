package tdc.edu.vn.nhom_10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;

import tdc.edu.vn.nhom_10.CustomView.CustomActionBar;
import tdc.edu.vn.nhom_10.CustomView.CustomAlertDialog;
import tdc.edu.vn.nhom_10.model.ChiThu;
import tdc.edu.vn.nhom_10.model.NhanVien;

public class ThemMoiThuChi extends AppCompatActivity {
    Button btnThem;
    RadioButton rdThu,rdChi;
    TextView tvDate,tvTenNguoiNhap;
    EditText edSoTien,edMoTa;
    CustomActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_moi_thu_chi);
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

        actionBar.setActionBarName("Thêm mới thu chi");
        getTime();
        getTenNguoiNhap();
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (kiemtratrong() == true){
                    ThemMoi();
                }
            }
        });
    }
    private void ThemMoi(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Thêm");
        alertDialog.setMessage("Bạn có muốn thêm không ? ");
        alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ChiThu chiThu = new ChiThu();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ThuChi");
                String maThuChi = reference.push().getKey();
                String tenNguoiNhap = tvTenNguoiNhap.getText().toString().substring(17);
                String ngayNhap = tvDate.getText().toString().substring(12);
                chiThu.setMaThuChi(maThuChi);
                chiThu.setNgayNhap(ngayNhap);
                chiThu.setNguoiNhap(tenNguoiNhap);
                if(rdChi.isChecked()){
                    chiThu.setLoaiThuChi("Chi");
                }else if (rdChi.isChecked()){
                    chiThu.setLoaiThuChi("Thu");
                }else{
                    chiThu.setLoaiThuChi("Thu");
                }
                chiThu.setLoai("Nhập thủ công");
                chiThu.setSoTien(Integer.parseInt(edSoTien.getText().toString()));
                chiThu.setMoTa(edMoTa.getText().toString());
                reference.child(maThuChi).setValue(chiThu);
                finish();
            }
        });
        alertDialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alertDialog.show();
    }

    private void getTenNguoiNhap() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("NhanVien/"+user.getUid());
        reference.child("hoTen").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                tvTenNguoiNhap.setText(String.valueOf("Tên người nhập : "+value));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getTime() {
        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        int ngay = today.monthDay;
        int thang = today.month+1;
        int nam = today.year;
        if (ngay<10){
            tvDate.setText("Ngày nhập : 0"+ngay+"/"+thang+"/"+nam);
        }else{
            tvDate.setText("Ngày nhập : "+ngay+"/"+thang+"/"+nam);
        }
    }

    private void setControl() {
        btnThem = findViewById(R.id.btnThem);
        rdThu = findViewById(R.id.rdThu);
        rdChi = findViewById(R.id.rdChi);
        tvDate = findViewById(R.id.tvDate);
        tvTenNguoiNhap = findViewById(R.id.tvTenNguoiNhap);
        edSoTien = findViewById(R.id.edSoTien);
        edMoTa = findViewById(R.id.edMoTa);
        actionBar = findViewById(R.id.actionBar);
    }
    private boolean kiemtratrong(){
        boolean kiemtra = true;
        if (edMoTa.getText().toString().trim().length() == 0){
            edMoTa.setError("Nhập mô tả !");
            kiemtra = false;
        }
        if (edSoTien.getText().toString().trim().length() == 0){
            edSoTien.setError("Nhập số tiền !");
            kiemtra = false;
        }
        return kiemtra;
    }
}