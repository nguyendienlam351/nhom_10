package tdc.edu.vn.nhom_10;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.SearchView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import tdc.edu.vn.nhom_10.adapter.XemHoaDonAdapter;
import tdc.edu.vn.nhom_10.model.HoaDon;

public class XemHoaDon extends AppCompatActivity {
    SearchView svTimKiem;
    RecyclerView lvHoaDon;
    ArrayList<HoaDon> data = new ArrayList<HoaDon>();
    XemHoaDonAdapter myRecyclerViewAdapter;
    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_hoa_don);
    }
}