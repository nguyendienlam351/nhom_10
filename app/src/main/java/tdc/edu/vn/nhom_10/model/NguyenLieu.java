package tdc.edu.vn.nhom_10.model;

import java.util.HashMap;
import java.util.Map;

public class NguyenLieu {
    String maNL;
    String tenNL;
    String moTa;
    String donVi;
    int gia;
    int soLuong;

    public NguyenLieu() {
    }

    public NguyenLieu(String maNL, String tenNL, String moTa, String donVi, int gia, int soLuong) {
        this.maNL = maNL;
        this.tenNL = tenNL;
        this.moTa = moTa;
        this.donVi = donVi;
        this.gia = gia;
        this.soLuong = soLuong;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public String getMaNL() {
        return maNL;
    }

    public void setMaNL(String maNL) {
        this.maNL = maNL;
    }

    public String getTenNL() {
        return tenNL;
    }

    public void setTenNL(String tenNL) {
        this.tenNL = tenNL;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getDonVi() {
        return donVi;
    }

    public void setDonVi(String donVi) {
        this.donVi = donVi;
    }

    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }

    public Map<String, Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("maNL",maNL);
        result.put("tenNL",tenNL);
        result.put("moTa",moTa);
        result.put("donVi",donVi);
        result.put("gia",gia);
        result.put("soLuong",soLuong);
        return result;
    }
}
