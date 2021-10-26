package tdc.edu.vn.nhom_10.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tdc.edu.vn.nhom_10.R;
import tdc.edu.vn.nhom_10.model.LoaiMon;

public class MyRecyclerViewAdapterLoaiMon extends RecyclerView.Adapter<MyRecyclerViewAdapterLoaiMon.MyViewHolder>{
    private Activity context;
    private int layoutID;
    private ArrayList<LoaiMon> loaiMonArrayList;
    private MyRecyclerViewAdapterLoaiMon.MyItemClickListener delegation;

    public void setDelegation(MyRecyclerViewAdapterLoaiMon.MyItemClickListener delegation) {
        this.delegation = delegation;
    }

    public MyRecyclerViewAdapterLoaiMon(Activity context, int layoutID, ArrayList<LoaiMon> loaiMonArrayList){
        this.context= context;
        this.layoutID=layoutID;
        this.loaiMonArrayList=loaiMonArrayList;
    }
    @NonNull
    @Override
    public MyRecyclerViewAdapterLoaiMon.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        CardView view =(CardView) layoutInflater.inflate(viewType, parent, false);
        return new MyRecyclerViewAdapterLoaiMon.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewAdapterLoaiMon.MyViewHolder holder, int position) {
        LoaiMon loaiMon = loaiMonArrayList.get(position);
        if(loaiMon==null){
            return;
        }
        holder.tvLoaiMon.setText(loaiMon.getTenLoaiMon());

        holder.imbDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delegation.getDeleteLoaiMon(loaiMon);
            }
        });
        holder.imbUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delegation.getUpDateLoaiMon(loaiMon);
            }
        });
    }
    @Override
    public int getItemViewType(int position) {
        return layoutID;
    }
    @Override
    public int getItemCount() {
        return loaiMonArrayList.size();
    }

    public static class  MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvLoaiMon;
        ImageButton imbUpdate;
        ImageButton imbDelete;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            tvLoaiMon = itemView.findViewById(R.id.tvLoaiMon);
            imbUpdate=itemView.findViewById(R.id.imbUpdate);
            imbDelete=itemView.findViewById(R.id.imbXoa);
        }
    }
    public interface MyItemClickListener{
        void getDeleteLoaiMon(LoaiMon loaiMon);
        void getUpDateLoaiMon(LoaiMon loaiMon);
    }
}
