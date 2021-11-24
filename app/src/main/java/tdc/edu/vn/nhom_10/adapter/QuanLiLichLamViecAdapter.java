package tdc.edu.vn.nhom_10.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tdc.edu.vn.nhom_10.R;
import tdc.edu.vn.nhom_10.model.CaLamViec;
import tdc.edu.vn.nhom_10.model.NhanVien;

public class QuanLiLichLamViecAdapter extends RecyclerView.Adapter<QuanLiLichLamViecAdapter.MyViewHolder>{
    private Activity context;
    private int layoutID;
    private ArrayList<CaLamViec> lichLamViecArrayList;
    private MyItemClickListener delegation;

    public void setDelegation(MyItemClickListener delegation) {
        this.delegation = delegation;
    }

    public QuanLiLichLamViecAdapter(Activity context, int layoutID, ArrayList<CaLamViec> lichLamViecArrayList){
        this.context= context;
        this.layoutID=layoutID;
        this.lichLamViecArrayList=lichLamViecArrayList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        LinearLayout view = (LinearLayout) layoutInflater.inflate(viewType, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CaLamViec caLamViec = lichLamViecArrayList.get(position);
        if(caLamViec==null){
            return;
        }
        holder.tvthuNgay.setText(caLamViec.getThuNgay());
        holder.imvCaA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delegation.getCaA(caLamViec);
            }
        });
        holder.imvcaB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delegation.getCaB(caLamViec);
            }
        });
        holder.imvcaC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delegation.getCaC(caLamViec);
            }
        });
        if(caLamViec.getCaA().size() == 0){
            holder.imvCaA.setImageDrawable(null);
        }
        if(caLamViec.getCaB().size() == 0){
            holder.imvcaB.setImageDrawable(null);
        }
        if(caLamViec.getCaC().size() == 0){
            holder.imvcaC.setImageDrawable(null);
        }
    }
    @Override
    public int getItemViewType(int position) {
        return layoutID;
    }
    @Override
    public int getItemCount() {
        return lichLamViecArrayList.size();
    }

    public static class  MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvthuNgay;
        ImageView imvCaA, imvcaB,imvcaC;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            tvthuNgay = itemView.findViewById(R.id.tvthuNgay);
            imvCaA = itemView.findViewById(R.id.imvcaA);
            imvcaB = itemView.findViewById(R.id.imvcaB);
            imvcaC=itemView.findViewById(R.id.imvcaC);
        }

    }
    public interface MyItemClickListener{
        void getCaA(CaLamViec caLamViec);
        void getCaB(CaLamViec caLamViec);
        void getCaC(CaLamViec caLamViec);
    }
}
