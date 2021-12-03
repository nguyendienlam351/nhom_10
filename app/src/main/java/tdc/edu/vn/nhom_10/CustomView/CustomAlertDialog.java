package tdc.edu.vn.nhom_10.CustomView;

import androidx.appcompat.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import tdc.edu.vn.nhom_10.R;

public class CustomAlertDialog extends AlertDialog {
    private Context context;
    private String title;
    private String content;
    private int type;
    public static final int SUCCESS = 1;
    public static final int ERROR = 2;

    public CustomAlertDialog(@NonNull Context context, String title, String content, int type) {
        super(context);
        this.context = context;
        this.title = title;
        this.content = content;
        this.type = type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_alert_dialog);
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        TextView tvContent = (TextView) findViewById(R.id.tvContent);
        Button btnDong = (Button) findViewById(R.id.btnDong);

        if (type == SUCCESS){
            tvTitle.setBackgroundColor(ContextCompat.getColor(context, R.color.green));
            btnDong.setBackgroundColor(ContextCompat.getColor(context, R.color.green));
        }
        else {
            tvTitle.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
            btnDong.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
        }

        tvTitle.setText(title);
        tvContent.setText(content);

        btnDong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }
}
