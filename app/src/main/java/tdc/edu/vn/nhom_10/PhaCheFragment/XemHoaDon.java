package tdc.edu.vn.nhom_10.PhaCheFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

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

import java.util.ArrayList;

import tdc.edu.vn.nhom_10.DanhSachDonHang;
import tdc.edu.vn.nhom_10.R;
import tdc.edu.vn.nhom_10.adapter.DonHangAdater;
import tdc.edu.vn.nhom_10.model.DonHang;

public class XemHoaDon extends Fragment {
    SearchView edtTimKiem;
    RecyclerView lvDanhSachBan;
    DonHangAdater donHangAdater;
    ArrayList<DonHang> donHangArrayList;
    DatabaseReference database;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_xem_don_hang,container,false);

        setControl(view);
        setEvent();

        return view;
    }

    private void setEvent() {
        donHangArrayList = new ArrayList<DonHang>();
        database = FirebaseDatabase.getInstance().getReference("Ban");
        donHangAdater = new DonHangAdater(getActivity(), R.layout.layout_item_ban_2, donHangArrayList);

        donHangAdater.setDelegation(new DonHangAdater.MyItemClickListener() {
            @Override
            public void getBan(DonHang donHang) {
                Intent intent = new Intent(getActivity(), DanhSachDonHang.class);
                Bundle bundle = new Bundle();
                bundle.putString("maBan", donHang.getMaBan());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        layoutManager.setOrientation(RecyclerView.VERTICAL);

        lvDanhSachBan.setLayoutManager(layoutManager);

        lvDanhSachBan.setAdapter(donHangAdater);

        getDataDonHang();

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
    }

    private void filter(String tenBan){
        ArrayList<DonHang> filterList = new ArrayList<DonHang>();
        for (DonHang donHang : donHangArrayList) {
            if(donHang.getTenBan().toLowerCase().contains(tenBan.toLowerCase())){
                filterList.add(donHang);
            }
        }

        donHangAdater.filterList(filterList);
    }

    private void getDataDonHang() {

        database.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                DonHang donHang = snapshot.getValue(DonHang.class);
                donHangArrayList.add(0, donHang);
                donHangAdater.notifyDataSetChanged();
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
        lvDanhSachBan = view.findViewById(R.id.lvDanhSachBan);
        edtTimKiem = view.findViewById(R.id.edtTimKiem);
    }
}