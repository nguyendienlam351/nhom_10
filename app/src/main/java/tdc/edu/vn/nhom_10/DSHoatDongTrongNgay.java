package tdc.edu.vn.nhom_10;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

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

import tdc.edu.vn.nhom_10.CustomView.CustomActionBar;
import tdc.edu.vn.nhom_10.adapter.HoatDongTrongNgayAdapter;
import tdc.edu.vn.nhom_10.model.HoatDongTrongNgay;
import tdc.edu.vn.nhom_10.model.NhapKho;

public class DSHoatDongTrongNgay extends AppCompatActivity {
    ImageButton btnThem;
    RecyclerView lvHDTN;
    ArrayList<HoatDongTrongNgay> list = new ArrayList<HoatDongTrongNgay>();
    HoatDongTrongNgayAdapter adapter;
    DatabaseReference mData;
    SearchView svSearch;
    CustomActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoat_dong_trong_ngay);

        setControl();
        setEvent();

    }


    private void setEvent() {
        actionBar.setDelegation(new CustomActionBar.ActionBarDelegation() {
            @Override
            public void backOnClick() {
                Intent intent = new Intent(getApplicationContext(), QuanLy.class);
                Bundle bundle = new Bundle();
                bundle.putInt("ManHinh", 2);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        actionBar.setActionBarName("Hoạt động trong ngày");

        //Gọi firebase
        getFirebase();

        //
        adapter = new HoatDongTrongNgayAdapter(this, R.layout.layout_item_hoat_dong_trong_ngay, list);
        lvHDTN.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        lvHDTN.setLayoutManager(layoutManager);

    }

    //Hàm đọc firebase
    private void getFirebase() {
        // Write a message to the database
        mData = FirebaseDatabase.getInstance().getReference("HoatDongTrongNgay");

//        for(int i= 1; i < 6;i++){
//            HoatDongTrongNgay hoatDongTrongNgay = new HoatDongTrongNgay();
//            hoatDongTrongNgay.setTenHD("quy");
//            hoatDongTrongNgay.setNgay("10/12/2001");
//            hoatDongTrongNgay.setMoTa("Nhập 10 đơn vị");
//            mData.push().setValue(hoatDongTrongNgay);
//        }
        mData.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                HoatDongTrongNgay hoatDongTrongNgay = snapshot.getValue(HoatDongTrongNgay.class);
                if (hoatDongTrongNgay != null) {
                    list.add(0,hoatDongTrongNgay);
                    adapter.notifyDataSetChanged();
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
        btnThem = findViewById(R.id.btnThem);
        lvHDTN = findViewById(R.id.lvHDTN);
        svSearch = findViewById(R.id.svSearch);
        actionBar = findViewById(R.id.actionBar);
    }
}