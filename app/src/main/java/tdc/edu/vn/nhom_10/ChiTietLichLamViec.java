package tdc.edu.vn.nhom_10;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import tdc.edu.vn.nhom_10.CustomView.CustomActionBar;
import tdc.edu.vn.nhom_10.adapter.ChiTietLichLamViecAdapter;
import tdc.edu.vn.nhom_10.model.NhanVien;
import tdc.edu.vn.nhom_10.model.TuanLamViec;

public class ChiTietLichLamViec extends AppCompatActivity {
    RecyclerView lvCTLichLamViec;
    TextView tvTenNV,tvSDT,tvChucVu,tvCa,tvNgay;
    ImageButton btnThem;
    ImageView imgAnhNV;
    TuanLamViec tuanLamViec;
    ArrayList<NhanVien>data = new ArrayList<>();
    ChiTietLichLamViecAdapter myRecyclerViewAdapter;
    DatabaseReference database;
    Calendar calendar;
    String maTuan,ca,tuan;
    int maThu;
    CustomActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_lich_lam_viec);
        setControl();
        setEvent();
    }
    private void getData(String maTuan,int maThu, String ca) {
        database.child("LichLamViec").child(maTuan).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TuanLamViec nTuanLamViec = snapshot.getValue(TuanLamViec.class);
                if (nTuanLamViec != null) {
                    tuanLamViec = nTuanLamViec;
                    data.clear();
                    if(ca.equals("caA")){
                        data.addAll(tuanLamViec.getCaLamViec().get(maThu).getCaA());
                    }
                    if(ca.equals("caB")){
                        data.addAll(tuanLamViec.getCaLamViec().get(maThu).getCaB());
                    }
                    if(ca.equals("caC")){
                        data.addAll(tuanLamViec.getCaLamViec().get(maThu).getCaC());
                    }
                    myRecyclerViewAdapter.notifyDataSetChanged();
                }
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
                Intent intent = new Intent(ChiTietLichLamViec.this, QuanLiLichLamViec.class);
                startActivity(intent);
            }
        });
        actionBar.setActionBarName("Chi ti???t l???ch l??m vi???c");

        SimpleDateFormat dinhDang = new SimpleDateFormat("dd/MM/yyyy");
        database = FirebaseDatabase.getInstance().getReference();
        myRecyclerViewAdapter = new ChiTietLichLamViecAdapter(this,R.layout.layout_item_chi_tiet_lich_lam_viec,data);
        myRecyclerViewAdapter.setDelegation(new ChiTietLichLamViecAdapter.MyItemClickListener() {
            @Override
            public void getXoaNV(NhanVien nhanVien) {
                openDiaLogDeleteItem(nhanVien);
            }
        });

        lvCTLichLamViec.setAdapter(myRecyclerViewAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        lvCTLichLamViec.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            maTuan = bundle.getString("maTuan", "");
            tuan=bundle.getString("tuan","");
            maThu=bundle.getInt("maThu",0);
            ca = bundle.getString("ca", "");
            getData(maTuan,maThu,ca);
            tvCa.setText(ca);

            int dot = tuan.lastIndexOf(' ');
            String base = (dot == -1) ? tuan : tuan.substring(0, dot);

            calendar = Calendar.getInstance();
            try {
                calendar.setTime(dinhDang.parse(base));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            calendar.roll(calendar.DAY_OF_YEAR,maThu);
            tvNgay.setText(dinhDang.format(calendar.getTime()));

        }
        Calendar tomorrowCalendar = Calendar.getInstance();
        Date date2 = tomorrowCalendar.getTime();

        if(calendar.getTime().compareTo(date2) < 0){
            myRecyclerViewAdapter.setKiemTra(false);
            btnThem.setVisibility(View.INVISIBLE);
        }
        else {
            btnThem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ChiTietLichLamViec.this, QuanLyNhanVienLichLamViec.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("maTuan",maTuan);
                    bundle.putString("tuan",tuan);
                    bundle.putInt("maThu", maThu);
                    bundle.putString("ca",ca);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }

    }
    //H??m xo??
    private void openDiaLogDeleteItem(NhanVien nhanVien){
        new AlertDialog.Builder(this)
                .setTitle("Xo??")
                .setMessage("B???n c?? mu???n xo???")
                .setPositiveButton("?????ng ??", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int ViTri = -1;
                        for(int i=0;i<data.size();i++){
                            if(data.get(i).getMaNV().equals(nhanVien.getMaNV())){
                                ViTri = i;
                                break;
                            }
                        }
                        if (ViTri != -1){
                            data.remove(ViTri);
                            database.child("LichLamViec").child(maTuan).child("caLamViec")
                                    .child(String.valueOf(maThu)).child(ca).setValue(data);
                        }
                    }
                })
                .setNegativeButton("T??? ch???i",null)
                .setCancelable(false)
                .show();
    }
    private void setControl() {
        tvCa=findViewById(R.id.tvcaA);
        tvNgay=findViewById(R.id.tvNgay);
        btnThem=findViewById(R.id.btnThem);
        tvTenNV=findViewById(R.id.tvTenNV);
        tvSDT=findViewById(R.id.tvSDT);
        tvChucVu=findViewById(R.id.tvChucVu);
        imgAnhNV=findViewById(R.id.imgAnhNV);
        lvCTLichLamViec=findViewById(R.id.lvCTlichLamViec);
        actionBar=findViewById(R.id.actionBar);
    }
}
