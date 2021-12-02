package tdc.edu.vn.nhom_10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import tdc.edu.vn.nhom_10.CustomView.CustomActionBar;
import tdc.edu.vn.nhom_10.CustomView.CustomAlertDialog;
import tdc.edu.vn.nhom_10.CustomView.MinMaxFilter;
import tdc.edu.vn.nhom_10.model.MaGiamGia;

public class ChiTietMaGiamGia extends AppCompatActivity {
    CustomActionBar actionBar;
    EditText edtTenMa;
    EditText edtGiaTriApDung;
    EditText edtPhanTramGiamGia;
    EditText edtSoLuong;
    ImageButton btnDate1;
    ImageButton btnDate2;
    TextView tvNgayBatDau;
    TextView tvNgayKetThuc;
    MaGiamGia maGiamGia;
    Button btnXoa;
    Button btnThayDoi;
    Calendar ngayBatDau;
    Calendar ngayKetThuc;
    DatabaseReference database;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_ma_giam_gia);
        setControl();
        setEvent();

    }

    private void setEvent() {
        //Actionbar
        actionBar.setDelegation(new CustomActionBar.ActionBarDelegation() {
            @Override
            public void backOnClick() {
                Intent intent = new Intent(ChiTietMaGiamGia.this, QuanLyMaGiamGia.class);
                startActivity(intent);
            }
        });
        actionBar.setActionBarName("Chi tiết mã giảm giá");

        database = FirebaseDatabase.getInstance().getReference("MaGiamGia");

        ngayBatDau = Calendar.getInstance();
        ngayKetThuc = Calendar.getInstance();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            //Lấy dữ liệu mã giảm giá theo mã
            String ma = bundle.getString("maGiamGia", "");
            getDataMaGiamGia(ma);
        }

        //Hiển thị dialog chọn ngày bắt đầu
        btnDate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ChiTietMaGiamGia.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        ngayBatDau.set(year, month, dayOfMonth);
                        tvNgayBatDau.setText("Ngày bắt đầu: " + dateFormat.format(ngayBatDau.getTime()));

                        if (ngayBatDau.compareTo(ngayKetThuc) > 0) {
                            ngayKetThuc.set(year, month, dayOfMonth);
                            tvNgayKetThuc.setText("Ngày kết thúc: " + dateFormat.format(ngayKetThuc.getTime()));
                        }
                    }
                }, ngayBatDau.get(Calendar.YEAR), ngayBatDau.get(Calendar.MONTH), ngayBatDau.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();

            }
        });

        //Hiển thị dialog chọn ngày kết thúc
        btnDate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ChiTietMaGiamGia.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        ngayKetThuc.set(year, month, dayOfMonth);
                        tvNgayKetThuc.setText("Ngày kết thúc: " + dateFormat.format(ngayKetThuc.getTime()));
                    }
                }, ngayKetThuc.get(Calendar.YEAR), ngayKetThuc.get(Calendar.MONTH), ngayKetThuc.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.getDatePicker().setMinDate(ngayBatDau.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        //Ràng buộc dữ liệu nhập vào
        edtTenMa.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        edtGiaTriApDung.setFilters(new InputFilter[]{new MinMaxFilter(1, Integer.MAX_VALUE)});
        edtPhanTramGiamGia.setFilters(new InputFilter[]{new MinMaxFilter(1, 100)});
        edtSoLuong.setFilters(new InputFilter[]{new MinMaxFilter(1, Integer.MAX_VALUE)});

        //Thay đổi mã giảm giá
        btnThayDoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder b = new AlertDialog.Builder(ChiTietMaGiamGia.this);
                b.setTitle("Thay đổi");
                b.setMessage("Bạn có muốn thay đổi?");
                b.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (validated()) {
                            MaGiamGia nMaGiamGia = new MaGiamGia();
                            nMaGiamGia.setMaGiamGia(maGiamGia.getMaGiamGia());
                            nMaGiamGia.setTenMaGiamGia(edtTenMa.getText().toString());
                            nMaGiamGia.setNgayBatDau(dateFormat.format(ngayBatDau.getTime()));
                            nMaGiamGia.setNgayKetThuc(dateFormat.format(ngayKetThuc.getTime()));
                            nMaGiamGia.setGiaTriApDung(Integer.parseInt(edtGiaTriApDung.getText().toString()));
                            nMaGiamGia.setPhanTramGiamGia(Integer.parseInt(edtPhanTramGiamGia.getText().toString()));
                            nMaGiamGia.setSoLuong(Integer.parseInt(edtSoLuong.getText().toString()));

                            database.child(maGiamGia.getMaGiamGia()).setValue(nMaGiamGia).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Intent intent = new Intent(ChiTietMaGiamGia.this, QuanLyMaGiamGia.class);
                                    startActivity(intent);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    new CustomAlertDialog(ChiTietMaGiamGia.this,
                                            "Lỗi",
                                            e.toString(),
                                            CustomAlertDialog.ERROR).show();
                                }
                            });
                        }
                    }
                });
                b.setNegativeButton("Từ chối", null);
                b.setCancelable(false);
                b.show();
            }
        });

        //Xóa mã giảm giá
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder b = new AlertDialog.Builder(ChiTietMaGiamGia.this);
                b.setTitle("Xóa");
                b.setMessage("Bạn có muốn xóa?");
                b.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        database.child(maGiamGia.getMaGiamGia()).removeValue().addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                new CustomAlertDialog(ChiTietMaGiamGia.this,
                                        "Lỗi",
                                        e.toString(),
                                        CustomAlertDialog.ERROR).show();
                            }
                        });
                    }
                });

                b.setNegativeButton("Từ chối", null);
                b.setCancelable(false);
                b.show();
            }
        });
    }

    //Lấy dữ liệu mã giảm giá theo mã
    private void getDataMaGiamGia(String ma) {
        database.child(ma).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                maGiamGia = snapshot.getValue(MaGiamGia.class);
                if (maGiamGia != null) {
                    edtTenMa.setText(maGiamGia.getTenMaGiamGia());

                    try {
                        ngayBatDau.setTime(dateFormat.parse(maGiamGia.getNgayBatDau()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    try {
                        ngayKetThuc.setTime(dateFormat.parse(maGiamGia.getNgayKetThuc()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    tvNgayBatDau.setText("Ngày bắt đầu: " + dateFormat.format(ngayBatDau.getTime()));
                    tvNgayKetThuc.setText("Ngày kết thúc: " + dateFormat.format(ngayKetThuc.getTime()));

                    edtGiaTriApDung.setText(String.valueOf(maGiamGia.getGiaTriApDung()));
                    edtPhanTramGiamGia.setText(String.valueOf(maGiamGia.getPhanTramGiamGia()));
                    edtSoLuong.setText(String.valueOf(maGiamGia.getSoLuong()));
                } else {
                    Intent intent = new Intent(ChiTietMaGiamGia.this, QuanLyMaGiamGia.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //Kiểm tra dữ liệu mã giảm giá
    private boolean validated() {
        boolean checkValdated = true;
        if (edtTenMa.getText().toString().length() == 0) {
            edtTenMa.setError("Hãy nhập mã giảm giá");
            checkValdated = false;
        }
        if (edtGiaTriApDung.getText().length() == 0) {
            edtGiaTriApDung.setError("Hãy nhập giá trị áp dụng");
            checkValdated = false;
        }
        if (edtPhanTramGiamGia.getText().length() == 0) {
            edtPhanTramGiamGia.setError("Hãy nhập phần trăm giảm giá");
            checkValdated = false;
        }
        if (edtSoLuong.getText().length() == 0) {
            edtSoLuong.setError("Hãy nhập số lượng");
            checkValdated = false;
        }

        return checkValdated;
    }

    private void setControl() {
        actionBar = findViewById(R.id.actionBar);
        btnDate1 = findViewById(R.id.btnDate1);
        btnDate2 = findViewById(R.id.btnDate2);
        tvNgayBatDau = findViewById(R.id.tvNgayBatDau);
        tvNgayKetThuc = findViewById(R.id.tvNgayKetThuc);
        btnThayDoi = findViewById(R.id.btnThayDoi);
        btnXoa = findViewById(R.id.btnXoa);
        edtTenMa = findViewById(R.id.edtTenMa);
        edtSoLuong = findViewById(R.id.edtSoLuong);
        edtPhanTramGiamGia = findViewById(R.id.edtPhanTramGiamGia);
        edtGiaTriApDung = findViewById(R.id.edtGiaTriApDung);
    }


}