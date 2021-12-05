package tdc.edu.vn.nhom_10;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

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

import tdc.edu.vn.nhom_10.adapter.QuanLiLichLamViecAdapter;
import tdc.edu.vn.nhom_10.model.CaLamViec;
import tdc.edu.vn.nhom_10.model.TuanLamViec;

public class LichLamViecNhanVien extends AppCompatActivity {
    RecyclerView lvLichLamViec;
    QuanLiLichLamViecAdapter myRecyclerViewAdapter;
    TuanLamViec tuanLamViec;
    TextView tvNgayBatDau,tvNgayKetThuc;
    ImageButton btnGiam,btnTang;
    ArrayList<CaLamViec> data= new ArrayList<>();
    DatabaseReference database;
    Calendar ngayBatDau, ngayKetThuc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_li_lich_lam_viec_nhan_vien);
        setControl();
        setEvent();
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
                Intent intent = new Intent(LichLamViecNhanVien.this, ChiTietLichLamViecNhanVien.class);
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
                Intent intent = new Intent(LichLamViecNhanVien.this, ChiTietLichLamViecNhanVien.class);
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
                Intent intent = new Intent(LichLamViecNhanVien.this, ChiTietLichLamViecNhanVien.class);
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
    }
}
