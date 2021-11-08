//package tdc.edu.vn.nhom_10;
//
//import android.Manifest;
//import android.app.AlertDialog;
//import android.app.DatePickerDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.drawable.BitmapDrawable;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.view.View;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.DatePicker;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.storage.FileDownloadTask;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;
//import com.gun0912.tedpermission.PermissionListener;
//import com.gun0912.tedpermission.normal.TedPermission;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.List;
//
//import gun0912.tedbottompicker.TedBottomPicker;
//import tdc.edu.vn.nhom_10.CustomView.CustomActionBar;
//import tdc.edu.vn.nhom_10.model.DanhMucKho;
//import tdc.edu.vn.nhom_10.model.NhanVien;
//
//public class ChiTietNhapKho extends AppCompatActivity {
//    EditText edtSoLuong;
//    TextView tvHoTen, tvEmail, tvTen, tvDonVi, tvGia, tvSoLuong, tvMoTa;
//    CustomActionBar actionBar;
//    Button btnThayDoi, btnXoa;
//    DanhMucKho danhMucKho;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_chi_tiet_nhan_vien);
//        setControl();
//        setEvent();
//
//    }
//
//    FirebaseStorage storage = FirebaseStorage.getInstance();
//    StorageReference storageRef = storage.getReference("NhanVien");
//
//    //Xoá user
//    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//    FirebaseUser User = firebaseAuth.getCurrentUser();
//
//    FirebaseDatabase data = FirebaseDatabase.getInstance();
//    DatabaseReference mData = data.getReference("NhanVien");
//
//    private void setEvent() {
//        actionBar.setDelegation(new CustomActionBar.ActionBarDelegation() {
//            @Override
//            public void backOnClick() {
//                finish();
//            }
//        });
//
//        actionBar.setActionBarName("Chi tiết nhân viên");
//        //
//        Intent intent = getIntent();
//        Bundle bundle = intent.getExtras();
//        if (bundle != null) {
//            String MaNV = bundle.getString("MaNV", "");
//            getDataNV(MaNV);
//        }
//        btnXoa.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Gọi hàm delete
//                openDiaLogDelete(danhMucKho);
//            }
//        });
//        btnThayDoi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Gọi hàm update
//                openDiaLogUpdateItem(danhMucKho);
//            }
//        });
//
//    }
//
//    //Hàm update
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
//                updateImage();
//                Intent intent = new Intent(getApplicationContext(), QuanLyNhanVien.class);
//                startActivity(intent);
//            }
//
//        });
//        b.setNegativeButton("Từ chối", null);
//        b.setCancelable(false);
//        b.show();
//    }
//
//    private void updateImage() {
//        //Thêm hình ảnh lên firebase
//        StorageReference mountainsRef = storageRef.child(nhanVien.getMaNV() + ".png");
//        imgHinh.setDrawingCacheEnabled(true);
//        imgHinh.buildDrawingCache();
//        Bitmap bitmap = ((BitmapDrawable) imgHinh.getDrawable()).getBitmap();
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
//        byte[] data = baos.toByteArray();
//
//        UploadTask uploadTask = mountainsRef.putBytes(data);
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle unsuccessful uploads
//                Toast.makeText(ChiTietNhanVien.this, "Lỗi", Toast.LENGTH_SHORT).show();
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//            }
//        });
//    }
//
//    //Hàm delete
//    private void openDiaLogDelete(DanhMucKho danhMucKho) {
//        AlertDialog.Builder b = new AlertDialog.Builder(this);
////        new AlertDialog.Builder(this)
//        b.setTitle("Xoá");
//        b.setMessage("Bạn có muốn xoá?");
//        b.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//                        mData.child(String.valueOf(danhMucKho.getMaDanhMucKho())).removeValue();
//                        Intent intent = new Intent(getApplicationContext(), DanhSachNhapKho.class);
//                        startActivity(intent);
//            }
//        });
//        b.setNegativeButton("Từ chối", null);
//        b.setCancelable(false);
//        b.show();
//    }
//
//    //Hàm lấy dữ liệu từ màn hình RV
//    private void getDataNV(String MaNV) {
//        mData = FirebaseDatabase.getInstance().getReference("NhanVien");
//        mData.child(MaNV).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                DanhMucKho nDanhMucKho = snapshot.getValue(DanhMucKho.class);
//                if (nDanhMucKho != null) {
//                    danhMucKho = nDanhMucKho;
//                    tvHoTen.setText(danhMucKho.get());
//                    tvEmail.setText(danhMucKho.get());
//                    tvTen.setText(danhMucKho.getTen());
//                    tvDonVi.setText("Ngày sinh: " + danhMucKho.getDonVi());
//                    tvGia.setText(danhMucKho.getGia());
//                    tvSoLuong.setText(danhMucKho.getSoLuong());
//                    tvMoTa.setText(danhMucKho.getMoTa());
//                    getAnhNhanVien(nhanVien.getAnh());
//                    for (int i = 0; i < chucVu.size(); i++) {
//                        if (chucVu.get(i).equals(nhanVien.getChucVu())) {
//                            spnChucVu.setSelection(i);
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });
//
//
//    }
//
//    //tẽttt
//    private void getAnhNhanVien(String anh) {
//        int dot = anh.lastIndexOf('.');
//        String base = (dot == -1) ? anh : anh.substring(0, dot);
//        String extension = (dot == -1) ? "" : anh.substring(dot + 1);
//        try {
//            final File file = File.createTempFile(base, extension);
//            storageRef.child(anh).getFile(file)
//                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
//                            imgHinh.setImageBitmap(bitmap);
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                        }
//                    });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void setControl() {
//        tvHoTen = findViewById(R.id.tvHoTen);
//        tvEmail = findViewById(R.id.tvEmail);
//        tvTen = findViewById(R.id.tvTen);
//        tvDonVi = findViewById(R.id.tvDonVi);
//        tvGia = findViewById(R.id.tvGia);
//        tvSoLuong = findViewById(R.id.tvSoLuong);
//        tvMoTa = findViewById(R.id.tvMoTa);
//        edtSoLuong = findViewById(R.id.edtSoLuong);
//        btnXoa = findViewById(R.id.btnXoa);
//        btnThayDoi = findViewById(R.id.btnThayDoi);
//        actionBar = findViewById(R.id.actionBar);
//    }
//}