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

import tdc.edu.vn.nhom_10.CustomView.CustomAlertDialog;
import tdc.edu.vn.nhom_10.CustomView.QuenMatKhauDialog;

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
                    edTaikhoan.setError("Vui l??ng nh???p email !");
                    return;
                }
                if (TextUtils.isEmpty(pass)) {
                    edMatkhau.setError("Vui l??ng nh???p m???t kh???u !");
                    return;
                }
                mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(DangNhap.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("NhanVien/"+mAuth.getUid());
                            myRef.child("chucVu").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String value = snapshot.getValue(String.class);
                                    if(value.compareTo("Qu???n l??")==0){
                                        Intent intent = new Intent(getApplicationContext(),QuanLy.class);
                                        startActivity(intent);
                                    }else if (value.compareTo("Pha ch???")==0){
                                        Intent intent = new Intent(getApplicationContext(),PhaChe.class);
                                        startActivity(intent);
                                    }else if (value.compareTo("Ph???c v???")==0){
                                        Intent intent = new Intent(getApplicationContext(),PhucVu.class);
                                        startActivity(intent);
                                    }else if (value.compareTo("Thu ng??n")==0){
                                        Intent intent = new Intent(getApplicationContext(), ThuNgan.class);
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }else{
                            new CustomAlertDialog(DangNhap.this,"Th???t b???i","????ng nh???p th???t b???i.\nSai t??i kho???n ho???c m???t kh???u.",CustomAlertDialog.ERROR).show();
                            edMatkhau.setText("");
                            edTaikhoan.setText("");
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
        QuenMatKhauDialog.FullNameListener listener = new QuenMatKhauDialog.FullNameListener() {
            @Override
            public void fullNameEntered(String email) {
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            new CustomAlertDialog(DangNhap.this,"Th??nh c??ng","???ng d???ng ???? g???i link ?????i m???t kh???u v??? Email",CustomAlertDialog.SUCCESS).show();
                        } else {
                            new CustomAlertDialog(DangNhap.this,"Th???t b???i","G???i link th???t b???i.\nEmail kh??ng ????ng.",CustomAlertDialog.ERROR).show();
                        }
                    }
                });
            }
        };
        final QuenMatKhauDialog dialog = new QuenMatKhauDialog(this, listener);

        dialog.show();
    }
}