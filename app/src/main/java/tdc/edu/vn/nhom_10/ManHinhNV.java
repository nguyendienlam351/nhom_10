package tdc.edu.vn.nhom_10;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import tdc.edu.vn.nhom_10.adapter.MyRecylerViewNhanVien;
import tdc.edu.vn.nhom_10.model.NhanVien;

public class ManHinhNV extends AppCompatActivity {
    ImageButton btnThem;
    RecyclerView lvNhanVien;
    ArrayList<NhanVien> list = new ArrayList<NhanVien>();
    MyRecylerViewNhanVien adapter;
    Spinner spnChucVu;
    ArrayList<String> chucVu = new ArrayList<String>();
    DatabaseReference mData;
    SearchView svSearch;
    int viTri = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhan_vien);

        setControl();
        setEvent();

    }


    private void setEvent() {
        //
        //Gọi firebase
        getFirebase();
        chucVu.add("Tất cả");
        chucVu.add("Quản lý");
        chucVu.add("Phục vụ");
        chucVu.add("Pha chế");
        chucVu.add("Thu ngân");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, chucVu);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnChucVu.setAdapter(arrayAdapter);

        //
        adapter = new MyRecylerViewNhanVien(this, R.layout.activity_list1_nhan_vien, R.layout.activity_list2_nhan_vien, R.layout.activity_list3_nhan_vien, list);
        lvNhanVien.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        lvNhanVien.setLayoutManager(layoutManager);


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

        //Chuyển màn hình thêm
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), ManHinhThemNV.class);
                startActivity(intent);
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

    //Hàm lọc theo tên
    private void filter(String search) {
        String stChucVu = "";
        if (!chucVu.get(viTri).equals("Tất cả")) {
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

    //Hàm đọc firebase
    private void getFirebase() {
        // Write a message to the database
        mData = FirebaseDatabase.getInstance().getReference("NhanVien");
        mData.addChildEventListener(new ChildEventListener() {
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
        btnThem = findViewById(R.id.btnThem);
        lvNhanVien = findViewById(R.id.lvNhanVien);
        spnChucVu = findViewById(R.id.spnChucVu);
        svSearch = findViewById(R.id.svSearch);

    }
}