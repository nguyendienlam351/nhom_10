package tdc.edu.vn.nhom_10;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import tdc.edu.vn.nhom_10.CustomView.CustomActionBar;
import tdc.edu.vn.nhom_10.CustomView.CustomAlertDialog;
import tdc.edu.vn.nhom_10.adapter.MonAdapter;
import tdc.edu.vn.nhom_10.model.DonHang;
import tdc.edu.vn.nhom_10.model.LoaiMon;
import tdc.edu.vn.nhom_10.model.ChiTietDonHang;

public class DanhSachMon extends AppCompatActivity {
    RecyclerView lvDanhSachMon;
    MonAdapter monAdapter;
    ArrayList<ChiTietDonHang> chiTietDonHangArrayList;
    ArrayList<LoaiMon> loaiMonArrayList;
    DatabaseReference database;
    Spinner spnLoaiMon;
    SearchView edtTimKiem;
    ArrayAdapter<LoaiMon> loaiMonAdapter;
    int viTriLoai = 0;
    DonHang donHang;
    CustomActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_mon);

        setControl();
        setEvent();
    }

    private void setEvent() {
        //Actionbar
        actionBar.setDelegation(new CustomActionBar.ActionBarDelegation() {
            @Override
            public void backOnClick() {
                Intent intent = new Intent(DanhSachMon.this, DonHangCuaBan.class);
                Bundle bundle = new Bundle();
                bundle.putString("maBan", donHang.getMaBan());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        actionBar.setActionBarName("Danh s??ch m??n");

        database = FirebaseDatabase.getInstance().getReference();

        //L???y d??? li???u danh s??ch m??n
        chiTietDonHangArrayList = new ArrayList<>();
        monAdapter = new MonAdapter(this, R.layout.layout_item_mon_1, chiTietDonHangArrayList);
        //Set s??? ki???n item
        monAdapter.setDelegation(new MonAdapter.MonItemClickListener() {

            @Override
            public void itemClick(ChiTietDonHang chiTietDonHang) {
                Intent intent = new Intent(DanhSachMon.this, ChiTietMon.class);
                Bundle bundle = new Bundle();
                bundle.putString("maMon", chiTietDonHang.getMaMon());
                bundle.putString("maBan", donHang.getMaBan());
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void iconClick(ChiTietDonHang chiTietDonHang) {
                if (!kiemtraThem(chiTietDonHang.getMaMon())) {
                    donHang.getChiTietDonHang().add(0,chiTietDonHang);
                    database.child("Ban").child(donHang.getMaBan()).setValue(donHang).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            Intent intent = new Intent(DanhSachMon.this, DonHangCuaBan.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("maBan", donHang.getMaBan());
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                }
                else {
                    new CustomAlertDialog(DanhSachMon.this,
                            "H??y ch???n m??n kh??c",
                            "M??n ???? c?? trong ????n h??ng.", CustomAlertDialog.ERROR).show();
                }
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        lvDanhSachMon.setLayoutManager(layoutManager);
        lvDanhSachMon.setAdapter(monAdapter);

        //L???y d??? li???u danh s??ch lo???i m??n
        loaiMonArrayList = new ArrayList<>();
        loaiMonAdapter = new ArrayAdapter<LoaiMon>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, loaiMonArrayList);
        loaiMonAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnLoaiMon.setAdapter(loaiMonAdapter);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            //L???y d??? li???u ????n h??ng
            String maBan = bundle.getString("maBan", "");
            getDataDonHang(maBan);
        }

        //l???y d??? li???u lo???i m??n
        getDataLoaiMon();
        //L???y d??? li???u m??n
        getDataMon();

        //T??m ki???m m??n theo t??n
        edtTimKiem.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        //L???c d??? li???u m??n theo lo???i m??n
        spnLoaiMon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    viTriLoai = position;
                    filter(edtTimKiem.getQuery().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //L???c d??? li???u m??n
    private void filter(String tenMon){
        String maLoaiMon = loaiMonArrayList.get(viTriLoai).getMaLoaiMon();
        ArrayList<ChiTietDonHang> filterList = new ArrayList<ChiTietDonHang>();
        for (ChiTietDonHang chiTietDonHang : chiTietDonHangArrayList) {
            if(chiTietDonHang.getLoaiMon().contains(maLoaiMon) && chiTietDonHang.getTenMon().toLowerCase().contains(tenMon.toLowerCase())){
                filterList.add(chiTietDonHang);
            }
        }

        monAdapter.filterList(filterList);
    }

    //L???y d??? li???u ????n h??ng theo m?? b??n
    private void getDataDonHang(String maBan) {
        database.child("Ban").child(maBan).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 DonHang nDonHang = snapshot.getValue(DonHang.class);
                if (nDonHang != null){
                    donHang = nDonHang;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    //L???y d??? li???u lo???i m??n
    private void getDataLoaiMon() {
        LoaiMon tatCa = new LoaiMon("T???t c???");
        tatCa.setMaLoaiMon("");
        loaiMonArrayList.add(tatCa);
        database.child("LoaiMon").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    LoaiMon loaiMon = dataSnapshot.getValue(LoaiMon.class);
                    if (loaiMon != null) {
                        loaiMonArrayList.add(loaiMon);
                        loaiMonAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //L???y d??? li???u m??n
    private void getDataMon() {
        database.child("Mon").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ChiTietDonHang chiTietDonHang = snapshot.getValue(ChiTietDonHang.class);
                if (chiTietDonHang != null) {
                    chiTietDonHangArrayList.add(0, chiTietDonHang);
                    monAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ChiTietDonHang chiTietDonHang = snapshot.getValue(ChiTietDonHang.class);
                if (chiTietDonHang == null || chiTietDonHangArrayList == null || chiTietDonHangArrayList.isEmpty()) {
                    return;
                }
                for (int i = 0; i < chiTietDonHangArrayList.size(); i++) {
                    if (chiTietDonHang.getMaMon() == chiTietDonHangArrayList.get(i).getMaMon()) {
                        chiTietDonHangArrayList.set(i, chiTietDonHang);
                        break;
                    }
                }
                monAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                ChiTietDonHang chiTietDonHang = snapshot.getValue(ChiTietDonHang.class);
                if (chiTietDonHang == null || chiTietDonHangArrayList == null || chiTietDonHangArrayList.isEmpty()) {
                    return;
                }
                for (int i = 0; i < chiTietDonHangArrayList.size(); i++) {
                    if (chiTietDonHang.getMaMon() == chiTietDonHangArrayList.get(i).getMaMon()) {
                        chiTietDonHangArrayList.remove(chiTietDonHangArrayList.get(i));
                        break;
                    }
                }
                monAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //Ki???m tra m??n tr??ng
    private boolean kiemtraThem(String maMon){
        for(ChiTietDonHang chiTietDonHang: donHang.getChiTietDonHang()){
            if(chiTietDonHang.getMaMon().equals(maMon) && chiTietDonHang.getTrangThai().equals("")){
                return true;
            }
        }

        return false;
    }

    private void setControl() {
        lvDanhSachMon = findViewById(R.id.lvDanhSachMon);
        spnLoaiMon = findViewById(R.id.spnLoaiMon);
        edtTimKiem = findViewById(R.id.edtTimKiem);
        actionBar = findViewById(R.id.actionBar);
    }
}