package tdc.edu.vn.nhom_10.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import tdc.edu.vn.nhom_10.R;
import tdc.edu.vn.nhom_10.model.ChiTietDonHang;

public class DanhSachDonHangAdapter extends RecyclerView.Adapter<DanhSachDonHangAdapter.DanhSachDonHangViewHolder>{
    private Activity context;
    private int layoutID;
    private ArrayList<ChiTietDonHang> chiTietDonHangArrayList;
    private DanhSachDonHangClickListener delegation;
    private StorageReference storage;

    public void setDelegation(DanhSachDonHangClickListener delegation) {
        this.delegation = delegation;
    }

    public DanhSachDonHangAdapter(Activity context, int layoutID, ArrayList<ChiTietDonHang> chiTietDonHangArrayList) {
        this.context = context;
        this.layoutID = layoutID;
        this.chiTietDonHangArrayList = chiTietDonHangArrayList;
        storage = FirebaseStorage.getInstance().getReference("Mon");
    }


    @NonNull
    @Override
    public DanhSachDonHangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        CardView view = (CardView) layoutInflater.inflate(viewType, parent, false);
        return new DanhSachDonHangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DanhSachDonHangViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ChiTietDonHang chiTietDonHang = chiTietDonHangArrayList.get(position);

        if(!chiTietDonHang.getTrangThai().equals("ch???")){
            holder.btnHuy.setVisibility(View.INVISIBLE);
            holder.btnXong.setVisibility(View.INVISIBLE);
        }
        else {
            holder.btnHuy.setVisibility(View.VISIBLE);
            holder.btnXong.setVisibility(View.VISIBLE);
        }

        getAnhMon(chiTietDonHang.getAnh(),holder.imgMon);

        holder.tvTenMon.setText(chiTietDonHang.getTenMon());

        NumberFormat formatter = new DecimalFormat("#,###,###");
        holder.tvGia.setText(formatter.format(chiTietDonHang.getGia()) + " ??");

        holder.tvSoLuong.setText("S??? l?????ng: " + chiTietDonHang.getSoLuong());

        holder.tvTrangThai.setText(chiTietDonHang.getTrangThai()+"");

        holder.btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(delegation != null){
                    delegation.huyClick(chiTietDonHang,position);
                }
                else {
                    Toast.makeText(context, "you must set delegation before", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.btnXong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(delegation != null){
                    delegation.xongClick(chiTietDonHang,position);
                }
                else {
                    Toast.makeText(context, "you must set delegation before", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getAnhMon(String anh, ImageView imgMon) {
        int dot = anh.lastIndexOf('.');
        String base = (dot == -1) ? anh : anh.substring(0, dot);
        String extension = (dot == -1) ? "" : anh.substring(dot + 1);
        try {
            final File file = File.createTempFile(base, extension);
            storage.child(anh).getFile(file)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                            imgMon.setImageBitmap(bitmap);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return layoutID;
    }

    @Override
    public int getItemCount() {
        return chiTietDonHangArrayList.size();
    }

    public static class DanhSachDonHangViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgMon;
        public TextView tvTenMon;
        public TextView tvGia;
        public TextView tvSoLuong;
        public ImageButton btnHuy;
        public ImageButton btnXong;
        public TextView tvTrangThai;

        public DanhSachDonHangViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMon = itemView.findViewById(R.id.imgMon);
            tvTenMon = itemView.findViewById(R.id.tvTenMon);
            tvGia = itemView.findViewById(R.id.tvGia);
            tvSoLuong = itemView.findViewById(R.id.tvSoLuong);
            btnHuy = itemView.findViewById(R.id.btnHuy);
            btnXong = itemView.findViewById(R.id.btnXong);
            tvTrangThai = itemView.findViewById(R.id.tvTrangThai);
        }
    }
    public interface DanhSachDonHangClickListener{
        public void huyClick(ChiTietDonHang chiTietDonHang, int position);
        public void xongClick(ChiTietDonHang chiTietDonHang, int position);
    }
}
