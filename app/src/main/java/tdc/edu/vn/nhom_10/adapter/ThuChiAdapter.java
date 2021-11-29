package tdc.edu.vn.nhom_10.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import tdc.edu.vn.nhom_10.R;
import tdc.edu.vn.nhom_10.model.ChiThu;

public class ThuChiAdapter extends RecyclerView.Adapter<ThuChiAdapter.ThuChiViewHolder> {
    private Fragment conText;
    private int layoutID;
    private ArrayList<ChiThu> data;
    private ThuChiItemClickListener delegation;

    public void setDelegation(ThuChiItemClickListener delegation) {
        this.delegation = delegation;
    }

    public ThuChiAdapter(Fragment conText, int layoutID, ArrayList<ChiThu> data) {
        this.conText = conText;
        this.layoutID = layoutID;
        this.data = data;
    }

    @NonNull
    @Override
    public ThuChiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = conText.getLayoutInflater();
        CardView view = (CardView) layoutInflater.inflate(viewType, parent, false);
        return new ThuChiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThuChiViewHolder holder, int position) {
        ChiThu chiThu = data.get(position);

        holder.tvLoai.setText(chiThu.getLoaiThuChi());
        holder.tvMoTa.setText(chiThu.getMoTa());
        holder.tvSoTien.setText(String.valueOf(chiThu.getSoTien()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(delegation != null){
                    delegation.itemClick(chiThu);
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

    public static class ThuChiViewHolder extends RecyclerView.ViewHolder{

        private TextView tvLoai;
        private TextView tvMoTa;
        private TextView tvSoTien;

        public ThuChiViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLoai = itemView.findViewById(R.id.tvLoai);
            tvMoTa = itemView.findViewById(R.id.tvMoTa);
            tvSoTien = itemView.findViewById(R.id.tvSoTien);
        }
    }
    public interface ThuChiItemClickListener{
        public void itemClick(ChiThu thuchi);
    }
    public void filterList(ArrayList<ChiThu> chiThuArrayList) {
        this.data = chiThuArrayList;
        notifyDataSetChanged();
    }
}
