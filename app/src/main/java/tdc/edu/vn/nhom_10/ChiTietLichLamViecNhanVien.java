package tdc.edu.vn.nhom_10;

import android.content.Intent;
import android.os.Bundle;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import tdc.edu.vn.nhom_10.CustomView.CustomActionBar;
import tdc.edu.vn.nhom_10.adapter.ChiTietLichLamViecNhanVienAdapter;
import tdc.edu.vn.nhom_10.model.NhanVien;
import tdc.edu.vn.nhom_10.model.TuanLamViec;

public class ChiTietLichLamViecNhanVien extends AppCompatActivity {
    RecyclerView lvCTLichLamViec;
    TextView tvTenNV,tvSDT,tvChucVu,tvCa,tvNgay;
    ImageView imgAnhNV;
    TuanLamViec tuanLamViec;
    ArrayList<NhanVien>data = new ArrayList<>();
    ChiTietLichLamViecNhanVienAdapter myRecyclerViewAdapter;
    DatabaseReference database;
    Calendar calendar;
    String maTuan,ca,tuan;
    int maThu;
    CustomActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_lich_lam_viec_nhan_vien);
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
                    if(ca.equals("caA")){
                        data.addAll(tuanLamViec.getCaLamViec().get(maThu).getCaA());
                    }
                    if(ca.equals("caB")){
                        data.addAll(tuanLamViec.getCaLamViec().get(maThu).getCaB());
                    }
                    if(ca.equals("caC")){
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
        actionBar.setDelegation(new CustomActionBar.ActionBarDelegation() {
            @Override
            public void backOnClick() {
               finish();
            }
        });
        actionBar.setActionBarName("Chi tiết lịch làm việc");

        SimpleDateFormat dinhDang = new SimpleDateFormat("dd/MM/yyyy");
        database = FirebaseDatabase.getInstance().getReference();
        myRecyclerViewAdapter = new ChiTietLichLamViecNhanVienAdapter(this,R.layout.layout_item_chi_tiet_lich_lam_viec_nhan_vien,data);

        lvCTLichLamViec.setAdapter(myRecyclerViewAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        lvCTLichLamViec.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            maTuan = bundle.getString("maTuan", "");
            tuan=bundle.getString("tuan","");
            maThu=bundle.getInt("maThu",0);
            ca = bundle.getString("ca", "");
            getData(maTuan,maThu,ca);
            tvCa.setText(ca);

            int dot = tuan.lastIndexOf(' ');
            String base = (dot == -1) ? tuan : tuan.substring(0, dot);

            calendar = Calendar.getInstance();
            try {
                calendar.setTime(dinhDang.parse(base));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            calendar.roll(calendar.DAY_OF_YEAR,maThu);
            tvNgay.setText(dinhDang.format(calendar.getTime()));
        }
    }
    private void setControl() {
        tvCa=findViewById(R.id.tvcaA);
        tvNgay=findViewById(R.id.tvNgay);
        tvTenNV=findViewById(R.id.tvTenNV);
        tvSDT=findViewById(R.id.tvSDT);
        tvChucVu=findViewById(R.id.tvChucVu);
        imgAnhNV=findViewById(R.id.imgAnhNV);
        lvCTLichLamViec=findViewById(R.id.lvCTlichLamViec);
        actionBar=findViewById(R.id.actionBar);
    }
}
