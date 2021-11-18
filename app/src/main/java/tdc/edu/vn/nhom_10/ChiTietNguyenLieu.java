package tdc.edu.vn.nhom_10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import tdc.edu.vn.nhom_10.CustomView.CustomActionBar;
import tdc.edu.vn.nhom_10.model.NguyenLieu;

public class ChiTietNguyenLieu extends AppCompatActivity {

    EditText edTenNL,edGia,edMoTa;
    Spinner spDonVi;
    Button btnXoa, btnThayDoi;
    NguyenLieu nguyenLieu = new NguyenLieu();
    DatabaseReference myref = FirebaseDatabase.getInstance().getReference("NguyenLieu");
    CustomActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_nguyen_lieu);
        setControl();
        setEvent();
    }

    private void setEvent() {
        actionBar.setDelegation(new CustomActionBar.ActionBarDelegation() {
            @Override
            public void backOnClick() {
                Intent intent1 = new Intent(getApplicationContext(),DanhSachNguyenLieu.class);
                startActivity(intent1);
            }
        });
        actionBar.setActionBarName("Chi tiết nguyên liệu");

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        String maNL = bundle.getString("maNL");
        getDataNguyenLieu(maNL);
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myref.child(maNL).removeValue();
            }
        });
        btnThayDoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myref.orderByChild("tenNL").equalTo(edTenNL.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            edTenNL.setError("Tên nguyên liệu đã tồn tại");
                        }else{
                            if (kiemtratrong()){
                                nguyenLieu.setMaNL(maNL);
                                nguyenLieu.setGia(Integer.parseInt(edGia.getText().toString()));
                                nguyenLieu.setMoTa(edMoTa.getText().toString());
                                nguyenLieu.setDonVi(spDonVi.getSelectedItem().toString());
                                nguyenLieu.setTenNL(edTenNL.getText().toString());
                                myref.child(maNL).setValue(nguyenLieu);
                                Intent intent2 = new Intent(getApplicationContext(),DanhSachNguyenLieu.class);
                                startActivity(intent2);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }

    private void getDataNguyenLieu(String maNL) {
        myref.child(maNL).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                nguyenLieu = snapshot.getValue(NguyenLieu.class);
                if(nguyenLieu != null){
                    edTenNL.setText(nguyenLieu.getTenNL());
                    edGia.setText(String.valueOf(nguyenLieu.getGia()));
                    edMoTa.setText(nguyenLieu.getMoTa());
                    if(nguyenLieu.getDonVi().compareTo("Kg") == 0){
                        spDonVi.setSelection(0);
                    }else{
                        spDonVi.setSelection(1);
                    }
                }else{
                    Intent intent1 = new Intent(getApplicationContext(),DanhSachNguyenLieu.class);
                    startActivity(intent1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setControl() {
        edTenNL = findViewById(R.id.edTenNL);
        edGia = findViewById(R.id.edGiaNL);
        edMoTa = findViewById(R.id.edMota);
        spDonVi = findViewById(R.id.spDonVi);
        btnXoa = findViewById(R.id.btnXoa);
        btnThayDoi = findViewById(R.id.btnThayDoi);
        actionBar = findViewById(R.id.actionBar);
    }
    private boolean kiemtratrong (){
        boolean kiemtra = true;
        if (edTenNL.getText().toString().trim().length() == 0){
            edTenNL.setError("Nhập tên nguyên liệu !");
            kiemtra = false;
        }
        if (edMoTa.getText().toString().trim().length() == 0){
            edMoTa.setError("Nhập mô tả nguyên liệu !");
            kiemtra = false;
        }
        if (edGia.getText().toString().trim().length() == 0){
            edGia.setError("Nhập giá nguyên liệu !");
            kiemtra = false;
        }
        return kiemtra;
    }
}