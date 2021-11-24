package tdc.edu.vn.nhom_10;

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

import java.util.ArrayList;

import tdc.edu.vn.nhom_10.adapter.MyRecyclerViewAdapterBan;
import tdc.edu.vn.nhom_10.adapter.MyRecylerViewNhanVien;
import tdc.edu.vn.nhom_10.adapter.QuanLiLichLamViecAdapter;
import tdc.edu.vn.nhom_10.model.Ban;
import tdc.edu.vn.nhom_10.model.CaLamViec;
import tdc.edu.vn.nhom_10.model.NhanVien;

public class QuanLiLichLamViec extends AppCompatActivity {
    RecyclerView lvLichLamViec;
    ArrayList<CaLamViec> data = new ArrayList<CaLamViec>();
    QuanLiLichLamViecAdapter myRecyclerViewAdapter;
    ArrayList<String> thuNgay = new ArrayList<String>();
    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_li_lich_lam_viec);
        setControl();
        setEvent();
        //list();
    }
    private void list(){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("LichLamViec");
        for (int i=1;i<8;i++){
            ArrayList<NhanVien> caA = new ArrayList<>();
            for (int j=1;j<3;j++){
                NhanVien nhanVien = new NhanVien();
                nhanVien.setHoTen("Pha Che");
                nhanVien.setanh("8n5nmsBHSmWkozHUTsW8ABAczmM2.png");
                nhanVien.setChucVu("Pha chế");
                nhanVien.setDiaChi("Binh dinh");
                nhanVien.setEmail("phache@gmail.com");
                nhanVien.setSoCCCD("1232421");
                nhanVien.setNgaySinh("18/11/2021");
                nhanVien.setSoDienThoai("123456");
                caA.add(nhanVien);
            }
            ArrayList<NhanVien> caB = new ArrayList<>();
            for (int j=1;j<3;j++){
                NhanVien nhanVien = new NhanVien();
                nhanVien.setHoTen("Pha Che");
                nhanVien.setanh("8n5nmsBHSmWkozHUTsW8ABAczmM2.png");
                nhanVien.setChucVu("Pha chế");
                nhanVien.setDiaChi("Binh dinh");
                nhanVien.setEmail("phache@gmail.com");
                nhanVien.setSoCCCD("1232421");
                nhanVien.setNgaySinh("18/11/2021");
                nhanVien.setSoDienThoai("123456");
                caB.add(nhanVien);
            }
            ArrayList<NhanVien> caC = new ArrayList<>();
            for (int j=1;j<3;j++){
                NhanVien nhanVien = new NhanVien();
                nhanVien.setHoTen("Pha Che");
                nhanVien.setanh("8n5nmsBHSmWkozHUTsW8ABAczmM2.png");
                nhanVien.setChucVu("Pha chế");
                nhanVien.setDiaChi("Binh dinh");
                nhanVien.setEmail("phache@gmail.com");
                nhanVien.setSoCCCD("1232421");
                nhanVien.setNgaySinh("18/11/2021");
                nhanVien.setSoDienThoai("123456");
                caC.add(nhanVien);
            }

            String maDH= mDatabase.push().getKey();
            CaLamViec caLamViec = new CaLamViec();
            caLamViec.setMaLichLamViec(maDH);
            caLamViec.setThuNgay("T2");
            caLamViec.setCaA(caA);
            caLamViec.setCaB(caB);
            caLamViec.setCaC(caC);
            mDatabase.child(maDH).setValue(caLamViec);

        }
    }
    //Lấy danh sách bàn trên RealTimeDatabase
    private void getListBanFromRealTimeDatabase(){
        database= FirebaseDatabase.getInstance().getReference("LichLamViec");
        //Query query = database.orderByChild("thuNgay");
        database.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                CaLamViec caLamViec = snapshot.getValue(CaLamViec.class);
                if(caLamViec != null){
                    data.add(caLamViec);
                    myRecyclerViewAdapter.notifyDataSetChanged();
                }
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
    }
    private void setEvent() {
        myRecyclerViewAdapter = new QuanLiLichLamViecAdapter(this,R.layout.layout_item_lich_lam_viec,data);
        myRecyclerViewAdapter.setDelegation(new QuanLiLichLamViecAdapter.MyItemClickListener() {
            @Override
            public void getCaA(CaLamViec caLamViec) {
                Toast.makeText(QuanLiLichLamViec.this, "Ca A", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void getCaB(CaLamViec caLamViec) {
                Toast.makeText(QuanLiLichLamViec.this, "Ca B", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void getCaC(CaLamViec caLamViec) {
                Toast.makeText(QuanLiLichLamViec.this, "Ca C", Toast.LENGTH_SHORT).show();
            }
        });

        lvLichLamViec.setAdapter(myRecyclerViewAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        lvLichLamViec.setLayoutManager(layoutManager);
        getListBanFromRealTimeDatabase();
    }
    private void setControl() {
        lvLichLamViec=findViewById(R.id.lvLichLamViec);
    }
}
