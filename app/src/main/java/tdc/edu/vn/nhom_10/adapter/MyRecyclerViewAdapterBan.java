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
import tdc.edu.vn.nhom_10.model.Ban;

public class MyRecyclerViewAdapterBan extends RecyclerView.Adapter<MyRecyclerViewAdapterBan.MyViewHolder>{
    private Activity context;
    private int layoutID;
    private ArrayList<Ban> banArrayList;
    private MyItemClickListener delegation;

    public void setDelegation(MyItemClickListener delegation) {
        this.delegation = delegation;
    }

    public MyRecyclerViewAdapterBan(Activity context, int layoutID, ArrayList<Ban> banArrayList){
        this.context= context;
        this.layoutID=layoutID;
        this.banArrayList=banArrayList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        CardView view =(CardView) layoutInflater.inflate(viewType, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Ban ban = banArrayList.get(position);
        if(ban==null){
            return;
        }
        holder.tvBan.setText(ban.getTenBan());

        holder.imbDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delegation.getDeleteBan(ban);
            }
        });
        holder.imbUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delegation.getUpDate(ban);
            }
        });
    }
    @Override
    public int getItemViewType(int position) {
        return layoutID;
    }
    @Override
    public int getItemCount() {
        return banArrayList.size();
    }

    public static class  MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvBan;
        ImageButton imbUpdate;
        ImageButton imbDelete;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            tvBan = itemView.findViewById(R.id.tvBan);
            imbUpdate=itemView.findViewById(R.id.imbUpdate);
            imbDelete=itemView.findViewById(R.id.imbXoa);
        }

    }
    public interface MyItemClickListener{
        void getDeleteBan(Ban ban);
        void getUpDate(Ban ban);
    }
}
