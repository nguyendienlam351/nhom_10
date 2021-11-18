package tdc.edu.vn.nhom_10;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import tdc.edu.vn.nhom_10.CustomView.CustomActionBar;
import tdc.edu.vn.nhom_10.adapter.MyRecyclerViewAdapterLoaiMon;
import tdc.edu.vn.nhom_10.model.Ban;
import tdc.edu.vn.nhom_10.model.LoaiMon;

public class QuanLyLoaiMon extends AppCompatActivity {
    EditText edtNhapTenLoaiMon;
    CustomActionBar actionBar;
    Button btnAdd,btnCancel,btnThayDoi;
    LinearLayout layout;
    RecyclerView lvLoaiMon;
    ArrayList<LoaiMon> data = new ArrayList<LoaiMon>();
    MyRecyclerViewAdapterLoaiMon myRecyclerViewAdapter;
    DatabaseReference database;
    LoaiMon selected = new LoaiMon();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_loai_mon);
        setControl();
        setEvent();
    }
    //Lấy danh sách loại món trên RealTimeDatabase
    private void getListBanFromRealTimeDatabase(){
        database= FirebaseDatabase.getInstance().getReference("LoaiMon");
        Query query = database.orderByChild("tenLoaiMon");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                LoaiMon loaiMon = snapshot.getValue(LoaiMon.class);
                if(loaiMon != null){
                    data.add(loaiMon);
                    myRecyclerViewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                LoaiMon loaiMon = snapshot.getValue(LoaiMon.class);
                if(loaiMon == null || data==null||data.isEmpty()){
                    return;
                }
                for (int i = 0;i<data.size();i++){
                    if(loaiMon.getMaLoaiMon()==data.get(i).getMaLoaiMon()){
                        data.set(i,loaiMon);
                    }
                }
                myRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                LoaiMon loaiMon = snapshot.getValue(LoaiMon.class);
                if(loaiMon == null || data==null||data.isEmpty()){
                    return;
                }
                for (int i = 0;i<data.size();i++){
                    if(loaiMon.getMaLoaiMon()==data.get(i).getMaLoaiMon()){
                        data.remove(data.get(i));
                        break;
                    }
                }
                myRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(QuanLyLoaiMon.this, "Faild", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setEvent() {
        actionBar.setDelegation(new CustomActionBar.ActionBarDelegation() {
            @Override
            public void backOnClick() {
                finish();
            }
        });

        actionBar.setActionBarName("Quản lý loại món");

        myRecyclerViewAdapter = new MyRecyclerViewAdapterLoaiMon(this,R.layout.layout_item_loai_mon,data);
        myRecyclerViewAdapter.setDelegation(new MyRecyclerViewAdapterLoaiMon.MyItemClickListener() {
            @Override
            public void getDeleteLoaiMon(LoaiMon loaiMon) {
                openDiaLogDeleteItem(loaiMon);
            }
            @Override
            public void getUpDateLoaiMon(LoaiMon loaiMon) {
                int dot = loaiMon.getTenLoaiMon().lastIndexOf(' ');
                String catChuoi = (dot == -1) ? "" : loaiMon.getTenLoaiMon().substring(dot + 1);
                selected.setMaLoaiMon(loaiMon.getMaLoaiMon());
                selected.setTenLoaiMon(catChuoi);
                edtNhapTenLoaiMon.setText(catChuoi);
                if(layout.getVisibility() == View.GONE){
                    btnAdd.setVisibility(View.GONE);
                    layout.setVisibility(View.VISIBLE);
                }
            }
        });
        btnThayDoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDiaLogUpdateItem();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtNhapTenLoaiMon.setText("");
                btnAdd.setVisibility(View.VISIBLE);
                layout.setVisibility(View.GONE);

            }
        });
        lvLoaiMon.setAdapter(myRecyclerViewAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        lvLoaiMon.setLayoutManager(layoutManager);
        //Hàm thêm loại món mới
        edtNhapTenLoaiMon.addTextChangedListener(watcher);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name =edtNhapTenLoaiMon.getText().toString().trim();
                String maLoaiMon=database.push().getKey();
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                LoaiMon loaiMon = new LoaiMon("Loại "+name);
                loaiMon.setMaLoaiMon(maLoaiMon);

                database.orderByChild("tenLoaiMon").equalTo(loaiMon.getTenLoaiMon()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            edtNhapTenLoaiMon.setError("Tên loại món đã tồn tại");
                        } else {
                            mDatabase.child("LoaiMon").child(maLoaiMon).setValue(loaiMon).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(QuanLyLoaiMon.this, "Thành công", Toast.LENGTH_SHORT).show();
                                    edtNhapTenLoaiMon.setText("");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(QuanLyLoaiMon.this, e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        getListBanFromRealTimeDatabase();

    }
    //Hàm vô hiệu hoá nút thêm khi không nhập gì
    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String name = edtNhapTenLoaiMon.getText().toString().trim();
            btnAdd.setEnabled(!name.isEmpty());
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    };
    //Hàm Sửa
    private void openDiaLogUpdateItem(){
        new AlertDialog.Builder(this)
                .setTitle("Thay đổi")
                .setMessage("Bạn có muốn thay đổi?")
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        database=FirebaseDatabase.getInstance().getReference("LoaiMon");
                        String newTenLoaiMon = edtNhapTenLoaiMon.getText().toString().trim();
                        selected.setTenLoaiMon("Loại "+newTenLoaiMon);
                        database.orderByChild("tenLoaiMon").equalTo(selected.getTenLoaiMon()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    edtNhapTenLoaiMon.setError("Tên loại món đã tồn tại");
                                } else {
                                    database.child(String.valueOf(selected.getMaLoaiMon())).updateChildren(selected.toMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(QuanLyLoaiMon.this, "Thành công", Toast.LENGTH_SHORT).show();
                                            edtNhapTenLoaiMon.setText("");
                                            selected = new LoaiMon();
                                            btnAdd.setVisibility(View.VISIBLE);
                                            layout.setVisibility(View.GONE);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(QuanLyLoaiMon.this, e.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                })
                .setNegativeButton("Từ chối",null)
                .setCancelable(false)
                .show();
    }
    //Hàm xoá
    private void openDiaLogDeleteItem(LoaiMon loaiMon){
        new AlertDialog.Builder(this)
                .setTitle("Xoá")
                .setMessage("Bạn có muốn xoá?")
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        database=FirebaseDatabase.getInstance().getReference("LoaiMon");
                        database.child(String.valueOf(loaiMon.getMaLoaiMon())).removeValue();
                    }

                })
                .setNegativeButton("Từ chối",null)
                .setCancelable(false)
                .show();
    }
    private void setControl() {
        lvLoaiMon = findViewById(R.id.lvLoaiMon);
        edtNhapTenLoaiMon=findViewById(R.id.edtNhapTenLoaiMon);
        btnAdd=findViewById(R.id.btnAdd);
        btnCancel=findViewById(R.id.btnCancel);
        btnThayDoi=findViewById(R.id.btnThayDoi);
        layout=findViewById(R.id.Layout);
        btnAdd.setEnabled(false);
        actionBar = findViewById(R.id.actionBar);
    }
}
