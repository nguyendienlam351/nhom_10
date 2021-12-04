package tdc.edu.vn.nhom_10.CustomView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import tdc.edu.vn.nhom_10.R;
import tdc.edu.vn.nhom_10.adapter.MaGiamGiaAdapter;
import tdc.edu.vn.nhom_10.model.MaGiamGia;

public class MaGiamGiaDialog extends AlertDialog {
    Context context;
    ArrayList<MaGiamGia> maGiamGiaArrayList;
    MaGiamGiaAdapter maGiamGiaAdapter;
    DatabaseReference database;
    Button btnDong;
    RecyclerView lvDanhSachGiamGia;
    SearchView edtTimKiem;
    MaGiamGiaDialog.MaGiamGiaDiaLogListener listener;

    public MaGiamGiaDialog(@NonNull Context context,MaGiamGiaDialog.MaGiamGiaDiaLogListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dialog_ma_giam_gia);

        Rect displayRectangle = new Rect();
        Window window = this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        this.getWindow().setLayout(window.getAttributes().width, (int)(displayRectangle.height() * 0.8f));

        btnDong = findViewById(R.id.btnDong);
        lvDanhSachGiamGia = findViewById(R.id.lvDanhSachGiamGia);
        edtTimKiem = findViewById(R.id.edtTimKiem);

        database = FirebaseDatabase.getInstance().getReference("MaGiamGia");

        maGiamGiaArrayList = new ArrayList<MaGiamGia>();
        maGiamGiaAdapter = new MaGiamGiaAdapter((Activity) context, R.layout.layout_item_ma_giam_gia_2, maGiamGiaArrayList);
        maGiamGiaAdapter.setDelegation(new MaGiamGiaAdapter.MyItemClickListener() {
            @Override
            public void getMaGiamGia(MaGiamGia maGiamGia) {
                listener.itemClick(maGiamGia);
                dismiss();
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        lvDanhSachGiamGia.setLayoutManager(layoutManager);
        lvDanhSachGiamGia.setAdapter(maGiamGiaAdapter);

        getDataMaGiamGia();

        //Tìm kiếm mã giảm giá theo tên
        edtTimKiem.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        btnDong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    //Lọc mã giảm giá
    private void filter(String tenMa) {
        ArrayList<MaGiamGia> filterList = new ArrayList<MaGiamGia>();
        for (MaGiamGia maGiamGia : maGiamGiaArrayList) {
            if (maGiamGia.getTenMaGiamGia().toLowerCase().contains(tenMa.toLowerCase())) {
                filterList.add(maGiamGia);
            }
        }

        maGiamGiaAdapter.filterList(filterList);
    }

    private void getDataMaGiamGia() {

        database.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MaGiamGia maGiamGia = snapshot.getValue(MaGiamGia.class);
                maGiamGiaArrayList.add(0, maGiamGia);
                maGiamGiaAdapter.notifyDataSetChanged();
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


    public interface MaGiamGiaDiaLogListener {
        public void itemClick(MaGiamGia maGiamGia);
    }
}
