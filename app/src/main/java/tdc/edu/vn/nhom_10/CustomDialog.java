package tdc.edu.vn.nhom_10;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CustomDialog extends Dialog {
    interface FullNameListener {
        public void fullNameEntered(String fullName);
    }
    public Context context;

    private EditText edEmail;
    private Button btnHuy, btnGui;
    private CustomDialog.FullNameListener listener;
    public CustomDialog(Context context, CustomDialog.FullNameListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.email_dialog);

        this.edEmail = (EditText) findViewById(R.id.edEmail);
        this.btnHuy = (Button) findViewById(R.id.btnHuy);
        this.btnGui  = (Button) findViewById(R.id.btnGui);

        this.btnGui .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gui();
            }
        });
        this.btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                huy();
            }
        });
    }
    private  void gui(){
        String email = this.edEmail.getText().toString();

        if(email== null || email.isEmpty())  {
            Toast.makeText(this.context, "Please enter your email", Toast.LENGTH_LONG).show();
            return;
        }
        this.dismiss(); // Close Dialog

        if(this.listener!= null)  {
            this.listener.fullNameEntered(email);
        }
    }
    private void huy()  {
        this.dismiss();
    }
}
