package tdc.edu.vn.nhom_10.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tdc.edu.vn.nhom_10.ChiTietXuatKho;
import tdc.edu.vn.nhom_10.R;
import tdc.edu.vn.nhom_10.model.XuatKho;

public class XuatKhoAdapter extends RecyclerView.Adapter<XuatKhoAdapter.MyViewHolder> {
    Activity conText;
    int layoutID;
    ArrayList<XuatKho> data;

    private MyItemClickListener delegation;

    public void setDelegation(MyItemClickListener delegation) {
        this.delegation = delegation;
    }

    public XuatKhoAdapter(Activity conText, int layoutID, ArrayList<XuatKho> data) {
        this.data = data;
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
        XuatKho xuatKho = data.get(position);
        holder.tvTen.setText(xuatKho.getTenXuatKho());
        holder.tvSoLuong.setText(xuatKho.getSoLuong() + " " + xuatKho.getDonVi());
        holder.tvNgay.setText(xuatKho.getNgayXuatKho());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Chuyển màn hình
                Intent intent = new Intent(conText.getApplicationContext(), ChiTietXuatKho.class);
                Bundle bundle = new Bundle();

                bundle.putString("MaXuatKho", xuatKho.getMaXuatKho());
                intent.putExtras(bundle);
                conText.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {

        return layoutID;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    //Search
    public void filterList(ArrayList<XuatKho> xuatKhoArrayList) {
        this.data = xuatKhoArrayList;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTen, tvSoLuong, tvNgay;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTen = itemView.findViewById(R.id.tvTen);
            tvSoLuong = itemView.findViewById(R.id.tvSoLuong);
            tvNgay = itemView.findViewById(R.id.tvNgay);
        }
    }

    public interface MyItemClickListener {
        void getThongTinDMK(XuatKho xuatKho);
    }

}
