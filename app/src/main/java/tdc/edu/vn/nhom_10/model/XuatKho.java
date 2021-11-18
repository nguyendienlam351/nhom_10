package tdc.edu.vn.nhom_10.model;

import java.util.HashMap;
import java.util.Map;

public class XuatKho {
    NguyenLieu nguyenLieu;
    String maXuatKho;
    String tenXuatKho;
    String ngayXuatKho;
    int soLuong;
    String donVi;

    public XuatKho() {
    }

    public XuatKho(NguyenLieu nguyenLieu, String maXuatKho, String tenXuatKho, String ngayXuatKho, int soLuong, String donVi) {
        this.nguyenLieu = nguyenLieu;
        this.maXuatKho = maXuatKho;
        this.tenXuatKho = tenXuatKho;
        this.ngayXuatKho = ngayXuatKho;
        this.soLuong = soLuong;
        this.donVi = donVi;
    }

    public NguyenLieu getNguyenLieu() {
        return nguyenLieu;
    }

    public void setNguyenLieu(NguyenLieu nguyenLieu) {
        this.nguyenLieu = nguyenLieu;
    }

    public String getMaXuatKho() {
        return maXuatKho;
    }

    public void setMaXuatKho(String maXuatKho) {
        this.maXuatKho = maXuatKho;
    }

    public String getTenXuatKho() {
        return tenXuatKho;
    }

    public void setTenXuatKho(String tenXuatKho) {
        this.tenXuatKho = tenXuatKho;
    }

    public String getNgayXuatKho() {
        return ngayXuatKho;
    }

    public void setNgayXuatKho(String ngayXuatKho) {
        this.ngayXuatKho = ngayXuatKho;
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
        result.put("maXuatKho",maXuatKho);
        result.put("tenXuatKho",tenXuatKho);
        result.put("ngayXuatKho",ngayXuatKho);
        result.put("soLuong",soLuong);
        result.put("donVi",donVi);
        return result;
    }
}
