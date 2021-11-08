package tdc.edu.vn.nhom_10.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tdc.edu.vn.nhom_10.R;
import tdc.edu.vn.nhom_10.model.DonHang;
import tdc.edu.vn.nhom_10.model.MaGiamGia;

public class MaGiamGiaAdapter extends RecyclerView.Adapter<MaGiamGiaAdapter.MaGiamGiaViewHolder> {
    private Activity context;
    private int layoutID;
    private ArrayList<MaGiamGia> maGiamGiaArrayList;

    private MyItemClickListener delegation;

    public void setDelegation(MyItemClickListener delegation) {
        this.delegation = delegation;
    }

    public MaGiamGiaAdapter(Activity context, int layoutID, ArrayList<MaGiamGia> maGiamGiaArrayList) {
        this.context = context;
        this.layoutID = layoutID;
        this.maGiamGiaArrayList = maGiamGiaArrayList;
    }

    @NonNull
    @Override
    public MaGiamGiaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        CardView view = (CardView) layoutInflater.inflate(viewType, parent, false);
        return new MaGiamGiaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MaGiamGiaViewHolder holder, int position) {
        MaGiamGia maGiamGia = maGiamGiaArrayList.get(position);
        holder.tvMaGiamGia.setText(maGiamGia.getTenMaGiamGia());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(delegation != null){
                    delegation.getMaGiamGia(maGiamGia);
                }
                else {
                    Toast.makeText(context, "you must set delegation before", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return layoutID;
    }

    @Override
    public int getItemCount() {
        return maGiamGiaArrayList.size();
    }

    public void filterList(ArrayList<MaGiamGia> maGiamGiaArrayList){
        this.maGiamGiaArrayList = maGiamGiaArrayList;
        notifyDataSetChanged();
    }

    public static class MaGiamGiaViewHolder extends RecyclerView.ViewHolder {
        public TextView tvMaGiamGia;

        public MaGiamGiaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaGiamGia = itemView.findViewById(R.id.tvMaGiamGia);
        }

    }

    public interface MyItemClickListener{
        public void getMaGiamGia(MaGiamGia maGiamGia);
    }
}
