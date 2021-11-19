package tdc.edu.vn.nhom_10;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Spinner;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import tdc.edu.vn.nhom_10.CustomView.CustomActionBar;
import tdc.edu.vn.nhom_10.adapter.MonAnAdapter;
import tdc.edu.vn.nhom_10.adapter.MyRecylerViewNhanVien;
import tdc.edu.vn.nhom_10.model.ChiTietDonHang;
import tdc.edu.vn.nhom_10.model.LoaiMon;
import tdc.edu.vn.nhom_10.model.MonAn;
import tdc.edu.vn.nhom_10.model.NhanVien;

public class QuanLyMon extends AppCompatActivity {

    RecyclerView imgMon;
    CustomActionBar actionBar;
    Spinner spMon;
    ImageButton btnThem;
    SearchView edtTimKiem;
    MonAnAdapter monAnAdapter;
    int viTri = 0;
    ArrayList<LoaiMon> loaiMonArrayList;
    ArrayList<MonAn> monAnArrayList= new ArrayList<MonAn>();

    DatabaseReference databaseReference;
    ArrayAdapter arrayAdapter;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_mon);
        setControl();
        setEvent();
    }


    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference("Mon");


    private void setEvent() {
        // lay du lieu tu item
        monAnAdapter = new MonAnAdapter(this,R.layout.layout_item_ql_mon,monAnArrayList);
        imgMon.setAdapter(monAnAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        imgMon.setLayoutManager(layoutManager);

        getDataMon();

// actionBar
        databaseReference = FirebaseDatabase.getInstance().getReference();
        actionBar.setDelegation(new CustomActionBar.ActionBarDelegation() {
            @Override
            public void backOnClick() {
                finish();
            }
        });
        actionBar.setActionBarName("MonAn");
// spiner
        loaiMonArrayList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<LoaiMon>(this, android.R.layout.simple_spinner_item, loaiMonArrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMon.setAdapter(arrayAdapter);
        getDataLoaiMon();



        //Chuyển màn hình thêm
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(QuanLyMon.this, QuanLyThemMon.class);
                startActivity(intent);
            }
        });

// search theo spiner
        spMon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viTri = position;
                filter(edtTimKiem.getQuery().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //Search
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
    }





    private void filter(String tenMon){
        String maLoaiMon = loaiMonArrayList.get(viTri).getMaLoaiMon();
        ArrayList<MonAn> filterList = new ArrayList<MonAn>();
        for (MonAn monAn  : monAnArrayList) {
            if(monAn.getLoaiMon().contains(maLoaiMon) && monAn.getTenMon().toLowerCase().contains(tenMon.toLowerCase())){
                filterList.add(monAn);
            }
        }
        monAnAdapter.filterList(filterList);
    }

    // lay du lieu tu loai mon
    private void getDataLoaiMon() {
        LoaiMon tatCa = new LoaiMon("Tất cả");
        tatCa.setMaLoaiMon("");
        loaiMonArrayList.add(tatCa);
        databaseReference.child("LoaiMon").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    LoaiMon loaiMon = dataSnapshot.getValue(LoaiMon.class);
                    if (loaiMon != null) {
                        loaiMonArrayList.add(loaiMon);
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


//    //Hàm lấy dữ liệu từ màn hình RCV
    private void getDataMon() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Mon");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MonAn monAn = snapshot.getValue(MonAn.class);
                if(monAn != null){
                    monAnArrayList.add(0,monAn);
                    monAnAdapter.notifyDataSetChanged();

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
    //Hàm lọc theo tên



    private void setControl() {
        imgMon = findViewById(R.id.imgMon);
        actionBar = findViewById(R.id.actionBar);
        edtTimKiem = findViewById(R.id.Search);
        spMon = findViewById(R.id.spMon);
        btnThem = findViewById(R.id.btnThem);

    }
}
