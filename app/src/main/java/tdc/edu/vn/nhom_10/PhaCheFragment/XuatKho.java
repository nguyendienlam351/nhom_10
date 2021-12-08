package tdc.edu.vn.nhom_10.PhaCheFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import tdc.edu.vn.nhom_10.CustomView.CustomActionBar;
import tdc.edu.vn.nhom_10.DanhSachNguyenLieuXuat;
import tdc.edu.vn.nhom_10.DanhSachNguyenLieuXuatPhaChe;
import tdc.edu.vn.nhom_10.QuanLy;
import tdc.edu.vn.nhom_10.R;
import tdc.edu.vn.nhom_10.adapter.XuatKhoAdapter;

public class XuatKho extends Fragment {
    ImageButton btnThem;
    RecyclerView lvXuatKho;
    ArrayList<tdc.edu.vn.nhom_10.model.XuatKho> list = new ArrayList<tdc.edu.vn.nhom_10.model.XuatKho>();
    XuatKhoAdapter adapter;
    DatabaseReference mData;
    SearchView svSearch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.xuatkho,container,false);
        setControl(view);
        setEvent();
        return view;
    }

    private void setEvent() {
        //Gọi firebase
        getFirebase();

        //
        adapter = new XuatKhoAdapter(getActivity(), R.layout.layout_item_xuat_kho, list);
        lvXuatKho.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        lvXuatKho.setLayoutManager(layoutManager);


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

                Intent intent = new Intent(getActivity(), DanhSachNguyenLieuXuatPhaChe.class);
                startActivity(intent);
            }
        });
    }

    //Hàm lọc theo tên
    private void filter(String search) {
        ArrayList<tdc.edu.vn.nhom_10.model.XuatKho> filterList = new ArrayList<>();
        for (tdc.edu.vn.nhom_10.model.XuatKho xuatKho : list) {
            if (xuatKho.getTenXuatKho().toLowerCase().contains(search.toLowerCase())) {
                filterList.add(xuatKho);
            }
            adapter.filterList(filterList);
        }
    }

    //Hàm đọc firebase
    private void getFirebase() {
        // Write a message to the database
        mData = FirebaseDatabase.getInstance().getReference("XuatKho");
        mData.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                tdc.edu.vn.nhom_10.model.XuatKho xuatKho = snapshot.getValue(tdc.edu.vn.nhom_10.model.XuatKho.class);
                if (xuatKho != null) {
                    list.add(0,xuatKho);
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


    private void setControl(View view) {
        btnThem = view.findViewById(R.id.btnThem);
        lvXuatKho = view.findViewById(R.id.lvXuatKho);
        svSearch = view.findViewById(R.id.svSearch);
    }
}
