package tdc.edu.vn.nhom_10;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import tdc.edu.vn.nhom_10.CustomView.CustomActionBar;
import tdc.edu.vn.nhom_10.model.MonAn;
import tdc.edu.vn.nhom_10.model.NhanVien;

public class ChiTietQLMon extends AppCompatActivity {

    EditText edtTenMon, edtGia, edtMoTa;
    Spinner spMon;
    Button btnThayDoi, btnXoa;
    ImageView imgHinh;
    CustomActionBar actionBar;
    MonAn monAn;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_ql_mon);
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


    }




    private void setControl() {
        edtTenMon = findViewById(R.id.edtTenMon);
        edtGia = findViewById(R.id.edtGia);
        edtMoTa = findViewById(R.id.edtMota);
        imgHinh = findViewById(R.id.imgHinh);
        spMon = findViewById(R.id.spMon);
        btnThayDoi = findViewById(R.id.btnThayDoi);
        btnXoa = findViewById(R.id.btnXoa);
        actionBar = findViewById(R.id.actionBar);

    }
}
