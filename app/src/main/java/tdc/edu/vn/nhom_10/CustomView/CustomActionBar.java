package tdc.edu.vn.nhom_10.CustomView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import tdc.edu.vn.nhom_10.R;

public class CustomActionBar extends LinearLayout {
    private ViewGroup actionBarLayout;
    private ImageButton btnBack;
    private TextView tvActionBarName;
    private ActionBarDelegation delegation = null;

    public void setActionBarName(String actionBarName){
        tvActionBarName.setText(actionBarName);
    }


    public void setDelegation(ActionBarDelegation delegation) {
        this.delegation = delegation;
    }

    public CustomActionBar(Context context) {
        super(context);
        init();
    }

    public CustomActionBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomActionBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("NewApi")
    public CustomActionBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }
    private void init(){
        inflate(getContext(), R.layout.layout_action_bar, this);

        actionBarLayout = (ViewGroup) getChildAt(0);

        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(delegation != null){
                    delegation.backOnClick();
                }
                else {
                    Toast.makeText(getContext(), "you must set delegation before", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvActionBarName = findViewById(R.id.tvActionBarName);
    }

    public interface ActionBarDelegation{
        public void backOnClick();
    }
}
