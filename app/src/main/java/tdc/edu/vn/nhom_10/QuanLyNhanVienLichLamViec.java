package tdc.edu.vn.nhom_10;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.SearchView;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import tdc.edu.vn.nhom_10.CustomView.CustomActionBar;
import tdc.edu.vn.nhom_10.CustomView.CustomAlertDialog;
import tdc.edu.vn.nhom_10.adapter.MyRecyclerViewAdapterBan;
import tdc.edu.vn.nhom_10.adapter.MyRecylerViewNhanVien;
import tdc.edu.vn.nhom_10.adapter.NhanVienLichLamViecAdapter;
import tdc.edu.vn.nhom_10.model.NhanVien;
import tdc.edu.vn.nhom_10.model.TuanLamViec;

public class QuanLyNhanVienLichLamViec extends AppCompatActivity {
    ImageButton btnBack;
    RecyclerView lvNhanVien;
    ArrayList<NhanVien> list = new ArrayList<NhanVien>();
    ArrayList<NhanVien> caLamViec= new ArrayList<>();
    NhanVienLichLamViecAdapter adapter;
    Spinner spnChucVu;
    ArrayList<String> chucVu = new ArrayList<String>();
    DatabaseReference mData;
    SearchView svSearch;
    CustomActionBar actionBar;
    TuanLamViec tuanLamViec;
    int viTri = 0;
    String maTuan,ca,tuan;
    int maThu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_nhan_vien_lich_lam_viec);

        setControl();
        setEvent();

    }

    private void setEvent() {
        actionBar.setDelegation(new CustomActionBar.ActionBarDelegation() {
            @Override
            public void backOnClick() {
                Intent intent = new Intent(QuanLyNhanVienLichLamViec.this, ChiTietLichLamViec.class);
                startActivity(intent);
            }
        });
        actionBar.setActionBarName("Danh s??ch nh??n vi??n");
        mData=FirebaseDatabase.getInstance().getReference();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            maTuan = bundle.getString("maTuan", "");
            tuan = bundle.getString("tuan","");
            maThu=bundle.getInt("maThu",0);
            ca = bundle.getString("ca", "");
            getData(maTuan,maThu,ca);
        }
        //
        actionBar.setDelegation(new CustomActionBar.ActionBarDelegation() {
            @Override
            public void backOnClick() {
                finish();
            }
        });

        actionBar.setActionBarName("Danh s??ch nh??n vi??n");

        //G???i firebase

        chucVu.add("T???t c???");
        chucVu.add("Qu???n l??");
        chucVu.add("Ph???c v???");
        chucVu.add("Pha ch???");
        chucVu.add("Thu ng??n");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, chucVu);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnChucVu.setAdapter(arrayAdapter);

        //H??m g???i adapter
        adapter = new NhanVienLichLamViecAdapter(this,R.layout.layout_item_nhan_vien_lich_lam_viec,list);
        adapter.setDelegation(new NhanVienLichLamViecAdapter.MyItemClickListener() {
             @Override
              public void getThemNV(NhanVien nhanVien) {
                 if(maTuan.equals("")){
                     maTuan=mData.push().getKey();
                     tuanLamViec.setMaTuanLamViec(maTuan);
                     tuanLamViec.setTuanLamViec(tuan);
                     mData.child("LichLamViec").child(tuanLamViec.getMaTuanLamViec()).setValue(tuanLamViec);

                     caLamViec.add(0,nhanVien);
                     mData.child("LichLamViec").child(tuanLamViec.getMaTuanLamViec()).child("caLamViec")
                             .child(String.valueOf(maThu)).child(ca).setValue(caLamViec);
                 }
                 else {
                     if (kiemTra(nhanVien.getMaNV())) {
                         new CustomAlertDialog(QuanLyNhanVienLichLamViec.this,"Th??ng b??o","Nh??n vi??n b??? tr??ng! Vui l??ng ch???n nh??n vi??n kh??c!",CustomAlertDialog.ERROR).show();
                         return;
                     }
                     else {
                         caLamViec.add(0,nhanVien);
                         mData.child("LichLamViec").child(maTuan).child("caLamViec")
                                 .child(String.valueOf(maThu)).child(ca).setValue(caLamViec);
                     }
                 }
                 Intent intent = new Intent(getApplicationContext(), ChiTietLichLamViec.class);
                 Bundle bundle = new Bundle();
                 bundle.putString("maTuan",maTuan);
                 bundle.putString("tuan",tuan);
                 bundle.putInt("maThu", maThu);
                 bundle.putString("ca",ca);
                 intent.putExtras(bundle);
                 startActivity(intent);
             }
        });
        lvNhanVien.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        lvNhanVien.setLayoutManager(layoutManager);
        getFirebase();
        //Search
        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });

        spnChucVu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viTri = position;
                filter(svSearch.getQuery().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    //H??m ki???m tra m?? nh??n vi??n
    private boolean kiemTra(String maNV){
        for (int i=0;i<caLamViec.size();i++){
            if(caLamViec.get(i).getMaNV().equals(maNV)){
                return true;
            }
        }
        return false;
    }
    //l???y d??? li???u c???a l???ch l??m vi???c
    private void getData(String maTuan,int maThu, String ca) {
        mData.child("LichLamViec").child(maTuan).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TuanLamViec nTuanLamViec = snapshot.getValue(TuanLamViec.class);
                if (nTuanLamViec != null) {
                    tuanLamViec = nTuanLamViec;
                    caLamViec.clear();
                    if(ca.equals("caA")){
                        caLamViec.addAll(tuanLamViec.getCaLamViec().get(maThu).getCaA());
                    }
                    if(ca.equals("caB")){
                        caLamViec.addAll(tuanLamViec.getCaLamViec().get(maThu).getCaB());
                    }
                    if(ca.equals("caC")){
                        caLamViec.addAll(tuanLamViec.getCaLamViec().get(maThu).getCaC());
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    //H??m l???c theo t??n
    private void filter(String search) {
        String stChucVu = "";
        if (!chucVu.get(viTri).equals("T???t c???")) {
            stChucVu = chucVu.get(viTri);
        }
        ArrayList<NhanVien> filterList = new ArrayList<>();
        for (NhanVien nhanVien : list) {
            if (nhanVien.getChucVu().contains(stChucVu) && nhanVien.getHoTen().toLowerCase().contains(search.toLowerCase())) {
                filterList.add(nhanVien);
            }
            adapter.filterList(filterList);
        }
    }

    //H??m ?????c firebase
    private void getFirebase() {
        // Write a message to the database
        mData.child("NhanVien").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                NhanVien nhanVien = snapshot.getValue(NhanVien.class);
                if (nhanVien != null) {
                    list.add(0,nhanVien);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                NhanVien nhanVien = snapshot.getValue(NhanVien.class);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void setControl() {
        lvNhanVien = findViewById(R.id.lvNhanVien);
        spnChucVu = findViewById(R.id.spnChucVu);
        svSearch = findViewById(R.id.svSearch);
        actionBar = findViewById(R.id.actionBar);
    }
}