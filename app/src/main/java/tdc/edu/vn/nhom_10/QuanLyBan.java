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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

import tdc.edu.vn.nhom_10.CustomView.CustomActionBar;
import tdc.edu.vn.nhom_10.adapter.MyRecyclerViewAdapterBan;
import tdc.edu.vn.nhom_10.model.Ban;

public class QuanLyBan extends AppCompatActivity {
    EditText edtNhapTenBan;
    CustomActionBar actionBar;
    Button btnAdd,btnCancel,btnThayDoi;
    LinearLayout layout;
    RecyclerView lvBan;
    ArrayList<Ban> data = new ArrayList<Ban>();
    MyRecyclerViewAdapterBan myRecyclerViewAdapter;
    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_ban);
        setControl();
        setEvent();
    }
    //Lấy danh sách bàn trên RealTimeDatabase
    private void getListBanFromRealTimeDatabase(){
        database= FirebaseDatabase.getInstance().getReference("Ban");
        Query query = database.orderByChild("tenBan");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Ban ban = snapshot.getValue(Ban.class);
                if(ban != null){
                    data.add(ban);
                    myRecyclerViewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Ban ban = snapshot.getValue(Ban.class);
                if(ban == null || data==null||data.isEmpty()){
                    return;
                }
                for (int i = 0;i<data.size();i++){
                    if(ban.getMaBan()==data.get(i).getMaBan()){
                        data.set(i,ban);
                    }
                }
                myRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Ban ban = snapshot.getValue(Ban.class);
                if(ban == null || data==null||data.isEmpty()){
                    return;
                }
                for (int i = 0;i<data.size();i++){
                    if(ban.getMaBan()==data.get(i).getMaBan()){
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
                Toast.makeText(QuanLyBan.this, "Faild", Toast.LENGTH_SHORT).show();
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

        actionBar.setActionBarName("Quản lý bàn");

        myRecyclerViewAdapter = new MyRecyclerViewAdapterBan(this,R.layout.layout_item_ban_1,data);
        myRecyclerViewAdapter.setDelegation(new MyRecyclerViewAdapterBan.MyItemClickListener() {
            @Override
            public void getDeleteBan(Ban ban) {
                openDiaLogDeleteItem(ban);
            }
            @Override
            public void getUpDate(Ban ban) {
                edtNhapTenBan.setText(ban.getTenBan());
                if(layout.getVisibility() == View.GONE){
                    btnAdd.setVisibility(View.GONE);
                    layout.setVisibility(View.VISIBLE);
                }
                btnThayDoi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openDiaLogUpdateItem(ban);
                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        edtNhapTenBan.setText("");
                        btnAdd.setVisibility(View.VISIBLE);
                        layout.setVisibility(View.GONE);

                    }
                });
            }
        });
        lvBan.setAdapter(myRecyclerViewAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        lvBan.setLayoutManager(layoutManager);
        //Hàm thêm bàn mới
        edtNhapTenBan.addTextChangedListener(watcher);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name =edtNhapTenBan.getText().toString().trim();
                String maBan=database.push().getKey();
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                Ban ban = new Ban("Bàn số "+name);
                ban.setMaBan(maBan);
                mDatabase.child("Ban").child(maBan).setValue(ban);
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
            String name = edtNhapTenBan.getText().toString().trim();
            btnAdd.setEnabled(!name.isEmpty());
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    };
    //Hàm Sửa
    private void openDiaLogUpdateItem(Ban ban){
        new AlertDialog.Builder(this)
                .setTitle("Thay đổi")
                .setMessage("Bạn có muốn thay đổi?")
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        database=FirebaseDatabase.getInstance().getReference("Ban");
                        String newTenBan = edtNhapTenBan.getText().toString().trim();
                        ban.setTenBan(newTenBan);
                        database.child(String.valueOf(ban.getMaBan())).updateChildren(ban.toMap());
                    }

                })
                .setNegativeButton("Từ chối",null)
                .setCancelable(false)
                .show();
    }
    //Hàm xoá
    private void openDiaLogDeleteItem(Ban ban){
        new AlertDialog.Builder(this)
                .setTitle("Xoá")
                .setMessage("Bạn có muốn xoá?")
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        database=FirebaseDatabase.getInstance().getReference("Ban");
                        database.child(String.valueOf(ban.getMaBan())).removeValue();
                    }

                })
                .setNegativeButton("Từ chối",null)
                .setCancelable(false)
                .show();
    }
    private void setControl() {
        lvBan = findViewById(R.id.lvBan);
        edtNhapTenBan=findViewById(R.id.edtNhapTenBan);
        btnAdd=findViewById(R.id.btnAdd);
        btnCancel=findViewById(R.id.btnCancel);
        btnThayDoi=findViewById(R.id.btnThayDoi);
        layout=findViewById(R.id.Layout);
        btnAdd.setEnabled(false);
        actionBar = findViewById(R.id.actionBar);
    }
}
