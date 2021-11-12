package tdc.edu.vn.nhom_10;

import android.Manifest;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import gun0912.tedbottompicker.TedBottomPicker;
import tdc.edu.vn.nhom_10.CustomView.CustomActionBar;
import tdc.edu.vn.nhom_10.model.NguyenLieu;
import tdc.edu.vn.nhom_10.model.NhanVien;

public class ChiTietNhapKho extends AppCompatActivity {
    EditText edtSoLuong;
    TextView tvHoTen, tvEmail, tvTen, tvDonVi, tvGia, tvSoLuong, tvMoTa;
    CustomActionBar actionBar;
    Button btnThayDoi, btnXoa;
    NguyenLieu nguyenLieu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_nhap_kho);
        setControl();
        setEvent();

    }

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference("NhanVien");

    //Xoá user
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser User = firebaseAuth.getCurrentUser();

    FirebaseDatabase data = FirebaseDatabase.getInstance();
    DatabaseReference mData = data.getReference("NhanVien");

    private void setEvent() {
        actionBar.setDelegation(new CustomActionBar.ActionBarDelegation() {
            @Override
            public void backOnClick() {
                finish();
            }
        });

        actionBar.setActionBarName("Chi tiết nhân viên");
        //
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String MaDanhMucKho = bundle.getString("MaDanhMucKho", "");
            getDataNV(MaDanhMucKho);
        }
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Gọi hàm delete
                openDiaLogDelete(nguyenLieu);
            }
        });
        btnThayDoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Gọi hàm update
                //openDiaLogUpdateItem(danhMucKho);
            }
        });

    }

    //Hàm update
//    private void openDiaLogUpdateItem(NhanVien nhanVien) {
//        AlertDialog.Builder b = new AlertDialog.Builder(this);
////        new AlertDialog.Builder(this)
//        b.setTitle("Thay đổi");
//        b.setMessage("Bạn có muốn thay đổi?");
//        b.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                mData = FirebaseDatabase.getInstance().getReference("NhanVien");
//                String newHoTen = edtHoTen.getText().toString().trim();
//                String newSDT = edtSDT.getText().toString().trim();
//                String newCCCD = edtCCCD.getText().toString().trim();
//                String newEmail = edtEmail.getText().toString().trim();
//                String newDiaChi = edtDiaChi.getText().toString().trim();
//                String newChucVu = spnChucVu.getSelectedItem().toString();
//                int dot = txtNgaySinh.getText().toString().lastIndexOf(" ");
//                String newNgaySinh = (dot == -1) ? "" : txtNgaySinh.getText().toString().substring(dot + 1);
//
//                nhanVien.setHoTen(newHoTen);
//                nhanVien.setSoDienThoai(newSDT);
//                nhanVien.setSoCCCD(newCCCD);
//                nhanVien.setEmail(newEmail);
//                nhanVien.setDiaChi(newDiaChi);
//                nhanVien.setChucVu(newChucVu);
//                nhanVien.setNgaySinh(newNgaySinh);
//                mData.child(String.valueOf(nhanVien.getMaNV())).updateChildren(nhanVien.toMap());
//
//                Intent intent = new Intent(getApplicationContext(), QuanLyNhanVien.class);
//                startActivity(intent);
//            }
//
//        });
//        b.setNegativeButton("Từ chối", null);
//        b.setCancelable(false);
//        b.show();
//    }

    //Hàm delete
    private void openDiaLogDelete(NguyenLieu nguyenLieu) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
//        new AlertDialog.Builder(this)
        b.setTitle("Xoá");
        b.setMessage("Bạn có muốn xoá?");
        b.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                        mData.child(String.valueOf(nguyenLieu.getMaNL())).removeValue();
                        Intent intent = new Intent(getApplicationContext(), DanhSachNhapKho.class);
                        startActivity(intent);
            }
        });
        b.setNegativeButton("Từ chối", null);
        b.setCancelable(false);
        b.show();
    }

    //Hàm lấy dữ liệu từ màn hình RV
    private void getDataNV(String MaNV) {
        mData = FirebaseDatabase.getInstance().getReference("DanhMucKho");
        mData.child(MaNV).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                NguyenLieu nNguyenLieu = snapshot.getValue(NguyenLieu.class);
                if (nNguyenLieu != null) {
                    nguyenLieu = nNguyenLieu;
//                    tvHoTen.setText(danhMucKho.get());
//                    tvEmail.setText(danhMucKho.get());
                    tvTen.setText(nguyenLieu.getTenNL());
                    tvDonVi.setText("Ngày sinh: " + nguyenLieu.getDonVi());
                    tvGia.setText(nguyenLieu.getGia());
//                    tvSoLuong.setText(nguyenLieu.getSoLuong());
                    tvMoTa.setText(nguyenLieu.getMoTa());
                }
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
        tvDonVi = findViewById(R.id.tvDonVi);
        tvGia = findViewById(R.id.tvGia);
        tvSoLuong = findViewById(R.id.tvSoLuong);
        tvMoTa = findViewById(R.id.tvMoTa);
        edtSoLuong = findViewById(R.id.edtSoLuong);
        btnXoa = findViewById(R.id.btnXoa);
        btnThayDoi = findViewById(R.id.btnThayDoi);
        actionBar = findViewById(R.id.actionBar);
    }
}