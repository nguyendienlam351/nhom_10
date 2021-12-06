package tdc.edu.vn.nhom_10.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import tdc.edu.vn.nhom_10.R;
import tdc.edu.vn.nhom_10.model.DoanhThuMD;

public class DoanhThuAdapter extends RecyclerView.Adapter<DoanhThuAdapter.MyViewHolder>{
    private Activity context;
    private int layoutID;
    private ArrayList<DoanhThuMD> doanhThuArrayList;

    public DoanhThuAdapter(Activity context, int layoutID, ArrayList<DoanhThuMD> doanhThuArrayList){
        this.context= context;
        this.layoutID=layoutID;
        this.doanhThuArrayList=doanhThuArrayList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        LinearLayout view = (LinearLayout) layoutInflater.inflate(viewType, parent, false);
        return new MyViewHolder(view);
    }
    public void filterList(ArrayList<DoanhThuMD> doanhThuArrayList){
        this.doanhThuArrayList = doanhThuArrayList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DoanhThuMD doanhThu = doanhThuArrayList.get(position);
        if(doanhThu==null){
            return;
        }
        NumberFormat formatter = new DecimalFormat("#,###,###");
        holder.tvNgay.setText(doanhThu.getNgay());
        holder.tvThu.setText(formatter.format(doanhThu.getThu()) + " đ");
        holder.tvChi.setText(formatter.format(doanhThu.getChi()) + " đ");
        holder.tvLoiNhuan.setText(formatter.format(doanhThu.getThu()-doanhThu.getChi()) + " đ");

    }
    @Override
    public int getItemViewType(int position) {
        return layoutID;
    }
    @Override
    public int getItemCount() {
        return doanhThuArrayList.size();
    }

    public static class  MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvNgay,tvThu,tvChi,tvLoiNhuan;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            tvNgay = itemView.findViewById(R.id.tvNgay);
            tvThu = itemView.findViewById(R.id.tvThu);
            tvChi = itemView.findViewById(R.id.tvChi);
            tvLoiNhuan=itemView.findViewById(R.id.tvLoiNhuan);
        }
    }
}
