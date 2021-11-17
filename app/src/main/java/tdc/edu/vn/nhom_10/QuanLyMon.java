package tdc.edu.vn.nhom_10;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
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


//    FirebaseDatabase data = FirebaseDatabase.getInstance();
//    DatabaseReference mData = data.getReference("Mon");


    private void setEvent() {
        // lay du lieu tu item
        monAnAdapter = new MonAnAdapter(this,R.layout.layout_item_ql_mon,monAnArrayList);
        imgMon.setAdapter(monAnAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        imgMon.setLayoutManager(layoutManager);
        getDataMon();
// spiner
        databaseReference = FirebaseDatabase.getInstance().getReference();
        actionBar.setDelegation(new CustomActionBar.ActionBarDelegation() {
            @Override
            public void backOnClick() {
                finish();
            }
        });
        actionBar.setActionBarName("MonAn");

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

    private void setControl() {
        imgMon = findViewById(R.id.imgMon);
        actionBar = findViewById(R.id.actionBar);
        edtTimKiem = findViewById(R.id.edtTimKiem);
        spMon = findViewById(R.id.spMon);
        btnThem = findViewById(R.id.btnThem);

    }
}
