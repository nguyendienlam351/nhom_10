package tdc.edu.vn.nhom_10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import tdc.edu.vn.nhom_10.CustomView.CustomActionBar;
import tdc.edu.vn.nhom_10.CustomView.CustomAlertDialog;
import tdc.edu.vn.nhom_10.model.DonHang;
import tdc.edu.vn.nhom_10.model.LoaiMon;
import tdc.edu.vn.nhom_10.model.ChiTietDonHang;

public class ChiTietMon extends AppCompatActivity {
    ImageView imgMon;
    TextView tvTenMon;
    TextView tvLoaiMon;
    TextView tvGia;
    TextView tvSoLuong;
    TextView tvMonTa;
    Button btnThemMon;
    ImageButton btnTang;
    ImageButton btnGiam;
    ChiTietDonHang chiTietDonHang;
    DatabaseReference database;
    StorageReference storage;
    DonHang donHang;
    CustomActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_mon);

        setControl();
        setEvent();
    }

    private void setEvent() {
        //Actionbar
        actionBar.setDelegation(new CustomActionBar.ActionBarDelegation() {
            @Override
            public void backOnClick() {
                Intent intent = new Intent(ChiTietMon.this, DanhSachMon.class);
                Bundle bundle = new Bundle();
                bundle.putString("maBan", donHang.getMaBan());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        actionBar.setActionBarName("Chi ti???t m??n");

        database = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance().getReference("Mon");
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            //L???y d??? li???u chi ti???t m??n
            String maMon = bundle.getString("maMon", "");
            getDataMon(maMon);

            //L???y d??? li???u ????n h??ng
            String maBan = bundle.getString("maBan", "");
            getDataDonHang(maBan);
        }

        btnThemMon.setOnClickListener(v -> {
            if (!kiemtraThem(chiTietDonHang.getMaMon())) {
                donHang.getChiTietDonHang().add(0,chiTietDonHang);
                database.child("Ban").child(donHang.getMaBan()).setValue(donHang).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Intent intent = new Intent(ChiTietMon.this, DonHangCuaBan.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("maBan", donHang.getMaBan());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            } else {
                new CustomAlertDialog(ChiTietMon.this,
                        "H??y ch???n m??n kh??c",
                        "M??n ???? c?? trong ????n h??ng", CustomAlertDialog.ERROR);
            }
        });

        btnTang.setOnClickListener(v -> {
            if (chiTietDonHang.getSoLuong() < 30) {
                chiTietDonHang.setSoLuong(chiTietDonHang.getSoLuong() + 1);
                tvSoLuong.setText(String.valueOf(chiTietDonHang.getSoLuong()));
            }
        });

        btnGiam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chiTietDonHang.getSoLuong() > 1) {
                    chiTietDonHang.setSoLuong(chiTietDonHang.getSoLuong() - 1);
                    tvSoLuong.setText(String.valueOf(chiTietDonHang.getSoLuong()));
                }
            }
        });
    }

    //Ki???m tra th??m m??n tr??ng
    private boolean kiemtraThem(String maMon){
        for(ChiTietDonHang chiTietDonHang: donHang.getChiTietDonHang()){
            if(chiTietDonHang.getMaMon().equals(maMon) && chiTietDonHang.getTrangThai().equals("")){
                return true;
            }
        }
        return false;
    }

    //L???y d??? li???u ????n h??ng
    private void getDataDonHang(String maBan) {
        database.child("Ban").child(maBan).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DonHang nDonHang = snapshot.getValue(DonHang.class);
                if (nDonHang != null) {
                    donHang = nDonHang;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                donHang.getChiTietDonHang().add(chiTietDonHang);
                database.child("Ban").child(donHang.getMaBan()).setValue(donHang);
            }
        });

    }

    //L???y d??? li???u chi ti???t m??n
    private void getDataMon(String maMon) {
        database.child("Mon").child(maMon).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chiTietDonHang = snapshot.getValue(ChiTietDonHang.class);
                tvTenMon.setText(chiTietDonHang.getTenMon());
                NumberFormat formatter = new DecimalFormat("#,###,###");
                tvGia.setText(formatter.format(chiTietDonHang.getGia()) + " ??");
                tvSoLuong.setText(String.valueOf(chiTietDonHang.getSoLuong()));
                tvMonTa.setText("M?? t???\n" + chiTietDonHang.getMoTa());
                getAnhMon(chiTietDonHang.getAnh());
                getDataLoaiMon(chiTietDonHang.getLoaiMon());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //L???y d??? li???u lo???i m??n theo m?? lo???i
    private void getDataLoaiMon(String maLoaiMon) {
        database.child("LoaiMon").child(maLoaiMon).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                LoaiMon loaiMon = snapshot.getValue(LoaiMon.class);
                if(loaiMon!= null) {
                    tvLoaiMon.setText(loaiMon.getTenLoaiMon());
                }
                else {
                    tvLoaiMon.setText("L???i lo???i m??n");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //L???y ???nh m??n
    private void getAnhMon(String anh) {
        int dot = anh.lastIndexOf('.');
        String base = (dot == -1) ? anh : anh.substring(0, dot);
        String extension = (dot == -1) ? "" : anh.substring(dot + 1);
        try {
            final File file = File.createTempFile(base, extension);
            storage.child(anh).getFile(file)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                            imgMon.setImageBitmap(bitmap);
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
        imgMon = findViewById(R.id.imgMon);
        tvTenMon = findViewById(R.id.tvTenMon);
        tvLoaiMon = findViewById(R.id.tvLoaiMon);
        tvGia = findViewById(R.id.tvGia);
        tvSoLuong = findViewById(R.id.tvSoLuong);
        tvMonTa = findViewById(R.id.tvMonTa);
        btnThemMon = findViewById(R.id.btnThemMon);
        btnTang = findViewById(R.id.btnTang);
        btnGiam = findViewById(R.id.btnGiam);
        actionBar = findViewById(R.id.actionBar);
    }
}