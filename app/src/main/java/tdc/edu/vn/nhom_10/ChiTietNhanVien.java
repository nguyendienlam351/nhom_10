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
import tdc.edu.vn.nhom_10.model.NhanVien;

public class ChiTietNhanVien extends AppCompatActivity {
    EditText edtHoTen, edtSDT, edtCCCD, edtDiaChi, edtEmail;
    TextView txtNgaySinh;
    Spinner spnChucVu;
    CustomActionBar actionBar;
    ImageButton btndate;
    ImageView imgHinh;
    Button btnThayDoi, btnXoa;
    String NgaySinh;
    ArrayList<String> chucVu = new ArrayList<String>();
    NhanVien nhanVien;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_nhan_vien);
        setControl();
        setEvent();

    }

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference("NhanVien");

    //Xo?? user
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

        actionBar.setActionBarName("Chi ti???t nh??n vi??n");

        //
        chucVu.add("Qu???n l??");
        chucVu.add("Ph???c v???");
        chucVu.add("Pha ch???");
        chucVu.add("Thu ng??n");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, chucVu);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnChucVu.setAdapter(arrayAdapter);
        //
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String MaNV = bundle.getString("MaNV", "");
            getDataNV(MaNV);
        }
        //T???o ng??y th??ng n??m sinh
        final Context context = this;
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        btndate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        NgaySinh = day + "/" + (month + 1) + "/" + year;
                        txtNgaySinh.setText("Ng??y sinh: " + NgaySinh);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //G???i h??m delete
                openDiaLogDelete(nhanVien);
            }
        });
        btnThayDoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //G???i h??m update
                openDiaLogUpdateItem(nhanVien);
            }
        });
        //Ch???n h??nh ???nh
        imgHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
            }
        });

    }

    //Ki???m tra requestPermission
    private void requestPermission() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                OpenImagePicker();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(ChiTietNhanVien.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
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
        TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(ChiTietNhanVien.this)
                .setOnImageSelectedListener(listener)
                .create();
        tedBottomPicker.show(getSupportFragmentManager());
    }

    //H??m update
    private void openDiaLogUpdateItem(NhanVien nhanVien) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
//        new AlertDialog.Builder(this)
        b.setTitle("Thay ?????i");
        b.setMessage("B???n c?? mu???n thay ?????i?");
        b.setPositiveButton("?????ng ??", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mData = FirebaseDatabase.getInstance().getReference("NhanVien");
                String newHoTen = edtHoTen.getText().toString().trim();
                String newSDT = edtSDT.getText().toString().trim();
                String newCCCD = edtCCCD.getText().toString().trim();
                String newEmail = edtEmail.getText().toString().trim();
                String newDiaChi = edtDiaChi.getText().toString().trim();
                String newChucVu = spnChucVu.getSelectedItem().toString();
                int dot = txtNgaySinh.getText().toString().lastIndexOf(" ");
                String newNgaySinh = (dot == -1) ? "" : txtNgaySinh.getText().toString().substring(dot + 1);

                nhanVien.setHoTen(newHoTen);
                nhanVien.setSoDienThoai(newSDT);
                nhanVien.setSoCCCD(newCCCD);
                nhanVien.setEmail(newEmail);
                nhanVien.setDiaChi(newDiaChi);
                nhanVien.setChucVu(newChucVu);
                nhanVien.setNgaySinh(newNgaySinh);
                mData.child(String.valueOf(nhanVien.getMaNV())).updateChildren(nhanVien.toMap());

                updateImage();
                Intent intent = new Intent(getApplicationContext(), QuanLyNhanVien.class);
                startActivity(intent);
            }

        });
        b.setNegativeButton("T??? ch???i", null);
        b.setCancelable(false);
        b.show();
    }

    private void updateImage() {
        //Th??m h??nh ???nh l??n firebase
        StorageReference mountainsRef = storageRef.child(nhanVien.getMaNV() + ".png");
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
                Toast.makeText(ChiTietNhanVien.this, "L???i", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            }
        });
    }

    //H??m delete
    private void openDiaLogDelete(NhanVien nhanVien) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
//        new AlertDialog.Builder(this)
        b.setTitle("Xo??");
        b.setMessage("B???n c?? mu???n xo???");
        b.setPositiveButton("?????ng ??", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                User.delete();
                storageRef.child(String.valueOf(nhanVien.getAnh())).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        mData.child(String.valueOf(nhanVien.getMaNV())).removeValue();
                        Intent intent = new Intent(getApplicationContext(), QuanLyNhanVien.class);
                        startActivity(intent);
                    }
                });
            }
        });
        b.setNegativeButton("T??? ch???i", null);
        b.setCancelable(false);
        b.show();
    }

    //H??m l???y d??? li???u t??? m??n h??nh RV
    private void getDataNV(String MaNV) {
        mData = FirebaseDatabase.getInstance().getReference("NhanVien");
        mData.child(MaNV).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                NhanVien nNhanVien = snapshot.getValue(NhanVien.class);
                if (nNhanVien != null) {
                    nhanVien = nNhanVien;
                    edtHoTen.setText(nhanVien.getHoTen());
                    edtSDT.setText(nhanVien.getSoDienThoai());
                    txtNgaySinh.setText("Ng??y sinh: " + nhanVien.getNgaySinh());
                    edtCCCD.setText(nhanVien.getSoCCCD());
                    edtEmail.setText(nhanVien.getEmail());
                    edtDiaChi.setText(nhanVien.getDiaChi());
                    getAnhNhanVien(nhanVien.getAnh());
                    for (int i = 0; i < chucVu.size(); i++) {
                        if (chucVu.get(i).equals(nhanVien.getChucVu())) {
                            spnChucVu.setSelection(i);
                        }
                    }
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
                            imgHinh.setImageBitmap(bitmap);
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
        txtNgaySinh = findViewById(R.id.txtNgaySinh);
        btndate = findViewById(R.id.btndate);
        btnThayDoi = findViewById(R.id.btnThayDoi);
        edtHoTen = findViewById(R.id.edtHoTen);
        edtSDT = findViewById(R.id.edtSDT);
        edtCCCD = findViewById(R.id.edtCCCD);
        edtDiaChi = findViewById(R.id.edtDiaChi);
        spnChucVu = findViewById(R.id.spnChucVu);
        edtEmail = findViewById(R.id.edtEmail);
        btnXoa = findViewById(R.id.btnXoa);
        imgHinh = findViewById(R.id.imgHinh);
        edtEmail.setFocusable(false);
        edtSDT.setFocusable(false);
        actionBar = findViewById(R.id.actionBar);
    }
}