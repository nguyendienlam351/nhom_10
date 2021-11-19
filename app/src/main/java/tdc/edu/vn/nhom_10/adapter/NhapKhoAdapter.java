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

import tdc.edu.vn.nhom_10.ChiTietNhapKho;
import tdc.edu.vn.nhom_10.R;
import tdc.edu.vn.nhom_10.model.NhapKho;

public class NhapKhoAdapter extends RecyclerView.Adapter<NhapKhoAdapter.MyViewHolder> {
    Activity conText;
    int layoutID;
    ArrayList<NhapKho> data;

    private MyItemClickListener delegation;

    public void setDelegation(MyItemClickListener delegation) {
        this.delegation = delegation;
    }

    public NhapKhoAdapter(Activity conText, int layoutID, ArrayList<NhapKho> data) {
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
        NhapKho nhapKho = data.get(position);
        holder.tvTen.setText(nhapKho.getTenNhapKho());
        holder.tvSoLuong.setText(nhapKho.getSoLuong() + " " + nhapKho.getDonVi());
        holder.tvNgay.setText(nhapKho.getNgayNhapKho());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Chuyển màn hình
                Intent intent = new Intent(conText.getApplicationContext(), ChiTietNhapKho.class);
                Bundle bundle = new Bundle();

                bundle.putString("MaNhapKho", nhapKho.getMaNhapKho());
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
    public void filterList(ArrayList<NhapKho> nhapKhoArrayList) {
        this.data = nhapKhoArrayList;
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
        void getThongTinDMK(NhapKho nhapKho);
    }

}
