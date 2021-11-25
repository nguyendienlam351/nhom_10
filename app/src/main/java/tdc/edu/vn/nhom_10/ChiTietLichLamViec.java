package tdc.edu.vn.nhom_10;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import tdc.edu.vn.nhom_10.adapter.ChiTietLichLamViecAdapter;
import tdc.edu.vn.nhom_10.adapter.QuanLiLichLamViecAdapter;
import tdc.edu.vn.nhom_10.model.CaLamViec;
import tdc.edu.vn.nhom_10.model.ChiTietDonHang;
import tdc.edu.vn.nhom_10.model.NhanVien;
import tdc.edu.vn.nhom_10.model.TuanLamViec;

public class ChiTietLichLamViec extends AppCompatActivity {
    RecyclerView lvCTLichLamViec;
    TextView tvTenNV,tvSDT,tvChucVu,tvCa,tvNgay;
    ImageButton btnThem;
    ImageView imgAnhNV;
    TuanLamViec tuanLamViec;
    ArrayList<NhanVien>data = new ArrayList<>();
    ChiTietLichLamViecAdapter myRecyclerViewAdapter;
    StorageReference storage;
    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_lich_lam_viec);
        setControl();
        setEvent();
    }
    private void getData(String maTuan,int maThu, String ca) {
        database.child("LichLamViec").child(maTuan).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TuanLamViec nTuanLamViec = snapshot.getValue(TuanLamViec.class);
                if (nTuanLamViec != null) {
                    tuanLamViec = nTuanLamViec;
                    data.clear();
                    if(ca.equals("ca A")){
                        data.addAll(tuanLamViec.getCaLamViec().get(maThu).getCaA());
                    }
                    if(ca.equals("ca B")){
                        data.addAll(tuanLamViec.getCaLamViec().get(maThu).getCaB());
                    }
                    if(ca.equals("ca C")){
                        data.addAll(tuanLamViec.getCaLamViec().get(maThu).getCaC());
                    }
                    myRecyclerViewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void setEvent() {
        database = FirebaseDatabase.getInstance().getReference();
        myRecyclerViewAdapter = new ChiTietLichLamViecAdapter(this,R.layout.layout_item_chi_tiet_lich_lam_viec,data);
        myRecyclerViewAdapter.setDelegation(new ChiTietLichLamViecAdapter.MyItemClickListener() {
            @Override
            public void getXoaNV(NhanVien nhanVien) {
                Toast.makeText(ChiTietLichLamViec.this, "Ca A", Toast.LENGTH_SHORT).show();
            }

        });

        lvCTLichLamViec.setAdapter(myRecyclerViewAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        lvCTLichLamViec.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            String maTuan = bundle.getString("maTuan", "");
            int maThu=bundle.getInt("maThu",0);
            String ca = bundle.getString("ca", "");
            getData(maTuan,maThu,ca);
            tvCa.setText(ca);
        }
    }
    private void setControl() {
        tvCa=findViewById(R.id.tvcaA);
        tvNgay=findViewById(R.id.tvNgay);
        btnThem=findViewById(R.id.btnThem);
        tvTenNV=findViewById(R.id.tvTenNV);
        tvSDT=findViewById(R.id.tvSDT);
        tvChucVu=findViewById(R.id.tvChucVu);
        imgAnhNV=findViewById(R.id.imgAnhNV);
        lvCTLichLamViec=findViewById(R.id.lvCTlichLamViec);
    }
}
