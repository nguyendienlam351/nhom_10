package tdc.edu.vn.nhom_10.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tdc.edu.vn.nhom_10.R;
import tdc.edu.vn.nhom_10.model.HoatDongTrongNgay;

public class HoatDongTrongNgayAdapter extends RecyclerView.Adapter<HoatDongTrongNgayAdapter.MyViewHolder> {
    Activity conText;
    int layoutID;
    ArrayList<HoatDongTrongNgay> data;

    private MyItemClickListener delegation;

    public void setDelegation(MyItemClickListener delegation) {
        this.delegation = delegation;
    }

    public HoatDongTrongNgayAdapter(Activity conText, int layoutID, ArrayList<HoatDongTrongNgay> data) {
        this.data = data;
        this.layoutID = layoutID;
        this.conText = conText;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = conText.getLayoutInflater();
        CardView view = (CardView) layoutInflater.inflate(viewType, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        HoatDongTrongNgay hoatDongTrongNgay = data.get(position);
        holder.tvTen.setText(hoatDongTrongNgay.getTenHD());
        holder.tvNgay.setText(hoatDongTrongNgay.getNgay());
        holder.tvMoTa.setText(hoatDongTrongNgay.getMoTa());
    }

    @Override
    public int getItemViewType(int position) {

        return layoutID;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTen, tvNgay, tvMoTa;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTen = itemView.findViewById(R.id.tvTen);
            tvNgay = itemView.findViewById(R.id.tvNgay);
            tvMoTa = itemView.findViewById(R.id.tvMoTa);
        }
    }

    public interface MyItemClickListener {
        void getThongTinHDTN(HoatDongTrongNgay nhapKho);
    }

}