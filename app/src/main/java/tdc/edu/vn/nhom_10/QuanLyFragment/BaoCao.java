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

import tdc.edu.vn.nhom_10.DSHoatDongTrongNgay;
import tdc.edu.vn.nhom_10.DanhSachNguyenLieu;
import tdc.edu.vn.nhom_10.DanhSachNhapKho;
import tdc.edu.vn.nhom_10.DanhSachXuatKho;
import tdc.edu.vn.nhom_10.DoanhThu;
import tdc.edu.vn.nhom_10.KiemKeKho;
import tdc.edu.vn.nhom_10.R;
import tdc.edu.vn.nhom_10.TinhHinhKinhDoanh;

public class BaoCao extends Fragment {
    Button btnHoatDongTrongNgay, btnTinhHinhKinhDoanh, btnDoanhThu;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.baocao,container,false);
        setControl(view);
        setEvent();
        return view;
    }

    private void setEvent() {
        btnHoatDongTrongNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DSHoatDongTrongNgay.class);
                startActivity(intent);
            }
        });
        btnTinhHinhKinhDoanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TinhHinhKinhDoanh.class);
                startActivity(intent);
            }
        });
        btnDoanhThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DoanhThu.class);
                startActivity(intent);
            }
        });
    }

    private void setControl(View view) {
        btnHoatDongTrongNgay = view.findViewById(R.id.btnHoatDongTrongNgay);
        btnTinhHinhKinhDoanh = view.findViewById(R.id.btnTinhHinhKinhDoanh);
        btnDoanhThu = view.findViewById(R.id.btnDoanhThu);
    }

}
