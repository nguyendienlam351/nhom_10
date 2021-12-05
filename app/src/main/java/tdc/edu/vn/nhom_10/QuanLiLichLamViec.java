package tdc.edu.vn.nhom_10;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import tdc.edu.vn.nhom_10.CustomView.CustomActionBar;
import tdc.edu.vn.nhom_10.adapter.QuanLiLichLamViecAdapter;
import tdc.edu.vn.nhom_10.model.CaLamViec;
import tdc.edu.vn.nhom_10.model.MaGiamGia;
import tdc.edu.vn.nhom_10.model.NhanVien;
import tdc.edu.vn.nhom_10.model.TuanLamViec;

public class QuanLiLichLamViec extends AppCompatActivity {
    RecyclerView lvLichLamViec;
    QuanLiLichLamViecAdapter myRecyclerViewAdapter;
    TuanLamViec tuanLamViec;
    TextView tvNgayBatDau,tvNgayKetThuc;
    ImageButton btnGiam,btnTang;
    ArrayList<CaLamViec> data= new ArrayList<>();
    DatabaseReference database;
    Calendar ngayBatDau, ngayKetThuc;
    CustomActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_li_lich_lam_viec);
        setControl();
        setEvent();
        //list();
    }

    private void list() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("LichLamViec");
        TuanLamViec tuanLamViec1 = new TuanLamViec();
        tuanLamViec1.setMaTuanLamViec(mDatabase.push().getKey());
        tuanLamViec1.setTuanLamViec("1");
//        for (int i=1;i<8;i++){
        for (int j = 1; j < 3; j++) {
            NhanVien nhanVien = new NhanVien();
            nhanVien.setMaNV("8n5nmsBHSmWkozHUTsW8ABAczmM2");
            nhanVien.setHoTen("Pha Che");
            nhanVien.setanh("8n5nmsBHSmWkozHUTsW8ABAczmM2.png");
            nhanVien.setChucVu("Pha chế");
            nhanVien.setDiaChi("Binh dinh");
            nhanVien.setEmail("phache@gmail.com");
            nhanVien.setSoCCCD("1232421");
            nhanVien.setNgaySinh("18/11/2021");
            nhanVien.setSoDienThoai("123456");
            tuanLamViec1.getCaLamViec().get(0).getCaA().add(nhanVien);
        }
        mDatabase.child(tuanLamViec1.getMaTuanLamViec()).setValue(tuanLamViec1);
//            ArrayList<NhanVien> caB = new ArrayList<>();
//            for (int j=1;j<3;j++){
//                NhanVien nhanVien = new NhanVien();
//                nhanVien.setMaNV("8n5nmsBHSmWkozHUTsW8ABAczmM2");
//                nhanVien.setHoTen("Phuc vu");
//                nhanVien.setanh("8n5nmsBHSmWkozHUTsW8ABAczmM2.png");
//                nhanVien.setChucVu("Phụ vụ");
//                nhanVien.setDiaChi("Binh dinh");
//                nhanVien.setEmail("phache@gmail.com");
//                nhanVien.setSoCCCD("1232421");
//                nhanVien.setNgaySinh("18/11/2021");
//                nhanVien.setSoDienThoai("123456");
//                caB.add(nhanVien);
//            }
//            ArrayList<NhanVien> caC = new ArrayList<>();
//            for (int j=1;j<3;j++){
//                NhanVien nhanVien = new NhanVien();
//                nhanVien.setMaNV("8n5nmsBHSmWkozHUTsW8ABAczmM2");
//                nhanVien.setHoTen("Thu ngan");
//                nhanVien.setanh("8n5nmsBHSmWkozHUTsW8ABAczmM2.png");
//                nhanVien.setChucVu("Thu Ngân");
//                nhanVien.setDiaChi("Binh dinh");
//                nhanVien.setEmail("phache@gmail.com");
//                nhanVien.setSoCCCD("1232421");
//                nhanVien.setNgaySinh("18/11/2021");
//                nhanVien.setSoDienThoai("123456");
//                caC.add(nhanVien);
//            }
//
//            String maDH= mDatabase.push().getKey();
//            CaLamViec caLamViec = new CaLamViec();
//            caLamViec.setMaLichLamViec(maDH);
//            caLamViec.setThuNgay("T2");
//            caLamViec.setCaA(caA);
//            caLamViec.setCaB(caB);
//            caLamViec.setCaC(caC);
//            mDatabase.child(maDH).setValue(caLamViec);
//
//        }
    }

    //Lấy danh sách bàn trên RealTimeDatabase
    private void getListBanFromRealTimeDatabase() {
        database = FirebaseDatabase.getInstance().getReference("LichLamViec");
        database.child("-MpMuYaqrHu3OK-yIamJ").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TuanLamViec nTuanLamViec = snapshot.getValue(TuanLamViec.class);
                if (nTuanLamViec != null) {
                    tuanLamViec = nTuanLamViec;
                    data.clear();
                    data.addAll(tuanLamViec.getCaLamViec());
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
                Intent intent = new Intent(getApplicationContext(), QuanLy.class);
                startActivity(intent);
            }
        });

        actionBar.setActionBarName("Quản lý lịch làm việc");
        database=FirebaseDatabase.getInstance().getReference();
        DateFormat dinhDang = new SimpleDateFormat("dd/MM/yyyy");
        ngayBatDau=Calendar.getInstance();
        ngayKetThuc=Calendar.getInstance();

        ngayBatDau.setFirstDayOfWeek(Calendar.MONDAY);
        ngayBatDau.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        tvNgayBatDau.setText(dinhDang.format(ngayBatDau.getTime()));

        ngayKetThuc.setFirstDayOfWeek(Calendar.MONDAY);
        ngayKetThuc.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        tvNgayKetThuc.setText(dinhDang.format(ngayKetThuc.getTime()));;

        tuanLamViec = new TuanLamViec();
        data.addAll(tuanLamViec.getCaLamViec());
        myRecyclerViewAdapter = new QuanLiLichLamViecAdapter(this, R.layout.layout_item_lich_lam_viec, data);
        myRecyclerViewAdapter.setDelegation(new QuanLiLichLamViecAdapter.MyItemClickListener() {
            @Override
            public void getCaA(CaLamViec caLamViec, int position) {
                Intent intent = new Intent(QuanLiLichLamViec.this, ChiTietLichLamViec.class);
                Bundle bundle = new Bundle();
                bundle.putString("maTuan", tuanLamViec.getMaTuanLamViec());
                bundle.putString("tuan",tvNgayBatDau.getText().toString() + " - "+tvNgayKetThuc.getText().toString());
                bundle.putInt("maThu", position);
                bundle.putString("ca","caA");
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void getCaB(CaLamViec caLamViec, int position) {
                Intent intent = new Intent(QuanLiLichLamViec.this, ChiTietLichLamViec.class);
                Bundle bundle = new Bundle();
                bundle.putString("maTuan", tuanLamViec.getMaTuanLamViec());
                bundle.putString("tuan",tvNgayBatDau.getText().toString() + " - "+tvNgayKetThuc.getText().toString());
                bundle.putInt("maThu", position);
                bundle.putString("ca","caB");
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void getCaC(CaLamViec caLamViec, int position) {
                Intent intent = new Intent(QuanLiLichLamViec.this, ChiTietLichLamViec.class);
                Bundle bundle = new Bundle();
                bundle.putString("maTuan", tuanLamViec.getMaTuanLamViec());
                bundle.putString("tuan",tvNgayBatDau.getText().toString() + " - "+tvNgayKetThuc.getText().toString());
                bundle.putInt("maThu", position);
                bundle.putString("ca","caC");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        lvLichLamViec.setAdapter(myRecyclerViewAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        lvLichLamViec.setLayoutManager(layoutManager);
        getLichLamViec(tvNgayBatDau.getText().toString() + " - "+tvNgayKetThuc.getText().toString());
       // getListBanFromRealTimeDatabase();

        //Hàm nút Tăng
        btnTang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int day=ngayBatDau.get(Calendar.DAY_OF_MONTH);
                int month=ngayBatDau.get(Calendar.MONTH);
                int year=ngayBatDau.get(Calendar.YEAR);
                ngayBatDau.set(year,month,day +7);

                 day=ngayKetThuc.get(Calendar.DAY_OF_MONTH);
                 month=ngayKetThuc.get(Calendar.MONTH);
                 year=ngayKetThuc.get(Calendar.YEAR);
                ngayKetThuc.set(year,month,day +7);
                tvNgayBatDau.setText(dinhDang.format(ngayBatDau.getTime()));
                tvNgayKetThuc.setText(dinhDang.format(ngayKetThuc.getTime()));

                getLichLamViec(tvNgayBatDau.getText().toString() + " - "+tvNgayKetThuc.getText().toString());
            }
        });

        btnGiam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int day=ngayBatDau.get(Calendar.DAY_OF_MONTH);
                int month=ngayBatDau.get(Calendar.MONTH);
                int year=ngayBatDau.get(Calendar.YEAR);
                ngayBatDau.set(year,month,day -7);

                day=ngayKetThuc.get(Calendar.DAY_OF_MONTH);
                month=ngayKetThuc.get(Calendar.MONTH);
                year=ngayKetThuc.get(Calendar.YEAR);
                ngayKetThuc.set(year,month,day -7);
                tvNgayBatDau.setText(dinhDang.format(ngayBatDau.getTime()));
                tvNgayKetThuc.setText(dinhDang.format(ngayKetThuc.getTime()));

                getLichLamViec(tvNgayBatDau.getText().toString() + " - "+tvNgayKetThuc.getText().toString());
            }
        });
    }
    //Hàm lấy dữ liệu
    private void getLichLamViec(String tuan) {
        final TuanLamViec[] tuanLamViecs = new TuanLamViec[1];
        database.child("LichLamViec").orderByChild("tuanLamViec").equalTo(tuan).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                tuanLamViecs[0] = snapshot.getValue(TuanLamViec.class);
                    tuanLamViec = tuanLamViecs[0];
                    data.clear();
                    data.addAll(tuanLamViec.getCaLamViec());
                    myRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if (tuanLamViecs[0] == null) {
            tuanLamViec = new TuanLamViec();
            data.clear();
            data.addAll(tuanLamViec.getCaLamViec());
            myRecyclerViewAdapter.notifyDataSetChanged();
        }
    }
    private void setControl() {
        lvLichLamViec = findViewById(R.id.lvLichLamViec);
        tvNgayBatDau = findViewById(R.id.tvNgayBatDau);
        tvNgayKetThuc = findViewById(R.id.tvNgayKetThuc);
        btnGiam = findViewById(R.id.btnGiam);
        btnTang = findViewById(R.id.btnTang);
        actionBar = findViewById(R.id.actionBar);
    }
}
