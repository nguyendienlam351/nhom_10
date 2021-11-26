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
import tdc.edu.vn.nhom_10.model.ChiTietDonHang;
import tdc.edu.vn.nhom_10.model.KinhDoanh;

public class TinhHinhKinhDoanhAdapter extends RecyclerView.Adapter<TinhHinhKinhDoanhAdapter.TinhHinhKinhDoanhViewHolder> {
    private Activity context;
    private int layoutID;
    private ArrayList<KinhDoanh> kinhDoanhArrayList;

    public TinhHinhKinhDoanhAdapter(Activity context, int layoutID, ArrayList<KinhDoanh> kinhDoanhArrayList) {
        this.context = context;
        this.layoutID = layoutID;
        this.kinhDoanhArrayList = kinhDoanhArrayList;
    }


    @NonNull
    @Override
    public TinhHinhKinhDoanhAdapter.TinhHinhKinhDoanhViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        LinearLayout view = (LinearLayout) layoutInflater.inflate(viewType, parent, false);
        return new TinhHinhKinhDoanhAdapter.TinhHinhKinhDoanhViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TinhHinhKinhDoanhAdapter.TinhHinhKinhDoanhViewHolder holder, int position) {
        KinhDoanh kinhDoanh = kinhDoanhArrayList.get(position);

        holder.tvTenMon.setText(kinhDoanh.getTenMon());
        holder.tvSoLuong.setText(String.valueOf(kinhDoanh.getSoLuong()));

    }

    @Override
    public int getItemViewType(int position) {
        return layoutID;
    }

    @Override
    public int getItemCount() {
        return kinhDoanhArrayList.size();
    }

    public void filterList(ArrayList<KinhDoanh> kinhDoanhArrayList){
        this.kinhDoanhArrayList = kinhDoanhArrayList;
        notifyDataSetChanged();
    }
    public static class TinhHinhKinhDoanhViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenMon;
        TextView tvSoLuong;

        public TinhHinhKinhDoanhViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenMon = itemView.findViewById(R.id.tvTenMon);
            tvSoLuong = itemView.findViewById(R.id.tvSoLuong);
        }
    }
}
