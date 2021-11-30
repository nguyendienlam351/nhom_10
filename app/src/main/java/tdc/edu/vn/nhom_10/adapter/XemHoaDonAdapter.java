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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import tdc.edu.vn.nhom_10.R;
import tdc.edu.vn.nhom_10.model.HoaDon;

public class XemHoaDonAdapter extends RecyclerView.Adapter<XemHoaDonAdapter.MyViewHolder>{
    private Activity context;
    private int layoutID;
    private ArrayList<HoaDon> hoaDonArrayList;

    public XemHoaDonAdapter(Activity context, int layoutID, ArrayList<HoaDon> hoaDonArrayList){
        this.context= context;
        this.layoutID=layoutID;
        this.hoaDonArrayList=hoaDonArrayList;
    }
    @NonNull
    @Override
    public XemHoaDonAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        CardView view =(CardView) layoutInflater.inflate(viewType, parent, false);
        return new XemHoaDonAdapter.MyViewHolder(view);
    }
    public void filterList(ArrayList<HoaDon> hoaDonArrayList){
        this.hoaDonArrayList = hoaDonArrayList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull XemHoaDonAdapter.MyViewHolder holder, int position) {
        HoaDon hoaDon = hoaDonArrayList.get(position);
        if(hoaDon==null){
            return;
        }
        holder.tvBan.setText(hoaDon.getDonHang().getTenBan());
        NumberFormat formatter = new DecimalFormat("#,###,###");
        holder.tvTong.setText("Tổng: "+formatter.format(hoaDon.getTong()) + " đ");
        holder.tvNgay.setText(hoaDon.getNgayThang());
        holder.tvHoTen.setText("Họ tên: "+hoaDon.getHoTen());
        holder.tvEmail.setText("Email: "+hoaDon.getEmail());

    }
    @Override
    public int getItemViewType(int position) {
        return layoutID;
    }
    @Override
    public int getItemCount() {
        return hoaDonArrayList.size();
    }

    public static class  MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvBan,tvTong,tvNgay,tvHoTen,tvEmail;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            tvBan = itemView.findViewById(R.id.tvBan);
            tvTong = itemView.findViewById(R.id.tvTong);
            tvNgay = itemView.findViewById(R.id.tvNgayThang);
            tvHoTen=itemView.findViewById(R.id.tvHoTen);
            tvEmail=itemView.findViewById(R.id.tvEmail);
        }

    }
}
