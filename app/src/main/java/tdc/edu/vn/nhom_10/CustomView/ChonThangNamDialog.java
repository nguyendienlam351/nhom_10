package tdc.edu.vn.nhom_10.CustomView;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import tdc.edu.vn.nhom_10.R;

public class ChonThangNamDialog extends Dialog {
    int Thang, Nam;
    int thangTam;
    int namTam;

    public interface FullNameListener {
        public void fullNameEntered(String fullName);
    }
    public Context context;
    TextView tvThang;
    TextView tvNam;
    ImageButton imbGiamThang;
    ImageButton imbTangThang;
    ImageButton imbGiamNam;
    ImageButton imbTangNam;
    private Button btnHuy, btnDongY;
    private ChonThangNamDialog.FullNameListener listener;
    public ChonThangNamDialog(Context context, ChonThangNamDialog.FullNameListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_dialog_chon_tg);

        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        Thang = today.month+ 1;
        Nam = today.year;

        this.tvThang = (TextView) findViewById(R.id.tvThang);
        this.tvNam = (TextView) findViewById(R.id.tvNam);
        this.imbGiamNam  =(ImageButton) findViewById(R.id.imbGiamNam);
        this.imbTangNam  =(ImageButton) findViewById(R.id.imbTangNam);
        this.imbGiamThang  =(ImageButton) findViewById(R.id.imbGiamThang);
        this.imbTangThang  =(ImageButton) findViewById(R.id.imbTangThang);
        this.btnHuy = (Button) findViewById(R.id.btnHuy);
        this.btnDongY  = (Button) findViewById(R.id.btnDongY);

        this.btnDongY .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gui();
                dismiss();
            }
        });
        this.btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                huy();
            }
        });
        thangTam=Thang;
        namTam=Nam;
        tvThang.setText("Tháng: "+thangTam);
        tvNam.setText("Năm: "+namTam);
        //Sự kiện khi nhấn nút giảm của tháng
        this.imbGiamThang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(thangTam > 1){
                    thangTam--;
                    tvThang.setText("Tháng: "+thangTam);
                }
                else{
                    thangTam=12;
                    tvThang.setText("Tháng: "+thangTam);
                }
            }
        });
        //Sự kiện khi nhấn nút tăng của tháng
        this.imbTangThang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(thangTam < 12){
                    thangTam++;
                    tvThang.setText("Tháng: "+thangTam);
                }
                else{
                    thangTam=1;
                    tvThang.setText("Tháng: "+thangTam);
                }
            }
        });
        //Sự kiện khi nhấn nút giảm của năm
        this.imbGiamNam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(namTam > 2000){
                    namTam--;
                    tvNam.setText("Năm: "+namTam);
                }
            }
        });
        //Sự kiện khi nhấn nút tăng của năm
        this.imbTangNam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(namTam < 2999){
                    namTam++;
                    tvNam.setText("Năm: "+namTam);
                }
            }
        });
    }
    private void gui(){
        String Thang = this.tvThang.getText().toString().split(" ")[1];
        String Nam = this.tvNam.getText().toString().split(" ")[1];
        String gopTN = Thang + "/"+ Nam;

        if(this.listener!= null)  {
            this.listener.fullNameEntered(gopTN);
        }
    }
    private void huy()  {
        this.dismiss();
    }
}
