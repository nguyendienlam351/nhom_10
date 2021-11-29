package tdc.edu.vn.nhom_10;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import tdc.edu.vn.nhom_10.CustomView.CustomActionBar;
import tdc.edu.vn.nhom_10.CustomView.MinMaxFilter;
import tdc.edu.vn.nhom_10.model.NguyenLieu;

public class ThemNguyenLieu extends AppCompatActivity {

    EditText edTenNL,edGiaNL,edMoTa;
    Spinner spDonVi;
    Button btnThem;
    DatabaseReference myref;
    CustomActionBar actionBar;
    ArrayList<NguyenLieu> data = new ArrayList<NguyenLieu>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_nguyen_lieu);
        setControl();
        setEvent();
    }

    private void setEvent() {
        actionBar.setDelegation(new CustomActionBar.ActionBarDelegation() {
            @Override
            public void backOnClick() {
                finish();
            }
        });
        actionBar.setActionBarName("Thêm nguyên liệu");

        myref = FirebaseDatabase.getInstance().getReference("NguyenLieu");
        edGiaNL.setFilters(new InputFilter[]{new MinMaxFilter(1, Integer.MAX_VALUE)});
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myref.orderByChild("tenNL").equalTo(edTenNL.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            edTenNL.setError("Tên nguyên liệu đã tồn tại !");
                        }else{
                            if(kiemtratrong() == true){
                                NguyenLieu nguyenLieu = new NguyenLieu();
                                String maNguyenlieu = myref.push().getKey();
                                nguyenLieu.setGia(Integer.parseInt(edGiaNL.getText().toString()));
                                nguyenLieu.setSoLuong(0);
                                nguyenLieu.setTenNL(edTenNL.getText().toString());
                                nguyenLieu.setMoTa(edMoTa.getText().toString());
                                nguyenLieu.setDonVi(spDonVi.getSelectedItem().toString());
                                nguyenLieu.setMaNL(maNguyenlieu);

                                myref.child(maNguyenlieu).setValue(nguyenLieu);

                                Intent intent = new Intent(getApplicationContext(),DanhSachNguyenLieu.class);
                                startActivity(intent);
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

    private void setControl() {
        edTenNL = findViewById(R.id.edTenNL);
        edGiaNL = findViewById(R.id.edGiaNL);
        edMoTa = findViewById(R.id.edMota);
        spDonVi = findViewById(R.id.spDonVi);
        btnThem = findViewById(R.id.btnThem);
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
        if (edGiaNL.getText().toString().trim().length() == 0){
            edGiaNL.setError("Nhập giá nguyên liệu !");
            kiemtra = false;
        }
        return kiemtra;
    }

}