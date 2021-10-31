package tdc.edu.vn.nhom_10.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DonHang {
    String maBan;
    String tenBan;
    ArrayList<ChiTietDonHang> chiTietDonHang = new ArrayList<ChiTietDonHang>();

    public DonHang() {
    }

    public ArrayList<ChiTietDonHang> getChiTietDonHang() {
        return chiTietDonHang;
    }

    public void setChiTietDonHang(ArrayList<ChiTietDonHang> chiTietDonHang) {
        this.chiTietDonHang = chiTietDonHang;
    }

    public String getMaBan() {
        return maBan;
    }

    public void setMaBan(String maBan) {
        this.maBan = maBan;
    }

    public String getTenBan() {
        return tenBan;
    }

    public void setTenBan(String tenban) {
        this.tenBan = tenban;
    }

    @Override
    public String toString() {
        return "Ban{" +
                "maBan='" + maBan + '\'' +
                ", tenBan='" + tenBan + '\'' +
                '}';
    }
}
