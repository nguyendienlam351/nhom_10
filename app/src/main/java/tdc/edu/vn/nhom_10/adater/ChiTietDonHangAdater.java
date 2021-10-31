package tdc.edu.vn.nhom_10.adater;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
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

public class ChiTietDonHangAdater extends RecyclerView.Adapter<ChiTietDonHangAdater.ChiTietDonHangViewHolder>{
    private Activity context;
    private int layoutID;
    private ArrayList<ChiTietDonHang> chiTietDonHangArrayList;
    private ChiTietDonHangClickListener delegation;
    private StorageReference storage;

    public void setDelegation(ChiTietDonHangClickListener delegation) {
        this.delegation = delegation;
    }

    public ChiTietDonHangAdater(Activity context, int layoutID, ArrayList<ChiTietDonHang> chiTietDonHangArrayList) {
        this.context = context;
        this.layoutID = layoutID;
        this.chiTietDonHangArrayList = chiTietDonHangArrayList;
        storage = FirebaseStorage.getInstance().getReference("Mon");
    }


    @NonNull
    @Override
    public ChiTietDonHangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        CardView view = (CardView) layoutInflater.inflate(viewType, parent, false);
        return new ChiTietDonHangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChiTietDonHangViewHolder holder, int position) {
        ChiTietDonHang chiTietDonHang = chiTietDonHangArrayList.get(position);

        if(!chiTietDonHang.getTrangThai().equals("")){
            holder.btnXoaMon.setVisibility(View.INVISIBLE);
        }

        getAnhMon(chiTietDonHang.getAnh(),holder.imgMon);

        holder.tvTenMon.setText(chiTietDonHang.getTenMon());

        NumberFormat formatter = new DecimalFormat("#,###,###");
        holder.tvGia.setText(formatter.format(chiTietDonHang.getGia()) + " đ");

        holder.tvSoLuong.setText(String.valueOf(chiTietDonHang.getSoLuong()));

        holder.tvTrangThai.setText(chiTietDonHang.getTrangThai()+"");

        holder.btnTang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(delegation != null){
                    delegation.tangSoLuong(chiTietDonHang);
                }
                else {
                    Toast.makeText(context, "you must set delegation before", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.btnGiam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(delegation != null){
                    delegation.giamSoLuong(chiTietDonHang);
                }
                else {
                    Toast.makeText(context, "you must set delegation before", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.btnXoaMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(delegation != null){
                    delegation.iconClick(chiTietDonHang);
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

    public static class ChiTietDonHangViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgMon;
        public TextView tvTenMon;
        public TextView tvGia;
        public ImageButton btnTang;
        public ImageButton btnGiam;
        public TextView tvSoLuong;
        public ImageButton btnXoaMon;
        public TextView tvTrangThai;

        public ChiTietDonHangViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMon = itemView.findViewById(R.id.imgMon);
            tvTenMon = itemView.findViewById(R.id.tvTenMon);
            tvGia = itemView.findViewById(R.id.tvGia);
            btnTang = itemView.findViewById(R.id.btnTang);
            btnGiam = itemView.findViewById(R.id.btnGiam);
            tvSoLuong = itemView.findViewById(R.id.tvSoLuong);
            btnXoaMon = itemView.findViewById(R.id.btnXoaMon);
            tvTrangThai = itemView.findViewById(R.id.tvTrangThai);
        }
    }
    public interface ChiTietDonHangClickListener{
        public void iconClick(ChiTietDonHang chiTietDonHang);
        public void tangSoLuong(ChiTietDonHang chiTietDonHang);
        public void giamSoLuong(ChiTietDonHang chiTietDonHang);
    }
}
