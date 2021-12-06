package tdc.edu.vn.nhom_10;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import gun0912.tedbottompicker.TedBottomPicker;
import tdc.edu.vn.nhom_10.CustomView.CustomActionBar;
import tdc.edu.vn.nhom_10.adapter.MonAnAdapter;
import tdc.edu.vn.nhom_10.adapter.MyRecylerViewNhanVien;
import tdc.edu.vn.nhom_10.model.NhanVien;

public class ThongTinNhanVien extends AppCompatActivity {
    TextView tvHoTen, tvSDT, tvCCCD, tvDiaChi, tvEmail, tvNgaySinh, tvChucVu;

    CustomActionBar actionBar;
    ImageView imgAnh;
    Button btnDoiMatKhau;

    ArrayList<NhanVien> list = new ArrayList<NhanVien>();
    ArrayList<String> chucVu = new ArrayList<String>();
    MyRecylerViewNhanVien adapter;
    NhanVien nhanVien;
    FirebaseAuth mAuth;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_nhan_vien);
        setControl();
        setEvent();
    }

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference("NhanVien");

    FirebaseDatabase data = FirebaseDatabase.getInstance();
    DatabaseReference mData = data.getReference("NhanVien");
    private void setEvent() {

        actionBar.setDelegation(new CustomActionBar.ActionBarDelegation() {
            @Override
            public void backOnClick() {
                finish();
            }
        });

        actionBar.setActionBarName("Thong Tin Nhan Vien");
        //


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        getDataNV(user.getUid());


        btnDoiMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThongTinNhanVien.this, DoiMatKhau.class);

                startActivity(intent);

            }
        });

    }

    //Hàm lấy dữ liệu từ màn hình RV
    private void getDataNV(String MaNV) {
        mData = FirebaseDatabase.getInstance().getReference("NhanVien");
        mData.child(MaNV).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                NhanVien nNhanVien = snapshot.getValue(NhanVien.class);
                if (nNhanVien != null) {
                    nhanVien = nNhanVien;
                    tvHoTen.setText(nhanVien.getHoTen());
                    tvSDT.setText(nhanVien.getSoDienThoai());
                    tvNgaySinh.setText("Ngày sinh: " + nhanVien.getNgaySinh());
                    tvCCCD.setText(nhanVien.getSoCCCD());
                    tvEmail.setText(nhanVien.getEmail());
                    tvDiaChi.setText(nhanVien.getDiaChi());
                    tvChucVu.setText(nhanVien.getChucVu());
                    getAnhNhanVien(nhanVien.getAnh());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    // lay anh
    private void getAnhNhanVien(String anh) {
        int dot = anh.lastIndexOf('.');
        String base = (dot == -1) ? anh : anh.substring(0, dot);
        String extension = (dot == -1) ? "" : anh.substring(dot + 1);
        try {
            final File file = File.createTempFile(base, extension);
            storageRef.child(anh).getFile(file)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                            imgAnh.setImageBitmap(bitmap);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setControl() {
        tvNgaySinh = findViewById(R.id.tvNgaySinh);
        tvHoTen = findViewById(R.id.tvHoTen);
        tvSDT = findViewById(R.id.tvSDT);
        tvCCCD = findViewById(R.id.tvCCCD);
        tvDiaChi = findViewById(R.id.tvDiaChi);
        tvChucVu = findViewById(R.id.tvChucVu);
        tvEmail = findViewById(R.id.tvEmail);
        btnDoiMatKhau = findViewById(R.id.btnDoiMatKhau);
        imgAnh = findViewById(R.id.imgHinh);
        actionBar = findViewById(R.id.actionBar);

    }

}
