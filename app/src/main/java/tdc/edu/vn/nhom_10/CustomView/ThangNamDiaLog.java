package tdc.edu.vn.nhom_10.CustomView;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import tdc.edu.vn.nhom_10.R;

public class ThangNamDiaLog extends AlertDialog {
    Context context;
    TextView tvThang;
    TextView tvNam;
    ImageButton imbGiamThang;
    ImageButton imbTangThang;
    ImageButton imbGiamNam;
    ImageButton imbTangNam;
    private Button btnHuy, btnDongY;
    private ThangNamDiaLog.ThangNamDiaLogListener listener;
    int thang, nam;

    public ThangNamDiaLog(Context context, int thang, int nam, ThangNamDiaLog.ThangNamDiaLogListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
        this.thang =thang;
        this.nam= nam;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dialog_chon_tg);

        this.tvThang = (TextView) findViewById(R.id.tvThang);
        this.tvNam = (TextView) findViewById(R.id.tvNam);
        this.imbGiamNam = (ImageButton) findViewById(R.id.imbGiamNam);
        this.imbTangNam = (ImageButton) findViewById(R.id.imbTangNam);
        this.imbGiamThang = (ImageButton) findViewById(R.id.imbGiamThang);
        this.imbTangThang = (ImageButton) findViewById(R.id.imbTangThang);
        this.btnHuy = (Button) findViewById(R.id.btnHuy);
        this.btnDongY = (Button) findViewById(R.id.btnDongY);

        tvThang.setText("Tháng: " + thang);
        tvNam.setText("Năm: " + nam);

        this.btnDongY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.dongYClick(thang, nam);
                dismiss();
            }
        });

        this.btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        //Sự kiện khi nhấn nút giảm của tháng
        this.imbGiamThang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (thang > 1) {
                    thang--;
                    tvThang.setText("Tháng: " + thang);
                } else {
                    thang = 12;
                    tvThang.setText("Tháng: " + thang);
                }
            }
        });

        //Sự kiện khi nhấn nút tăng của tháng
        this.imbTangThang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (thang < 12) {
                    thang++;
                    tvThang.setText("Tháng: " + thang);
                } else {
                    thang = 1;
                    tvThang.setText("Tháng: " + thang);
                }
            }
        });

        //Sự kiện khi nhấn nút giảm của năm
        this.imbGiamNam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nam > 2000) {
                    nam--;
                    tvNam.setText("Năm: " + nam);
                }
            }
        });

        //Sự kiện khi nhấn nút tăng của năm
        this.imbTangNam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nam < 2999) {
                    nam++;
                    tvNam.setText("Năm: " + nam);
                }
            }
        });
    }

    public interface ThangNamDiaLogListener {
        public void dongYClick(int thang, int nam);
    }
}