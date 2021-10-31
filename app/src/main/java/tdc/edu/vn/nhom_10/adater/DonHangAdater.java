package tdc.edu.vn.nhom_10.adater;

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

public class DonHangAdater extends RecyclerView.Adapter<DonHangAdater.DonHangViewHolder> {
    private Activity context;
    private int layoutID;
    private ArrayList<DonHang> donHangArrayList;

    private MyItemClickListener delegation;

    public void setDelegation(MyItemClickListener delegation) {
        this.delegation = delegation;
    }

    public DonHangAdater(Activity context, int layoutID, ArrayList<DonHang> donHangArrayList) {
        this.context = context;
        this.layoutID = layoutID;
        this.donHangArrayList = donHangArrayList;
    }

    @NonNull
    @Override
    public DonHangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        CardView view = (CardView) layoutInflater.inflate(viewType, parent, false);
        return new DonHangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonHangViewHolder holder, int position) {
        DonHang donHang = donHangArrayList.get(position);
        holder.tvBan.setText(donHang.getTenBan());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(delegation != null){
                    delegation.getBan(donHang);
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
        return donHangArrayList.size();
    }

    public void filterList(ArrayList<DonHang> donHangArrayList){
        this.donHangArrayList = donHangArrayList;
        notifyDataSetChanged();
    }

    public static class DonHangViewHolder extends RecyclerView.ViewHolder {
        public TextView tvBan;

        public DonHangViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBan = itemView.findViewById(R.id.tvBan);
        }

    }

    public interface MyItemClickListener{
        public void getBan(DonHang donHang);
    }
}
