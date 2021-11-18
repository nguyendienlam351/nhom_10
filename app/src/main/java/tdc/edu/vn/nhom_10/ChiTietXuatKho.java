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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import gun0912.tedbottompicker.TedBottomPicker;
import tdc.edu.vn.nhom_10.CustomView.CustomActionBar;
import tdc.edu.vn.nhom_10.model.LoaiMon;
import tdc.edu.vn.nhom_10.model.NguyenLieu;
import tdc.edu.vn.nhom_10.model.NhanVien;
import tdc.edu.vn.nhom_10.model.NhapKho;
import tdc.edu.vn.nhom_10.model.XuatKho;

public class ChiTietXuatKho extends AppCompatActivity {
    EditText edtSoLuong;
    TextView tvHoTen, tvEmail, tvTen, tvDonVi, tvGia, tvSoLuong, tvMoTa, tvNgay;
    CustomActionBar actionBar;
    Button btnThayDoi, btnXoa;
    XuatKho xuatKho;
    NguyenLieu nguyenLieu;
    DatabaseReference mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_xuat_kho);
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

        actionBar.setActionBarName("Chi tiết nhập kho");
        //
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String MaNhapKho = bundle.getString("MaXuatKho", "");
            getDataNV(MaNhapKho);
        }
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Gọi hàm delete
                openDiaLogDelete(xuatKho);
            }
        });
        btnThayDoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Gọi hàm update
                openDiaLogUpdateItem(xuatKho);
            }
        });

    }

    //Hàm update
    private void openDiaLogUpdateItem(XuatKho xuatKho) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
//        new AlertDialog.Builder(this)
        b.setTitle("Thay đổi");
        b.setMessage("Bạn có muốn thay đổi?");
        b.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mData = FirebaseDatabase.getInstance().getReference("XuatKho");
                int newSoLuong =Integer.parseInt(edtSoLuong.getText().toString());

                xuatKho.setSoLuong(newSoLuong);
                mData.child(String.valueOf(xuatKho.getMaXuatKho())).updateChildren(xuatKho.toMap());

                Intent intent = new Intent(getApplicationContext(), DanhSachXuatKho.class);
                startActivity(intent);
            }

        });
        b.setNegativeButton("Từ chối", null);
        b.setCancelable(false);
        b.show();
    }

    //Hàm delete
    private void openDiaLogDelete(XuatKho xuatKho) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
//        new AlertDialog.Builder(this)
        b.setTitle("Xoá");
        b.setMessage("Bạn có muốn xoá?");
        b.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mData.child(String.valueOf(xuatKho.getMaXuatKho())).removeValue();
                Intent intent = new Intent(getApplicationContext(), DanhSachNhapKho.class);
                startActivity(intent);
            }
        });
        b.setNegativeButton("Từ chối", null);
        b.setCancelable(false);
        b.show();
    }

    //Hàm lấy dữ liệu từ màn hình RV
    private void getDataNV(String MaXK) {
        mData = FirebaseDatabase.getInstance().getReference("XuatKho");
        mData.child(MaXK).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                XuatKho nXuatKho = snapshot.getValue(XuatKho.class);
                if (nXuatKho != null) {
                    xuatKho = nXuatKho;
//                    tvHoTen.setText(danhMucKho.get());
//                    tvEmail.setText(danhMucKho.get());
                    tvTen.setText(xuatKho.getTenXuatKho());
                    tvNgay.setText("Ngày sinh: " + xuatKho.getNgayXuatKho());
                    tvDonVi.setText("Đơn vị: " + xuatKho.getNguyenLieu().getDonVi());
                    NumberFormat formatter = new DecimalFormat("#,###,###");
                    tvGia.setText( "Giá: " + formatter.format(xuatKho.getNguyenLieu().getGia()) + " đ");
                    tvSoLuong.setText("Số lượng: " + xuatKho.getSoLuong());
                    tvMoTa.setText("Mô tả: " + xuatKho.getNguyenLieu().getMoTa());
//                    getDataNguyenLieu(String.valueOf(nguyenLieu.getSoLuong()));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void getDataNguyenLieu(String maNguyenLieu) {
        mData.child("NguyenLieu").child(maNguyenLieu).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                NguyenLieu nguyenLieu = snapshot.getValue(NguyenLieu.class);
                if(nguyenLieu!= null) {
                    tvSoLuong.setText(nguyenLieu.getSoLuong());
                }
                else {
                    tvSoLuong.setText("Lỗi số lượng");
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
        tvNgay = findViewById(R.id.tvNgay);
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