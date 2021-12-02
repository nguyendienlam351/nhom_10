package tdc.edu.vn.nhom_10;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import tdc.edu.vn.nhom_10.CustomView.CustomActionBar;
import tdc.edu.vn.nhom_10.CustomView.CustomAlertDialog;
import tdc.edu.vn.nhom_10.CustomView.ThangNamDiaLog;
import tdc.edu.vn.nhom_10.adapter.TinhHinhKinhDoanhAdapter;
import tdc.edu.vn.nhom_10.model.ChiTietDonHang;
import tdc.edu.vn.nhom_10.model.DonHang;
import tdc.edu.vn.nhom_10.model.HoaDon;
import tdc.edu.vn.nhom_10.model.KinhDoanh;

public class TinhHinhKinhDoanh extends AppCompatActivity {
    RecyclerView lvDanhSach;
    Button btnChonTG;
    SearchView svSearch;
    CustomActionBar actionBar;
    ArrayList<KinhDoanh> kinhDoanhArrayList;
    TinhHinhKinhDoanhAdapter kinhDoanhAdapter;
    DatabaseReference database;
    ArrayList<ChiTietDonHang> chiTietDonHangArrayList;
    int month, year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tinh_hinh_kinh_doanh);
        setControl();
        setEvent();
    }

    private void setEvent() {
        //Actionbar
        actionBar.setDelegation(new CustomActionBar.ActionBarDelegation() {
            @Override
            public void backOnClick() {
                finish();
            }
        });
        actionBar.setActionBarName("Tình hình kinh doanh");

        database = FirebaseDatabase.getInstance().getReference();
        chiTietDonHangArrayList = new ArrayList<ChiTietDonHang>();

        //Danh sách tình hình kinh doanh
        kinhDoanhArrayList = new ArrayList<KinhDoanh>();
        kinhDoanhAdapter = new TinhHinhKinhDoanhAdapter(this, R.layout.layout_item_tinh_hinh_kinh_doanh, kinhDoanhArrayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        lvDanhSach.setLayoutManager(layoutManager);
        lvDanhSach.setAdapter(kinhDoanhAdapter);

        //Tạo dữ liệu tháng năm hiện tại
        Calendar calendar = Calendar.getInstance();
        month = calendar.get(Calendar.MONTH) + 1;
        year = calendar.get(Calendar.YEAR);
        btnChonTG.setText(month + "/" + year);

        //Lấy dữ liệu đơn hàng theo tháng năm
        getDataDonHang(month + "/" + year);

        //Dialog chọn tháng năm
        btnChonTG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThangNamDiaLog thangNamDiaLog = new ThangNamDiaLog(TinhHinhKinhDoanh.this, month, year,
                        new ThangNamDiaLog.ThangNamDiaLogListener() {
                            @Override
                            public void dongYClick(int thang, int nam) {
                                month = thang;
                                year = nam;

                                btnChonTG.setText(thang + "/" + nam);

                                getDataDonHang(thang + "/" + nam);
                            }
                        });
                thangNamDiaLog.show();
            }
        });

        //Tìm kiếm món theo tên
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

    //Lọc dữ liệu tình hình kinh doanh
    private void filter(String tenMon) {
        ArrayList<KinhDoanh> filterList = new ArrayList<KinhDoanh>();
        for (KinhDoanh kinhDoanh : kinhDoanhArrayList) {
            if(kinhDoanh.getTenMon().toLowerCase().contains(tenMon.toLowerCase())){
                filterList.add(kinhDoanh);
            }
        }

        kinhDoanhAdapter.filterList(filterList);
    }

    //Lấy dữ liệu tình hình kinh doanh
    private void getDataKinhDoanh() {
        for (ChiTietDonHang item : chiTietDonHangArrayList) {
            if (item.getTrangThai().equals("xong")) {
                int pos = timMaMon(item.getMaMon());
                if (pos == -1) {
                    KinhDoanh kinhDoanh = new KinhDoanh();
                    kinhDoanh.setMaMon(item.getMaMon());
                    kinhDoanh.setTenMon(item.getTenMon());
                    kinhDoanh.setSoLuong(item.getSoLuong());
                    kinhDoanhArrayList.add(kinhDoanh);
                } else {
                    kinhDoanhArrayList.get(pos).setSoLuong(kinhDoanhArrayList.get(pos).getSoLuong() + item.getSoLuong());
                }
            }
        }
    }

    //Tìm món theo mã món
    private int timMaMon(String maMon) {
        for (int i = 0; i < kinhDoanhArrayList.size(); i++) {
            if (kinhDoanhArrayList.get(i).getMaMon().equals(maMon)) {
                return i;
            }
        }
        return -1;
    }

    //Lấy dữ liệu đơn hàng
    private void getDataDonHang(String ngay) {
        database.child("HoaDon").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                kinhDoanhArrayList.clear();
                chiTietDonHangArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    HoaDon hoaDon = dataSnapshot.getValue(HoaDon.class);
                    if (hoaDon.getNgayThang().contains(ngay)) {
                        chiTietDonHangArrayList.addAll(hoaDon.getDonHang().getChiTietDonHang());
                    }
                }
                getDataKinhDoanh();
                kinhDoanhAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setControl() {
        lvDanhSach = findViewById(R.id.lvDanhSach);
        btnChonTG = findViewById(R.id.btnChonTG);
        svSearch = findViewById(R.id.svSearch);
        actionBar = findViewById(R.id.actionBar);
    }
}