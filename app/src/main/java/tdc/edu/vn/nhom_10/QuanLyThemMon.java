package tdc.edu.vn.nhom_10;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
import tdc.edu.vn.nhom_10.CustomView.MinMaxFilter;
import tdc.edu.vn.nhom_10.model.LoaiMon;
import tdc.edu.vn.nhom_10.model.MonAn;

public class QuanLyThemMon extends AppCompatActivity {

    EditText edtTenMon, edtGia, edtMoTa;
    Spinner spMon;
    Button btnThem;
    ImageView imgHinh;
    CustomActionBar actionBar;
    DatabaseReference databaseReference;
    int viTriLoai= 0;
    ArrayList<LoaiMon> loaiMonArrayList;
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_ql_mon);
        setControl();
        setEvent();
    }
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference("Mon");

    private void setEvent() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        actionBar.setDelegation(new CustomActionBar.ActionBarDelegation() {
            @Override
            public void backOnClick() {
                finish();
            }
        });
        actionBar.setActionBarName("Th??m m??n");

        loaiMonArrayList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<LoaiMon>(this, android.R.layout.simple_spinner_item, loaiMonArrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMon.setAdapter(arrayAdapter);
        spMon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viTriLoai = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getDataLoaiMon();

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = FirebaseDatabase.getInstance().getReference();
                MonAn monAn = new MonAn();
                monAn.setTenMon(edtTenMon.getText().toString().trim());
                monAn.setGia(Integer.parseInt(edtGia.getText().toString()));

                monAn.setLoaiMon(loaiMonArrayList.get(viTriLoai).getMaLoaiMon());
                monAn.setMoTa(edtMoTa.getText().toString());

                String mamon = databaseReference.child("Mon").push().getKey();
                monAn.setMaMon(mamon);
                monAn.setAnh(monAn.getMaMon() + ".png");
                databaseReference.child("Mon").child(mamon).setValue(monAn);
                databaseReference.child("Mon").push();

                // Them hinh Anh

                StorageReference mountainsRef = storageRef.child( mamon + ".png");
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
                        Toast.makeText(QuanLyThemMon.this, "L???i", Toast.LENGTH_SHORT).show();

                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                        // ...
                        Toast.makeText(QuanLyThemMon.this, "Th??nh C??ng", Toast.LENGTH_SHORT).show();

                        // quay man hinh ve qlm
                        Intent intent = new Intent(QuanLyThemMon.this, QuanLyMon.class);
                        startActivity(intent);

                    }
                });
            }
        });

        // lay anh tu thu vien
        imgHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
            }
        });

// gioi han gia tri nhap
        edtGia.setFilters(new InputFilter[]{new MinMaxFilter(1, Integer.MAX_VALUE)});


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
                Toast.makeText(QuanLyThemMon.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
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
        TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(QuanLyThemMon.this)
                .setOnImageSelectedListener(listener)
                .create();
        tedBottomPicker.show(getSupportFragmentManager());
    }

    // lay du lieu tu loai mon
    private void getDataLoaiMon() {
        databaseReference.child("LoaiMon").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    LoaiMon loaiMon = dataSnapshot.getValue(LoaiMon.class);
                    if (loaiMon != null) {
                        loaiMonArrayList.add(loaiMon);
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private void setControl() {
        edtTenMon = findViewById(R.id.edtTenMon);
        edtGia = findViewById(R.id.edtGia);
        edtMoTa = findViewById(R.id.edtMota);
        imgHinh = findViewById(R.id.imgHinh);
        spMon = findViewById(R.id.spMon);
        btnThem = findViewById(R.id.btnThem);
        actionBar = findViewById(R.id.actionBar);

    }

}
