package tdc.edu.vn.nhom_10.model;

import java.util.HashMap;
import java.util.Map;

public class NhapKho {
    NguyenLieu nguyenLieu;
    String maNhapKho;
    String tenNhapKho;
    String ngayNhapKho;
    int soLuong;
    String donVi;

    public NhapKho() {
    }

    public NhapKho(NguyenLieu nguyenLieu, String maNhapKho, String tenNhapKho, String ngayNhapKho, int soLuong, String donVi) {
        this.nguyenLieu = nguyenLieu;
        this.maNhapKho = maNhapKho;
        this.tenNhapKho = tenNhapKho;
        this.ngayNhapKho = ngayNhapKho;
        this.soLuong = soLuong;
        this.donVi = donVi;
    }

    public NguyenLieu getNguyenLieu() {
        return nguyenLieu;
    }

    public void setNguyenLieu(NguyenLieu nguyenLieu) {
        this.nguyenLieu = nguyenLieu;
    }

    public String getMaNhapKho() {
        return maNhapKho;
    }

    public void setMaNhapKho(String maNhapKho) {
        this.maNhapKho = maNhapKho;
    }

    public String getTenNhapKho() {
        return tenNhapKho;
    }

    public void setTenNhapKho(String tenNhapKho) {
        this.tenNhapKho = tenNhapKho;
    }

    public String getNgayNhapKho() {
        return ngayNhapKho;
    }

    public void setNgayNhapKho(String ngayNhapKho) {
        this.ngayNhapKho = ngayNhapKho;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public String getDonVi() {
        return donVi;
    }

    public void setDonVi(String donVi) {
        this.donVi = donVi;
    }

    public Map<String, Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("nguyenLieu",nguyenLieu);
        result.put("maNhapKho",maNhapKho);
        result.put("tenNhapKho",tenNhapKho);
        result.put("ngayNhapKho",ngayNhapKho);
        result.put("soLuong",soLuong);
        result.put("donVi",donVi);
        return result;
    }
}
