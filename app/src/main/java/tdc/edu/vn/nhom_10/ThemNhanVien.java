package tdc.edu.vn.nhom_10;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import gun0912.tedbottompicker.TedBottomPicker;
import tdc.edu.vn.nhom_10.CustomView.CustomActionBar;
import tdc.edu.vn.nhom_10.model.NhanVien;

public class ThemNhanVien extends AppCompatActivity {
    EditText edtHoTen, edtCCCD, edtEmail, edtSDT, edtDiaChi;
    TextView txtNgaySinh;
    String NgaySinh;
    Spinner spnChucVu;
    CustomActionBar actionBar;
    ImageButton btndate;
    ImageView imgHinh;
    Button btnThem;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    ArrayList<String> chucVu = new ArrayList<String>();
    int REQUEST_CODE_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_nhan_vien);

        setControl();
        setEvent();

    }

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference("NhanVien");

    private void setEvent() {
        actionBar.setDelegation(new CustomActionBar.ActionBarDelegation() {
            @Override
            public void backOnClick() {
                finish();
            }
        });

        actionBar.setActionBarName("Thêm nhân viên");
        //
        chucVu.add("Quản lý");
        chucVu.add("Phục vụ");
        chucVu.add("Pha chế");
        chucVu.add("Thu ngân");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, chucVu);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnChucVu.setAdapter(arrayAdapter);
        //Gọi hàm để trống dữ liệu
        edtHoTen.addTextChangedListener(watcher);
        edtCCCD.addTextChangedListener(watcher);
        edtEmail.addTextChangedListener(watcher);
        edtSDT.addTextChangedListener(watcher);
        edtDiaChi.addTextChangedListener(watcher);
        //Tạo ngày, tháng, năm sinh
        final Context context = this;
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        NgaySinh = day + "/" + (month + 1) + "/" + year;
        txtNgaySinh.setText("Ngày sinh: " + NgaySinh);
        btndate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        NgaySinh = day + "/" + (month + 1) + "/" + year;
                        txtNgaySinh.setText("Ngày sinh: " + NgaySinh);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = FirebaseDatabase.getInstance().getReference();
                String SDT = edtSDT.getText().toString().trim();
                String Email = edtEmail.getText().toString().trim();
                mAuth.createUserWithEmailAndPassword(Email, SDT).addOnCompleteListener(ThemNhanVien.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        String HoTen = edtHoTen.getText().toString().trim();
                        String CCCD = edtCCCD.getText().toString().trim();
                        String DiaChi = edtDiaChi.getText().toString().trim();
                        String ChucVu = spnChucVu.getSelectedItem().toString();
                        String maNV = mAuth.getUid();
                        String anh = mAuth.getUid() + ".png";
                        NhanVien nhanVien = new NhanVien(HoTen, SDT, NgaySinh, CCCD, Email, DiaChi, ChucVu, maNV, anh);
                        nhanVien.setMaNV(maNV);
                        databaseReference.child("NhanVien").child(maNV).setValue(nhanVien);
                        Toast.makeText(ThemNhanVien.this, mAuth.getUid(), Toast.LENGTH_SHORT).show();

                        //Thêm hình ảnh lên firebase
                        StorageReference mountainsRef = storageRef.child(maNV + ".png");
                        imgHinh.setDrawingCacheEnabled(true);
                        imgHinh.buildDrawingCache();
                        Bitmap bitmap = ((BitmapDrawable) imgHinh.getDrawable()).getBitmap();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] data = baos.toByteArray();

                        UploadTask uploadTask = mountainsRef.putBytes(data);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                Toast.makeText(ThemNhanVien.this, "Lỗi", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                                // ...
                            }
                        });
                        //Chuyển màn hình
                        Intent intent = new Intent(getApplicationContext(), QuanLyNhanVien.class);
                        startActivity(intent);
                    }
                });

            }
        });
        //Chọn hình ảnh
        imgHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
            }
        });

    }

    //Kiểm tra requestPermission
    private void requestPermission() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                OpenImagePicker();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(ThemNhanVien.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }

    private void OpenImagePicker() {
        TedBottomPicker.OnImageSelectedListener listener = new TedBottomPicker.OnImageSelectedListener() {
            @Override
            public void onImageSelected(Uri uri) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    imgHinh.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(ThemNhanVien.this)
                .setOnImageSelectedListener(listener)
                .create();
        tedBottomPicker.show(getSupportFragmentManager());
    }

    //Hàm không được để trống dữ liệu
    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String newHoTen = edtHoTen.getText().toString().trim();
            String newCCCD = edtCCCD.getText().toString().trim();
            String newEmail = edtEmail.getText().toString().trim();
            String newSDT = edtSDT.getText().toString().trim();
            String newDiaChi = edtDiaChi.getText().toString().trim();
            btnThem.setEnabled(!newHoTen.isEmpty() && !newCCCD.isEmpty() && !newEmail.isEmpty() && !newSDT.isEmpty() && !newDiaChi.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    //Ánh xạ
    private void setControl() {

        btndate = findViewById(R.id.btndate);
        edtHoTen = findViewById(R.id.edtHoTen);
        edtSDT = findViewById(R.id.edtSDT);
        txtNgaySinh = findViewById(R.id.txtNgaySinh);
        edtCCCD = findViewById(R.id.edtCCCD);
        edtEmail = findViewById(R.id.edtEmail);
        edtDiaChi = findViewById(R.id.edtDiaChi);
        spnChucVu = findViewById(R.id.spnChucVu);
        btnThem = findViewById(R.id.btnThem);
        imgHinh = findViewById(R.id.imgHinh);
        mAuth = FirebaseAuth.getInstance();
        btnThem.setEnabled(false);
        actionBar = findViewById(R.id.actionBar);

    }
}