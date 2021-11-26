package tdc.edu.vn.nhom_10;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import tdc.edu.vn.nhom_10.adapter.MyRecyclerViewAdapterBan;
import tdc.edu.vn.nhom_10.adapter.MyRecylerViewNhanVien;
import tdc.edu.vn.nhom_10.adapter.QuanLiLichLamViecAdapter;
import tdc.edu.vn.nhom_10.model.Ban;
import tdc.edu.vn.nhom_10.model.CaLamViec;
import tdc.edu.vn.nhom_10.model.NhanVien;
import tdc.edu.vn.nhom_10.model.TuanLamViec;

public class QuanLiLichLamViec extends AppCompatActivity {
    RecyclerView lvLichLamViec;
    QuanLiLichLamViecAdapter myRecyclerViewAdapter;
    TuanLamViec tuanLamViec;
    ArrayList<CaLamViec> data= new ArrayList<>();
    DatabaseReference database;

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
        tuanLamViec = new TuanLamViec();
        data.addAll(tuanLamViec.getCaLamViec());
        myRecyclerViewAdapter = new QuanLiLichLamViecAdapter(this, R.layout.layout_item_lich_lam_viec, data);
        myRecyclerViewAdapter.setDelegation(new QuanLiLichLamViecAdapter.MyItemClickListener() {
            @Override
            public void getCaA(CaLamViec caLamViec, int position) {
                Intent intent = new Intent(QuanLiLichLamViec.this, ChiTietLichLamViec.class);
                Bundle bundle = new Bundle();
                bundle.putString("maTuan", tuanLamViec.getMaTuanLamViec());
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
        getListBanFromRealTimeDatabase();
    }

    private void setControl() {
        lvLichLamViec = findViewById(R.id.lvLichLamViec);
    }
}
