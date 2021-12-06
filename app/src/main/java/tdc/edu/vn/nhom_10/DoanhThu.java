package tdc.edu.vn.nhom_10;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import tdc.edu.vn.nhom_10.CustomView.CustomActionBar;
import tdc.edu.vn.nhom_10.adapter.DoanhThuAdapter;

import tdc.edu.vn.nhom_10.model.ChiThu;
import tdc.edu.vn.nhom_10.model.DoanhThuMD;


public class DoanhThu extends AppCompatActivity {

    RecyclerView lvDoanhThu;
    ArrayList<DoanhThuMD> data = new ArrayList<DoanhThuMD>();
    DoanhThuAdapter myRecyclerViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doanh_thu);
        setControl();
        setEvent();
    }
    private int timDT(String ngay ){
        for(int i =0; i < data.size(); i++)
        {
            if(ngay.equals(data.get(i).getNgay() ))
            {
                return i;
            }
        }

        return -1;
    }
    private void setEvent() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ThuChi");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ChiThu thuChi = snapshot.getValue(ChiThu.class);
                if (thuChi != null) {
                    int TimDoanhThu = timDT(thuChi.getNgayNhap());
                    if(TimDoanhThu  == -1)
                    {
                        DoanhThuMD doanhThu = new DoanhThuMD();
                        doanhThu.setNgay(thuChi.getNgayNhap());
                        if(thuChi.getLoaiThuChi().equals("Chi"))
                        {
                            doanhThu.setChi(thuChi.getSoTien()) ;
                        }
                        else
                        {
                            doanhThu.setThu(thuChi.getSoTien());

                        }
                        data.add(0, doanhThu);
                    }
                    else
                    {
                        if(thuChi.getLoaiThuChi().equals("Chi"))
                        {
                            data.get(TimDoanhThu).setChi( data.get(TimDoanhThu).getChi()+ thuChi.getSoTien()) ;
                        }
                        else
                        {
                            data.get(TimDoanhThu).setThu( data.get(TimDoanhThu).getThu() + thuChi.getSoTien()); ;
                        }
                    }
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
        myRecyclerViewAdapter = new DoanhThuAdapter(this, R.layout.layout_item_doanh_thu, data);
        lvDoanhThu.setAdapter(myRecyclerViewAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        lvDoanhThu.setLayoutManager(layoutManager);


    }


//    private void chonTG()  {
//        ChonThangNamDialog.FullNameListener listener = new ChonThangNamDialog.FullNameListener() {
//            @Override
//            public void fullNameEntered(String gopTN) {
//                btnChonTG.setText(gopTN);
//                data.clear();
//                for (int i = 0; i< data3.size();i++){
//                    KiemKe kiemKe = new KiemKe();
//                    kiemKe.setMa("ma"+i);
//                    NguyenLieu nguyenLieu = data3.get(i);
//                    kiemKe.setTen(nguyenLieu.getTenNL());
//                    kiemKe.setThangnam(gopTN);
//                    int nhap = 0;
//                    int xuat = 0;
//                    for(int j = 0; j<data1.size();j++){
//                        NhapKho nhapKho = data1.get(j);
//                        String ngaythang = nhapKho.getNgayNhapKho();
//                        if(nguyenLieu.getTenNL().compareTo(nhapKho.getTenNhapKho()) == 0 && ngaythang.contains(gopTN) == true){
//                            nhap = nhap + nhapKho.getSoLuong();
//                        }
//                    }
//                    kiemKe.setNhap(nhap);
//                    for(int l = 0; l<data2.size();l++){
//                        XuatKho xuatKho = data2.get(l);
//                        String ngaythang = xuatKho.getNgayXuatKho();
//                        if(nguyenLieu.getTenNL().compareTo(xuatKho.getTenXuatKho()) == 0 && ngaythang.contains(gopTN)==true){
//                            xuat = xuat + xuatKho.getSoLuong();
//                        }
//                    }
//                    kiemKe.setXuat(xuat);
//                    kiemKe.setTon(nguyenLieu.getSoLuong());
//                    data.add(kiemKe);
//                    myRecyclerViewAdapter.notifyDataSetChanged();
//                }
//            }
//        };
//        final ChonThangNamDialog dialog = new ChonThangNamDialog(this, listener);
//
//        dialog.show();

    private void setControl() {
        lvDoanhThu = findViewById(R.id.lvDoanhThu);

    }
}