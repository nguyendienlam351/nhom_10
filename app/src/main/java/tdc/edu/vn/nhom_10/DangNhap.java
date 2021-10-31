package tdc.edu.vn.nhom_10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import tdc.edu.vn.nhom_10.QuanLyFragment.ThuChi;

public class DangNhap extends AppCompatActivity {
    CheckBox cbHienmk;
    Button btnQuenmk,btnDangNhap;
    EditText edMatkhau,edTaikhoan;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        setControl();
        setEvent();
    }

    private void setEvent() {
        btnQuenmk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quenmk();
            }
        });
        cbHienmk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbHienmk.isChecked() == true){
                    edMatkhau.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                else{
                    edMatkhau.setInputType(InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, pass;
                email = edTaikhoan.getText().toString().trim();
                pass = edMatkhau.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Nhập email của bạn !", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(pass)) {
                    Toast.makeText(getApplicationContext(), "Nhập mật khẩu của bạn !", Toast.LENGTH_LONG).show();
                    return;
                }
                mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(DangNhap.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Đăng nhập thành công !", Toast.LENGTH_LONG).show();
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("NhanVien/"+mAuth.getUid());
                            myRef.child("chucVu").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String value = snapshot.getValue(String.class);
                                    if(value.compareTo("Quản lý")==0){
                                        Intent intent = new Intent(getApplicationContext(),QuanLy.class);
                                        startActivity(intent);
                                    }else if (value.compareTo("Pha chế")==0){
                                        Intent intent = new Intent(getApplicationContext(),PhaChe.class);
                                        startActivity(intent);
                                    }else if (value.compareTo("Phục vụ")==0){
                                        Intent intent = new Intent(getApplicationContext(),PhucVu.class);
                                        startActivity(intent);
                                    }else if (value.compareTo("Thu ngân")==0){
                                        Intent intent = new Intent(getApplicationContext(), ThuNgan.class);
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }else{
                            Toast.makeText(getApplicationContext(), "Đăng nhập thất bại \n"+task.getException(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    private void setControl() {
        mAuth = FirebaseAuth.getInstance();
        cbHienmk = findViewById(R.id.cdHienmk);
        edMatkhau = findViewById(R.id.edMatkhau);
        edTaikhoan = findViewById(R.id.edTaikhoan);
        btnQuenmk = findViewById(R.id.btnQuenmk);
        btnDangNhap = findViewById(R.id.btnDangNhap);
    }
    private void quenmk()  {
        CustomDialog.FullNameListener listener = new CustomDialog.FullNameListener() {
            @Override
            public void fullNameEntered(String email) {
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(DangNhap.this, "Ứng dụng đã gửi link đổi mật khẩu về Email", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(DangNhap.this, "Gửi link thất bại, vui lòng nhập đúng Email", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        };
        final CustomDialog dialog = new CustomDialog(this, listener);

        dialog.show();
    }
}