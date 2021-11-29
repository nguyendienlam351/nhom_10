package tdc.edu.vn.nhom_10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import tdc.edu.vn.nhom_10.CustomView.CustomActionBar;
import tdc.edu.vn.nhom_10.model.ChiThu;

public class ChiTietThuChi extends AppCompatActivity {
    CustomActionBar actionBar;
    Button btnHuy, btnThayDoi,btnXoa;
    RadioButton rdThu,rdChi;
    TextView tvDate,tvTenNguoiNhap;
    EditText edSoTien,edMoTa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_thu_chi);
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

        actionBar.setActionBarName("Chi tiết thu chi");
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String maTC = bundle.getString("maTC");
        getThuChi(maTC);
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Xoa(maTC);
            }
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnThayDoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThayDoi(maTC);
            }
        });
    }
    private void ThayDoi(String maTC){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Thay đổi");
        alertDialog.setMessage("Bạn có muốn thay đổi không ? ");
        alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ChiThu chiThu = new ChiThu();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ThuChi");
                String tenNguoiNhap = tvTenNguoiNhap.getText().toString().substring(17);
                String ngayNhap = tvDate.getText().toString().substring(12);
                chiThu.setMaThuChi(maTC);
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
                reference.child(maTC).setValue(chiThu);
                Intent intent = new Intent(getApplicationContext(),ThuNgan.class);
                Bundle bundle = new Bundle();
                bundle.putInt("manhinh",1);
                intent.putExtras(bundle);
                startActivity(intent);
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

    private void getThuChi(String maTC) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ThuChi/"+maTC);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ChiThu chiThu = snapshot.getValue(ChiThu.class);
                if (chiThu != null){
                    tvDate.setText("Ngày nhập : "+chiThu.getNgayNhap());
                    tvTenNguoiNhap.setText("Tên người nhập : "+chiThu.getNguoiNhap());
                    if(chiThu.getLoaiThuChi().compareTo("Chi") == 0){
                        rdChi.setChecked(true);
                    }else{
                        rdThu.setChecked(true);
                    }
                    edSoTien.setText(String.valueOf(chiThu.getSoTien()));
                    edMoTa.setText(chiThu.getMoTa());
                    if(chiThu.getLoai().compareTo("Nhập thủ công") != 0){
                        btnThayDoi.setEnabled(false);
                        btnXoa.setEnabled(false);
                        edMoTa.setEnabled(false);
                        edSoTien.setEnabled(false);
                        rdThu.setEnabled(false);
                        rdChi.setEnabled(false);
                    }
                }
                else {
                    Intent intent = new Intent(getApplicationContext(),ThuNgan.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("manhinh",2);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void Xoa(String maTC){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Xóa");
        alertDialog.setMessage("Bạn có muốn xóa không ? ");
        alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ThuChi");
                reference.child(maTC).removeValue();
                Intent intent = new Intent(getApplicationContext(),ThuNgan.class);
                Bundle bundle = new Bundle();
                bundle.putInt("manhinh",2);
                intent.putExtras(bundle);
                startActivity(intent);
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

    private void setControl() {
        actionBar = findViewById(R.id.actionBar);
        btnHuy = findViewById(R.id.btnHuy);
        btnXoa = findViewById(R.id.btnXoa);
        btnThayDoi = findViewById(R.id.btnThayDoi);
        rdThu = findViewById(R.id.rdThu);
        rdChi = findViewById(R.id.rdChi);
        tvDate = findViewById(R.id.tvDate);
        tvTenNguoiNhap = findViewById(R.id.tvTenNguoiNhap);
        edSoTien = findViewById(R.id.edSoTien);
        edMoTa = findViewById(R.id.edMoTa);
    }
}