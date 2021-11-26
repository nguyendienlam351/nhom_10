package tdc.edu.vn.nhom_10.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tdc.edu.vn.nhom_10.ChiTietLichLamViec;
import tdc.edu.vn.nhom_10.ChiTietNhanVien;
import tdc.edu.vn.nhom_10.R;
import tdc.edu.vn.nhom_10.model.NhanVien;

public class NhanVienLichLamViecAdapter extends RecyclerView.Adapter<NhanVienLichLamViecAdapter.MyViewHolder> {
    Activity conText;
    int layoutID;
    ArrayList<NhanVien> datanhanVien;

    private MyItemClickListener delegation;

    public void setDelegation(MyItemClickListener delegation) {
        this.delegation = delegation;
    }

    public NhanVienLichLamViecAdapter(Activity conText, int layoutID, ArrayList<NhanVien> datanhanVien) {
        this.datanhanVien = datanhanVien;
        this.layoutID = layoutID;
        this.conText = conText;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = conText.getLayoutInflater();
        LinearLayout view = (LinearLayout) layoutInflater.inflate(viewType, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        NhanVien nhanVien = datanhanVien.get(position);
        holder.tvHoTen.setText(nhanVien.getHoTen());
        holder.tvSDT.setText(nhanVien.getSoDienThoai());
        holder.tvNgaySinh.setText(nhanVien.getNgaySinh());
        holder.btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delegation.getThemNV(nhanVien);
                //Chuyển màn hình
//                Intent intent = new Intent(conText.getApplicationContext(), ChiTietLichLamViec.class);
//                Bundle bundle = new Bundle();
//
//                bundle.putString("MaNV", nhanVien.getMaNV());
//                intent.putExtras(bundle);
//                conText.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return layoutID;
    }

    @Override
    public int getItemCount() {
        return datanhanVien.size();
    }

    //Search
    public void filterList(ArrayList<NhanVien> nhanVienArrayList) {
        this.datanhanVien = nhanVienArrayList;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvHoTen, tvSDT, tvNgaySinh;
        ImageButton btnThem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHoTen = itemView.findViewById(R.id.tvHoTen);
            tvSDT = itemView.findViewById(R.id.tvSDT);
            tvNgaySinh = itemView.findViewById(R.id.tvNgaySinh);
            btnThem = itemView.findViewById(R.id.btnThem);
        }
    }

    public interface MyItemClickListener {
        void getThemNV(NhanVien nhanVien);
    }

}
