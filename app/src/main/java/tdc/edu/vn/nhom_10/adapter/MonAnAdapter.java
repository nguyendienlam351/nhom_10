package tdc.edu.vn.nhom_10.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import tdc.edu.vn.nhom_10.ChiTietNhanVien;
import tdc.edu.vn.nhom_10.ChiTietQLMon;
import tdc.edu.vn.nhom_10.QuanLyThemMon;
import tdc.edu.vn.nhom_10.R;
import tdc.edu.vn.nhom_10.model.MonAn;
import tdc.edu.vn.nhom_10.model.NhanVien;

public class MonAnAdapter extends RecyclerView.Adapter<MonAnAdapter.MonAnViewHolder> {
    private Activity context;
    private int layoutID;
    private ArrayList<MonAn> monAnArrayList;

    private StorageReference storage;

    public MonAnAdapter(Activity context, int layoutID, ArrayList<MonAn> monAnArrayList) {
        this.context = context;
        this.layoutID = layoutID;
        this.monAnArrayList = monAnArrayList;
        storage = FirebaseStorage.getInstance().getReference("Mon");
    }

    @NonNull
    @Override
    public MonAnViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = context.getLayoutInflater();
        CardView view = (CardView) layoutInflater.inflate(viewType, parent, false);
        return new MonAnViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MonAnViewHolder holder, int position) {
        MonAn monAn = monAnArrayList.get(position);

        getAnhMon(monAn.getAnh(), holder.imgMon);

        holder.tvTenMon.setText(monAn.getTenMon());
        NumberFormat formatter = new DecimalFormat("#,###,###");
        holder.tvGia.setText(formatter.format(monAn.getGia()) + " đ");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChiTietQLMon.class);
                Bundle bundle = new Bundle();
                bundle.putString("maMon", monAn.getMaMon());
                intent.putExtras(bundle);

                context.startActivity(intent);

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
    public int getItemCount() {
        return monAnArrayList.size();

    }

    @Override
    public int getItemViewType(int position) {
        return layoutID;
    }


    public class MonAnViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgMon;
        public TextView tvTenMon, tvGia;

        public MonAnViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMon = itemView.findViewById(R.id.imgMon);
            tvTenMon = itemView.findViewById(R.id.tvTenMon);
            tvGia = itemView.findViewById(R.id.tvGia);
        }

    }
}







