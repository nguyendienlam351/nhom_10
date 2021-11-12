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
import java.util.List;

import tdc.edu.vn.nhom_10.DanhSachNguyenLieu;
import tdc.edu.vn.nhom_10.R;
import tdc.edu.vn.nhom_10.model.ChiTietDonHang;
import tdc.edu.vn.nhom_10.model.NguyenLieu;
import tdc.edu.vn.nhom_10.model.NhanVien;

public class NguyenLieuAdapter extends RecyclerView.Adapter<NguyenLieuAdapter.NguyenLieuViewHolder> {
    private Activity conText;
    private int layoutID;
    private ArrayList<NguyenLieu> data;
    private NguyenLieuItemClickListener delegation;

    public void setDelegation(NguyenLieuItemClickListener delegation) {
        this.delegation = delegation;
    }

    public NguyenLieuAdapter(Activity conText, int layoutID, ArrayList<NguyenLieu> data) {
        this.conText = conText;
        this.layoutID = layoutID;
        this.data = data;
    }

    @NonNull
    @Override
    public NguyenLieuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = conText.getLayoutInflater();
        LinearLayout view = (LinearLayout) layoutInflater.inflate(viewType, parent, false);
        return new NguyenLieuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NguyenLieuViewHolder holder, int position) {
        NguyenLieu nguyenLieu = data.get(position);

        holder.tvTenNL.setText(nguyenLieu.getTenNL());
        holder.tvGia.setText(String.valueOf(nguyenLieu.getGia()));
        holder.tvDonVi.setText(nguyenLieu.getDonVi());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(delegation != null){
                    delegation.itemClick(nguyenLieu);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(data!=null){
            return data.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return layoutID;
    }

    public static class NguyenLieuViewHolder extends RecyclerView.ViewHolder{

        private TextView tvTenNL;
        private TextView tvDonVi;
        private TextView tvGia;

        public NguyenLieuViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenNL = itemView.findViewById(R.id.tvTenNL);
            tvDonVi = itemView.findViewById(R.id.tvDonVi);
            tvGia = itemView.findViewById(R.id.tvGia);
        }
    }
    public interface NguyenLieuItemClickListener{
        public void itemClick(NguyenLieu nguyenLieu);
        public void iconClick(NguyenLieu nguyenLieu);
    }
}
