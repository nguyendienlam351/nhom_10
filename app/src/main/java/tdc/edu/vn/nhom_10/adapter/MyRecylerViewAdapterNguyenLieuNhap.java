package tdc.edu.vn.nhom_10.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

import tdc.edu.vn.nhom_10.ChiTietNhanVien;
import tdc.edu.vn.nhom_10.ChiTietNhapKho;
import tdc.edu.vn.nhom_10.R;
import tdc.edu.vn.nhom_10.ThemNhapKho;
import tdc.edu.vn.nhom_10.model.NguyenLieu;

public class MyRecylerViewAdapterNguyenLieuNhap extends RecyclerView.Adapter<MyRecylerViewAdapterNguyenLieuNhap.MyViewHolder> {
    Activity conText;
    int layoutID;
    ArrayList<NguyenLieu> data;

    private MyItemClickListener delegation;

    public void setDelegation(MyItemClickListener delegation) {
        this.delegation = delegation;
    }

    public MyRecylerViewAdapterNguyenLieuNhap(Activity conText, int layoutID, ArrayList<NguyenLieu> data) {
        this.data = data;
        this.layoutID = layoutID;
        this.conText = conText;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = conText.getLayoutInflater();
        LinearLayout view = (LinearLayout) layoutInflater.inflate(viewType, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        NguyenLieu nguyenLieu = data.get(position);
        holder.tvTen.setText(nguyenLieu.getTenNL());
        NumberFormat formatter = new DecimalFormat("#,###,###");
        holder.tvGia.setText(formatter.format(nguyenLieu.getGia()) + " đ");
        holder.tvDonVi.setText(nguyenLieu.getDonVi());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Chuyển màn hình
                Intent intent = new Intent(conText.getApplicationContext(), ThemNhapKho.class);
                Bundle bundle = new Bundle();

                bundle.putString("MaNL", nguyenLieu.getMaNL());
                intent.putExtras(bundle);
                conText.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {

        return layoutID;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    //Search
    public void filterList(ArrayList<NguyenLieu> nguyenLieuArrayList) {
        this.data = nguyenLieuArrayList;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTen, tvGia, tvDonVi;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTen = itemView.findViewById(R.id.tvTen);
            tvGia = itemView.findViewById(R.id.tvGia);
            tvDonVi = itemView.findViewById(R.id.tvDonVi);
        }
    }

    public interface MyItemClickListener {
        void getThongTinDMK(NguyenLieu nguyenLieu);
    }

}