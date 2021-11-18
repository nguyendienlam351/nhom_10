package tdc.edu.vn.nhom_10.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tdc.edu.vn.nhom_10.R;
import tdc.edu.vn.nhom_10.model.KiemKe;

public class KiemKeKhoAdapter extends RecyclerView.Adapter<KiemKeKhoAdapter.MyViewHolder>{
    private Activity context;
    private int layoutID;
    private ArrayList<KiemKe> kiemKeKhoArrayList;

    public KiemKeKhoAdapter(Activity context, int layoutID, ArrayList<KiemKe> kiemKeKhoArrayList){
        this.context= context;
        this.layoutID=layoutID;
        this.kiemKeKhoArrayList=kiemKeKhoArrayList;
    }
    @NonNull
    @Override
    public KiemKeKhoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        LinearLayout view = (LinearLayout) layoutInflater.inflate(viewType, parent, false);
        return new KiemKeKhoAdapter.MyViewHolder(view);
    }
    public void filterList(ArrayList<KiemKe> kiemKeKhoArrayList){
        this.kiemKeKhoArrayList = kiemKeKhoArrayList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        KiemKe kiemKeKho = kiemKeKhoArrayList.get(position);
        if(kiemKeKho==null){
            return;
        }

        holder.tvTen.setText(kiemKeKho.getTen());
        holder.tvNhap.setText(String.valueOf(kiemKeKho.getNhap()));
        holder.tvXuat.setText(String.valueOf(kiemKeKho.getXuat()));
        holder.tvTon.setText(String.valueOf(kiemKeKho.getTon()));
    }
    @Override
    public int getItemViewType(int position) {
        return layoutID;
    }
    @Override
    public int getItemCount() {
        return kiemKeKhoArrayList.size();
    }

    public static class  MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTen,tvNhap,tvXuat,tvTon;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            tvTen = itemView.findViewById(R.id.tvTen);
            tvNhap = itemView.findViewById(R.id.tvNhap);
            tvXuat = itemView.findViewById(R.id.tvXuat);
            tvTon=itemView.findViewById(R.id.tvTon);
        }
    }
}
