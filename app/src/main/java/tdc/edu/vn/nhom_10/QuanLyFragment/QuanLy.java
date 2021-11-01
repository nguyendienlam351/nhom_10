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

import tdc.edu.vn.nhom_10.DanhSachMon;
import tdc.edu.vn.nhom_10.DonHangCuaBan;
import tdc.edu.vn.nhom_10.MainBan;
import tdc.edu.vn.nhom_10.MainLoaiMon;
import tdc.edu.vn.nhom_10.R;

public class QuanLy extends Fragment {
    Button btnQuanLyBan;
    Button btnQuanLyLoaiMon;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.quanly,container,false);
        setControl(view);
        setEvent();
        return view;
    }

    private void setEvent(){
        btnQuanLyBan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainBan.class);
                startActivity(intent);
            }
        });
        btnQuanLyLoaiMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainLoaiMon.class);
                startActivity(intent);
            }
        });
    }

    private void setControl(View view){
        btnQuanLyBan = view.findViewById(R.id.btnQuanLyBan);
        btnQuanLyLoaiMon = view.findViewById(R.id.btnQuanLyLoaiMon);
    }
}
