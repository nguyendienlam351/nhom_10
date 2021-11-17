package tdc.edu.vn.nhom_10;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import gun0912.tedbottompicker.TedBottomPicker;
import tdc.edu.vn.nhom_10.CustomView.CustomActionBar;
import tdc.edu.vn.nhom_10.model.MonAn;

public class QuanLyThemMon extends AppCompatActivity {

    EditText edtTenNV, edtGia, edtMoTa;
    Spinner spMon;
    Button btnThem;
    ImageView imgHinh;
    CustomActionBar actionBar;
    DatabaseReference databaseReference;

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
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = FirebaseDatabase.getInstance().getReference();
                MonAn monAn = new MonAn();
                monAn.setTenMon(edtTenNV.getText().toString().trim());
                monAn.setGia(Integer.parseInt(edtGia.getText().toString()));
                monAn.setLoaiMon(spMon.getSelectedItem().toString());
                monAn.setMoTa(edtMoTa.getText().toString());
                monAn.setAnh(imgHinh.toString() + ".png");
                String mamon = databaseReference.child("Mon").push().getKey();
                monAn.setMaMon(mamon);
                databaseReference.child("Mon").child(mamon).setValue(monAn);
                databaseReference.child("Mon").push();

                actionBar.setDelegation(new CustomActionBar.ActionBarDelegation() {
                    @Override
                    public void backOnClick() {
                        finish();
                    }
                });


                // Them hinh Anh
                Calendar calendar = Calendar.getInstance();
                StorageReference mountainsRef = storageRef.child(calendar.getTimeInMillis() + ".png");
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
                        Toast.makeText(QuanLyThemMon.this, "Lỗi", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                        // ...
                        Toast.makeText(QuanLyThemMon.this, "Thành Công", Toast.LENGTH_SHORT).show();

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


    private void setControl() {
        edtTenNV = findViewById(R.id.edtTenNV);
        edtGia = findViewById(R.id.edtGia);
        edtMoTa = findViewById(R.id.edtMota);
        imgHinh = findViewById(R.id.imgHinh);
        spMon = findViewById(R.id.spMon);
        btnThem = findViewById(R.id.btnThem);

    }

}
