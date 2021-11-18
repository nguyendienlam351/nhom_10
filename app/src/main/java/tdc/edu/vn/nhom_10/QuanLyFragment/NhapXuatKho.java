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

import tdc.edu.vn.nhom_10.DanhSachNguyenLieu;
import tdc.edu.vn.nhom_10.QuanLyBan;
import tdc.edu.vn.nhom_10.R;

public class NhapXuatKho extends Fragment {
    Button btnNguyenLieu, btnNhapKho, btnXuatKho, btnKiemKeKho;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nhapxuatkho,container,false);
        setControl(view);
        setEvent();
        return view;
    }

    private void setEvent() {
        btnNguyenLieu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DanhSachNguyenLieu.class);
                startActivity(intent);
            }
        });
        btnXuatKho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DanhSachNguyenLieu.class);
                startActivity(intent);
            }
        });
        btnNhapKho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DanhSachNguyenLieu.class);
                startActivity(intent);
            }
        });
        btnKiemKeKho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DanhSachNguyenLieu.class);
                startActivity(intent);
            }
        });
    }

    private void setControl(View view) {
        btnNguyenLieu = view.findViewById(R.id.btnNguyenLieu);
        btnNhapKho = view.findViewById(R.id.btnNhapkho);
        btnXuatKho = view.findViewById(R.id.btnXuatkho);
        btnKiemKeKho = view.findViewById(R.id.btnKiemKeKho);
    }
}
