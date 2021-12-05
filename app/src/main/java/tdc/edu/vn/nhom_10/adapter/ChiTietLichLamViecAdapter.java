package tdc.edu.vn.nhom_10.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import tdc.edu.vn.nhom_10.R;
import tdc.edu.vn.nhom_10.model.CaLamViec;
import tdc.edu.vn.nhom_10.model.NhanVien;

public class ChiTietLichLamViecAdapter extends RecyclerView.Adapter<ChiTietLichLamViecAdapter.MyViewHolder>{
    private Activity context;
    private int layoutID;
    private ArrayList<NhanVien> lichLamViecArrayList;
    private MyItemClickListener delegation;
    private StorageReference storage;
    boolean kiemTra = true;

    public void setKiemTra(boolean kiemTra) {
        this.kiemTra = kiemTra;
    }

    public void setDelegation(MyItemClickListener delegation) {
        this.delegation = delegation;
    }

    public ChiTietLichLamViecAdapter(Activity context, int layoutID, ArrayList<NhanVien> lichLamViecArrayList){
        this.context= context;
        this.layoutID=layoutID;
        this.lichLamViecArrayList=lichLamViecArrayList;
        storage = FirebaseStorage.getInstance().getReference("NhanVien");
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        CardView view = (CardView) layoutInflater.inflate(viewType, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        NhanVien nhanVien = lichLamViecArrayList.get(position);
        if(nhanVien==null){
            return;
        }
        getAnhNhanVien(nhanVien.getAnh(),holder.imgAnhNV);
        holder.tvTenNV.setText(nhanVien.getHoTen());
        holder.tvSDT.setText(nhanVien.getSoDienThoai());
        holder.tvChucVu.setText(nhanVien.getChucVu());
        if(kiemTra){
            holder.imbXoa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delegation.getXoaNV(nhanVien);
                }
            });
        }
        else {
            holder.imbXoa.setVisibility(View.INVISIBLE);
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

    private void getAnhNhanVien(String anh, ImageView imgAnhNV) {
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
                            imgAnhNV.setImageBitmap(bitmap);
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
    public static class  MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenNV,tvSDT,tvChucVu;
        ImageView imgAnhNV;
        ImageButton imbXoa;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            tvTenNV = itemView.findViewById(R.id.tvTenNV);
            tvSDT = itemView.findViewById(R.id.tvSDT);
            tvChucVu = itemView.findViewById(R.id.tvChucVu);
            imgAnhNV = itemView.findViewById(R.id.imgAnhNV);
            imbXoa = itemView.findViewById(R.id.imbXoa);
        }

    }
    public interface MyItemClickListener{
        void getXoaNV(NhanVien nhanVien);
    }
}
