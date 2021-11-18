package tdc.edu.vn.nhom_10.QuanLyFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import tdc.edu.vn.nhom_10.QuanLyBan;
import tdc.edu.vn.nhom_10.QuanLyLoaiMon;
import tdc.edu.vn.nhom_10.QuanLyMaGiamGia;
import tdc.edu.vn.nhom_10.QuanLyNhanVien;
import tdc.edu.vn.nhom_10.R;

public class QuanLyBanHang extends Fragment {
    Button btnMaGiamGia;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.quanlybanhang,container,false);
        setControl(view);
        setEvent();
        return view;
    }
    private void setEvent(){
        btnMaGiamGia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), QuanLyMaGiamGia.class);
                startActivity(intent);
            }
        });
    }

    private void setControl(View view){
        btnMaGiamGia = view.findViewById(R.id.btnMaGiamGia);
    }
}
