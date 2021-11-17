package tdc.edu.vn.nhom_10;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import tdc.edu.vn.nhom_10.adapter.KiemKeKhoAdapter;
import tdc.edu.vn.nhom_10.model.Kiemkekho;

public class KiemKeKho extends AppCompatActivity {
    SearchView svSearch;
    Button btnChonTG;
    RecyclerView lvKiemKeKho;
    ArrayList<Kiemkekho> data = new ArrayList<Kiemkekho>();
    KiemKeKhoAdapter myRecyclerViewAdapter;
    DatabaseReference database;
    int Thang, Nam;
    int thangTam;
    int namTam;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kiem_ke_kho);
        setControl();
        setEvent();
        //list();
    }
//        private void list(){
//        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("KiemKeKho");
//        for (int i=1;i<5;i++){
//            String maHD= mDatabase.push().getKey();
//            Kiemkekho kiemkekho = new Kiemkekho();
//            kiemkekho.setTenNguyenLieu("Trà Sữa");
//            kiemkekho.setNhap(10+i);
//            kiemkekho.setXuat(3);
//            kiemkekho.setTon(kiemkekho.getNhap()-kiemkekho.getXuat());
//            kiemkekho.setNgayNhap("01/01/2021");
//            kiemkekho.setNgayXuat("05/05/2021");
//            mDatabase.child(maHD).setValue(kiemkekho);
//        }
//    }
    //Lấy dữ liệu firebase
    private void getlist(){
        database= FirebaseDatabase.getInstance().getReference("KiemKeKho");
        database.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Kiemkekho kiemkekho = snapshot.getValue(Kiemkekho.class);
                if(kiemkekho != null){
                    data.add(0,kiemkekho);
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
    }
    private void setEvent() {
        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        Thang = today.month+ 1;
        Nam = today.year;
        myRecyclerViewAdapter = new KiemKeKhoAdapter(this,R.layout.layout_item_kiem_ke_kho,data);
        lvKiemKeKho.setAdapter(myRecyclerViewAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        lvKiemKeKho.setLayoutManager(layoutManager);
        btnChonTG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(Gravity.CENTER);
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
        getlist();
    }
    //Hàm mở dialog
    private void openDialog(int gravity){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_chon_tg);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity=gravity;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(false);

        TextView tvThang =dialog.findViewById(R.id.tvThang);
        TextView tvNam =dialog.findViewById(R.id.tvNam);
        ImageButton imbGiamThang=dialog.findViewById(R.id.imbGiamThang);
        ImageButton imbTangThang=dialog.findViewById(R.id.imbTangThang);
        ImageButton imbGiamNam=dialog.findViewById(R.id.imbGiamNam);
        ImageButton imbTangNam=dialog.findViewById(R.id.imbTangNam);

        Button btnHuy =dialog.findViewById(R.id.btnHuy);
        Button btnDongY =dialog.findViewById(R.id.btnDongY);
        thangTam=Thang;
        namTam=Nam;
        tvThang.setText("Tháng: "+thangTam);
        tvNam.setText("Năm: "+namTam);
        //Sự kiện khi nhấn nút giảm của tháng
        imbGiamThang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(thangTam > 1){
                    thangTam--;
                    tvThang.setText("Tháng: "+thangTam);
                }
                else{
                    thangTam=12;
                    tvThang.setText("Tháng: "+thangTam);
                }
            }
        });
        //Sự kiện khi nhấn nút tăng của tháng
        imbTangThang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(thangTam < 12){
                    thangTam++;
                    tvThang.setText("Tháng: "+thangTam);
                }
                else{
                    thangTam=1;
                    tvThang.setText("Tháng: "+thangTam);
                }
            }
        });
        //Sự kiện khi nhấn nút giảm của năm
        imbGiamNam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(namTam > 2000){
                    namTam--;
                    tvNam.setText("Năm: "+namTam);
                }
            }
        });
        //Sự kiện khi nhấn nút tăng của năm
        imbTangNam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(namTam < 2999){
                    namTam++;
                    tvNam.setText("Năm: "+namTam);
                }
            }
        });
        //Sự kiện khi nhấn nút huỷ
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //Sự kiện khi nhấn nút đồng ý
        btnDongY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Time today = new Time(Time.getCurrentTimezone());
                today.setToNow();
                int nn = today.month;
                if(tvThang.getText() == String.valueOf(thangTam) && tvNam.getText() ==  String.valueOf(namTam)){

                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //Hàm tìm kiếm theo tên bàn
    private void filter(String tenNguyenLieu){
        ArrayList<Kiemkekho> filterList = new ArrayList<Kiemkekho>();
        for (Kiemkekho kiemkekho : data) {
            if(kiemkekho.getTenNguyenLieu().toLowerCase().contains(tenNguyenLieu.toLowerCase())){
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