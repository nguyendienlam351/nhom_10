package tdc.edu.vn.nhom_10;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import java.util.List;

import gun0912.tedbottompicker.TedBottomPicker;
import tdc.edu.vn.nhom_10.CustomView.CustomActionBar;
import tdc.edu.vn.nhom_10.CustomView.MinMaxFilter;
import tdc.edu.vn.nhom_10.adapter.MonAnAdapter;
import tdc.edu.vn.nhom_10.model.LoaiMon;
import tdc.edu.vn.nhom_10.model.MonAn;
import tdc.edu.vn.nhom_10.model.NhanVien;

public class ChiTietQLMon extends AppCompatActivity {

    EditText edtTenMon, edtGia, edtMoTa;
    Spinner spMon;
    Button btnThayDoi, btnXoa;
    ImageView imgHinh;
    CustomActionBar actionBar;
    MonAn monAn;
    DatabaseReference databaseReference;
    ArrayAdapter arrayAdapter;
    ArrayList<LoaiMon> loaiMonArrayList;
    MonAnAdapter monAnAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_ql_mon);
        setControl();
        setEvent();

    }

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference("Mon");
    //Xo?? Mon An

    FirebaseDatabase data = FirebaseDatabase.getInstance();
    DatabaseReference mData = data.getReference("Mon");


    private void setEvent() {

        // truy???n d??? li???u l??n rcv
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String maMon = bundle.getString("maMon", "");
            getDataMon(maMon);
        }


        // l???y d??? li???u l??n spiner
        databaseReference = FirebaseDatabase.getInstance().getReference();
        loaiMonArrayList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<LoaiMon>(this, android.R.layout.simple_spinner_item, loaiMonArrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMon.setAdapter(arrayAdapter);

        actionBar.setDelegation(new CustomActionBar.ActionBarDelegation() {
            @Override
            public void backOnClick() {
                finish();
            }
        });
        actionBar.setActionBarName("Chi ti???t m??n");

        //Ch???n h??nh ???nh
        imgHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
            }
        });

        // Xoa Mon
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //G???i h??m delete
                openDiaLogDelete(monAn);
            }
        });

        btnThayDoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //G???i h??m update
                openDiaLogUpdateItem(monAn);
            }
        });

//   gi???i h???n s??? l?????ng nh???p
        edtGia.setFilters(new InputFilter[]{new MinMaxFilter(1, Integer.MAX_VALUE)});
    }
// Ham update
    private void openDiaLogUpdateItem(MonAn monAn) {

        AlertDialog.Builder b = new AlertDialog.Builder(this);
//        new AlertDialog.Builder(this)
        b.setTitle("Thay ?????i");
        b.setMessage("B???n c?? mu???n thay ?????i?");
        b.setPositiveButton("?????ng ??", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mData = FirebaseDatabase.getInstance().getReference("Mon");
                String newTenMon = edtTenMon.getText().toString().trim();
                Integer newGia = Integer.parseInt(edtGia.getText().toString().trim());
                String newMoTa = edtMoTa.getText().toString().trim();
                String newMon = spMon.getSelectedItem().toString();


                monAn.setTenMon(newTenMon);
                monAn.setMoTa(newMoTa);
                monAn.setGia(newGia);
                monAn.setLoaiMon(newMon);
                mData.child(String.valueOf(monAn.getMaMon())).updateChildren(monAn.toMap());
                updateImage();

                Intent intent = new Intent(getApplicationContext(), QuanLyMon.class);
                startActivity(intent);
            }

        });
        b.setNegativeButton("T??? ch???i", null);
        b.setCancelable(false);
        b.show();
    }


    private void openDiaLogDelete(MonAn monAn) {
        new AlertDialog.Builder(this)
                .setTitle("Xo??")
                .setMessage("B???n c?? mu???n xo???")
                .setPositiveButton("?????ng ??", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseReference=FirebaseDatabase.getInstance().getReference("Mon");
                        databaseReference.child(String.valueOf(monAn.getMaMon())).removeValue();

                        Intent intent = new Intent(getApplicationContext(), QuanLyMon.class);
                        startActivity(intent);

                    }

                })
                .setNegativeButton("T??? ch???i",null)
                .setCancelable(false)
                .show();
    }

    // Ki???m tra ch???n ???nh
    private void requestPermission() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                OpenImagePicker();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(ChiTietQLMon.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
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
        TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(ChiTietQLMon.this)
                .setOnImageSelectedListener(listener)
                .create();
        tedBottomPicker.show(getSupportFragmentManager());
    }


    //H??m l???y d??? li???u t??? m??n h??nh RV
    private void getDataMon(String maMon) {

        databaseReference = FirebaseDatabase.getInstance().getReference("Mon");
        databaseReference.child(maMon).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MonAn nMon = snapshot.getValue(MonAn.class);
                if (nMon != null) {
                    monAn = nMon ;
                    edtTenMon.setText(monAn.getTenMon());
                    edtMoTa.setText(monAn.getMoTa());

                    getDataLoaiMon(monAn.getLoaiMon());

                    edtGia.setText(String.valueOf(monAn.getGia()));
                    getAnhMon(monAn.getAnh());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    // lay anh
    private void getAnhMon(String anh) {
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

    // lay du lieu tu loai mon
    private void getDataLoaiMon(String maLoai) {

        databaseReference.child("LoaiMon").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    LoaiMon loaiMon = dataSnapshot.getValue(LoaiMon.class);
                    if (loaiMon != null) {
                        loaiMonArrayList.add(loaiMon);
                        if(loaiMon.getMaLoaiMon().equals(maLoai))
                        {
                            spMon.setSelection(loaiMonArrayList.indexOf(loaiMon));
                        }

                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void updateImage() {
        //Th??m h??nh ???nh l??n firebase
        StorageReference mountainsRef = storageRef.child(monAn.getMaMon() + ".png");
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
                Toast.makeText(ChiTietQLMon.this, "L???i", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            }
        });
    }

    private void setControl() {
        edtTenMon = findViewById(R.id.edtTenMon);
        edtGia = findViewById(R.id.edtGia);
        edtMoTa = findViewById(R.id.edtMota);
        imgHinh = findViewById(R.id.imgHinh);
        spMon = findViewById(R.id.spMon);
        btnThayDoi = findViewById(R.id.btnThayDoi);
        btnXoa = findViewById(R.id.btnXoa);
        actionBar = findViewById(R.id.actionBar);


    }
}
