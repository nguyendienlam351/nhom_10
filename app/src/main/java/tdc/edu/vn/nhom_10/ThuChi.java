package tdc.edu.vn.nhom_10;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import tdc.edu.vn.nhom_10.R;
import tdc.edu.vn.nhom_10.adapter.ThuChiAdapter;
import tdc.edu.vn.nhom_10.model.ChiThu;

public class ThuChi extends Fragment {
    private RecyclerView rcvThuChi;
    private ImageButton btnDate, btnThem;
    private TextView tvNgay;
    ArrayList<ChiThu> data = new ArrayList<ChiThu>();
    private ThuChiAdapter thuChiAdapter;
    private Calendar ngaythangnam;
    final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.thuchi,container,false);
        setControl(view);
        setEvent();
        return view;
    }

    private void setEvent() {
        getTime();
        KhoiTao();
        thuChiAdapter = new ThuChiAdapter(this,R.layout.layout_item_thu_chi,data);
        rcvThuChi.setAdapter(thuChiAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvThuChi.setLayoutManager(layoutManager);
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ThemMoiThuChi.class);
                startActivity(intent);
            }
        });
        thuChiAdapter.setDelegation(new ThuChiAdapter.ThuChiItemClickListener() {
            @Override
            public void itemClick(ChiThu thuchi) {
                Intent intent = new Intent(getContext(),ChiTietThuChi.class);
                Bundle bundle = new Bundle();
                bundle.putString("maTC", thuchi.getMaThuChi());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        ngaythangnam.set(year, month, dayOfMonth);
                        tvNgay.setText("Ngày bắt đầu : " + dateFormat.format(ngaythangnam.getTime()));
                        KhoiTao();
                        thuChiAdapter.notifyDataSetChanged();
                    }
                },ngaythangnam.get(Calendar.YEAR),ngaythangnam.get(Calendar.MONTH),ngaythangnam.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();
            }
        });
    }

    private void setControl(View view) {
        rcvThuChi = view.findViewById(R.id.rcvThuChi);
        btnDate = view.findViewById(R.id.btnDate);
        btnThem = view.findViewById(R.id.btnThem);
        tvNgay = view.findViewById(R.id.tvNgay);
        ngaythangnam = Calendar.getInstance();

    }
    private void KhoiTao(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ThuChi");
        data.clear();
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ChiThu chiThu = snapshot.getValue(ChiThu.class);
                if (chiThu != null){
                    String day = tvNgay.getText().toString().substring(15);
                    if(chiThu.getNgayNhap().compareTo(day)==0){
                        data.add(0,chiThu);
                        thuChiAdapter.notifyDataSetChanged();
                    }
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
    private void getTime() {
        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        int ngay = today.monthDay;
        int thang = today.month+1;
        int nam = today.year;
        if (ngay<10){
            tvNgay.setText("Ngày bắt đầu : 0"+ngay+"/"+thang+"/"+nam);
        }else{
            tvNgay.setText("Ngày bắt đầu : "+ngay+"/"+thang+"/"+nam);
        }
    }
}
