package tdc.edu.vn.nhom_10;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.SearchView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import tdc.edu.vn.nhom_10.CustomView.CustomActionBar;
import tdc.edu.vn.nhom_10.adapter.XemHoaDonAdapter;
import tdc.edu.vn.nhom_10.model.Ban;
import tdc.edu.vn.nhom_10.model.DonHang;
import tdc.edu.vn.nhom_10.model.HoaDon;

public class XemHoaDon extends AppCompatActivity {
    SearchView svTimKiem;
    RecyclerView lvHoaDon;
    ArrayList<HoaDon> data = new ArrayList<HoaDon>();
    XemHoaDonAdapter myRecyclerViewAdapter;
    DatabaseReference database;
    CustomActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_hoa_don);
        setControl();
        setEvent();
    }
    //Lấy dữ liệu firebase
    private void getlist(){
        database= FirebaseDatabase.getInstance().getReference("HoaDon");
        database.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                HoaDon hoaDon = snapshot.getValue(HoaDon.class);
                if(hoaDon != null){
                    data.add(0,hoaDon);
                    myRecyclerViewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                HoaDon hoaDon = snapshot.getValue(HoaDon.class);
                if(hoaDon == null || data==null||data.isEmpty()){
                    return;
                }
                for (int i = 0;i<data.size();i++){
                    if(hoaDon.getMaHoaDon()==data.get(i).getMaHoaDon()){
                        data.remove(data.get(i));
                        break;
                    }
                }
                myRecyclerViewAdapter.notifyDataSetChanged();
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
        actionBar.setDelegation(new CustomActionBar.ActionBarDelegation() {
            @Override
            public void backOnClick() {
                finish();
            }
        });

        actionBar.setActionBarName("Hoá đơn");
        myRecyclerViewAdapter = new XemHoaDonAdapter(this,R.layout.layout_item_hoa_don,data);
        myRecyclerViewAdapter.setDelegation(new XemHoaDonAdapter.MyItemClickListener() {
            @Override
            public void getDeleteHoaDon(HoaDon hoadon) {
                openDiaLogDeleteItem(hoadon);
            }
        });
        lvHoaDon.setAdapter(myRecyclerViewAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        lvHoaDon.setLayoutManager(layoutManager);
        svTimKiem.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        getlist();
    }
    //Hàm tìm kiếm theo tên bàn
    private void filter(String tenBan){
        ArrayList<HoaDon> filterList = new ArrayList<HoaDon>();
        for (HoaDon hoaDon : data) {
            if(hoaDon.getDonHang().getTenBan().toLowerCase().contains(tenBan.toLowerCase())){
                filterList.add(hoaDon);
            }
        }

        myRecyclerViewAdapter.filterList(filterList);
    }
    //Hàm xoá
    private void openDiaLogDeleteItem(HoaDon hoaDon){
        new AlertDialog.Builder(this)
                .setTitle("Xoá")
                .setMessage("Bạn có muốn xoá?")
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        database= FirebaseDatabase.getInstance().getReference("HoaDon");
                        database.child(String.valueOf(hoaDon.getMaHoaDon())).removeValue();
                    }

                })
                .setNegativeButton("Từ chối",null)
                .setCancelable(false)
                .show();
    }

    private void setControl() {
        svTimKiem=findViewById(R.id.svTimKiem);
        lvHoaDon=findViewById(R.id.lvHoaDon);
        actionBar = findViewById(R.id.actionBar);
    }
}