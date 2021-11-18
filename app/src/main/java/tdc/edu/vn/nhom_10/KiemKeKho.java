package tdc.edu.vn.nhom_10;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.text.format.Time;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import tdc.edu.vn.nhom_10.CustomView.ChonThangNamDialog;
import tdc.edu.vn.nhom_10.CustomView.QuenMatKhauDialog;
import tdc.edu.vn.nhom_10.adapter.KiemKeKhoAdapter;
import tdc.edu.vn.nhom_10.model.KiemKe;
import tdc.edu.vn.nhom_10.model.NguyenLieu;
import tdc.edu.vn.nhom_10.model.NhapKho;
import tdc.edu.vn.nhom_10.model.XuatKho;

public class KiemKeKho extends AppCompatActivity {
    SearchView svSearch;
    Button btnChonTG;
    RecyclerView lvKiemKeKho;
    ArrayList<KiemKe> data = new ArrayList<KiemKe>();
    ArrayList<NhapKho> data1 = new ArrayList<NhapKho>();
    ArrayList<XuatKho> data2 = new ArrayList<XuatKho>();
    ArrayList<NguyenLieu> data3 = new ArrayList<NguyenLieu>();
    KiemKeKhoAdapter myRecyclerViewAdapter;
    DatabaseReference database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kiem_ke_kho);
        setControl();
        setEvent();
    }
    private void setEvent() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("NhapKho");
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("XuatKho");
        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("NguyenLieu");

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                NhapKho nhapKho = snapshot.getValue(NhapKho.class);
                if(nhapKho != null){
                    data1.add(0,nhapKho);
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

        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                XuatKho xuatKho = snapshot.getValue(XuatKho.class);
                if(xuatKho != null){
                    data2.add(0,xuatKho);
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

        reference2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                NguyenLieu nguyenLieu = snapshot.getValue(NguyenLieu.class);
                if(nguyenLieu != null){
                    data3.add(0,nguyenLieu);
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

        myRecyclerViewAdapter = new KiemKeKhoAdapter(this,R.layout.layout_item_kiem_ke_kho,data);
        lvKiemKeKho.setAdapter(myRecyclerViewAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        lvKiemKeKho.setLayoutManager(layoutManager);

        btnChonTG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chonTG();
            }
        });
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
    }
    private void chonTG()  {
        ChonThangNamDialog.FullNameListener listener = new ChonThangNamDialog.FullNameListener() {
            @Override
            public void fullNameEntered(String email) {
                data.clear();
                for (int i = 0; i< data3.size();i++){
                    KiemKe kiemKe = new KiemKe();
                    kiemKe.setMa("ma"+i);
                    NguyenLieu nguyenLieu = data3.get(i);
                    kiemKe.setTen(nguyenLieu.getTenNL());
                    kiemKe.setThangnam(email);
                    int nhap = 0;
                    int xuat = 0;
                    for(int j = 0; j<data1.size();j++){
                        NhapKho nhapKho = data1.get(j);
                        String ngaythang = nhapKho.getNgayNhapKho();
                        if(nguyenLieu.getTenNL().compareTo(nhapKho.getTenNhapKho()) == 0 && ngaythang.contains(email) == true){
                            nhap = nhap + nhapKho.getSoLuong();
                        }
                    }
                    kiemKe.setNhap(nhap);
                    for(int l = 0; l<data2.size();l++){
                        XuatKho xuatKho = data2.get(l);
                        String ngaythang = xuatKho.getNgayXuatKho();
                        if(nguyenLieu.getTenNL().compareTo(xuatKho.getTenXuatKho()) == 0 && ngaythang.contains(email)==true){
                            xuat = xuat + xuatKho.getSoLuong();
                        }
                    }
                    kiemKe.setXuat(xuat);
                    kiemKe.setTon(nguyenLieu.getSoLuong());
                    data.add(kiemKe);
                    myRecyclerViewAdapter.notifyDataSetChanged();
                }
            }
        };
        final ChonThangNamDialog dialog = new ChonThangNamDialog(this, listener);

        dialog.show();
    }
    //Hàm tìm kiếm theo tên bàn
    private void filter(String tenNguyenLieu){
        ArrayList<KiemKe> filterList = new ArrayList<KiemKe>();
        for (KiemKe kiemkekho : data) {
            if(kiemkekho.getTen().toLowerCase().contains(tenNguyenLieu.toLowerCase())){
                filterList.add(kiemkekho);
            }
        }

        myRecyclerViewAdapter.filterList(filterList);
    }
    private void setControl() {
        svSearch=findViewById(R.id.svSearch);
        lvKiemKeKho=findViewById(R.id.lvKiemKeKho);
        btnChonTG=findViewById(R.id.btnChonTG);
    }
}