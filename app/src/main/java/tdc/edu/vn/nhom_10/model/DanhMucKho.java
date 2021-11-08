package tdc.edu.vn.nhom_10.model;

import java.util.HashMap;
import java.util.Map;

public class DanhMucKho {
    String maDanhMucKho;
    String ten;
    String soLuong;
    String ngay;
    String donVi;
    int gia;
    String moTa;

    public DanhMucKho() {
    }

    public DanhMucKho(String maDanhMucKho, String ten, String soLuong,String ngay , String donVi, int gia, String moTa) {

        this.ten = ten;
        this.soLuong = soLuong;
        this.ngay = ngay;
        this.donVi = donVi;
        this.gia = gia;
        this.moTa = moTa;
        this.maDanhMucKho = maDanhMucKho;
    }

    public String getMaDanhMucKho() {
        return maDanhMucKho;
    }

    public void setMaDanhMucKho(String maDanhMucKho) {
        this.maDanhMucKho = maDanhMucKho;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
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

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(String soLuong) {
        this.soLuong = soLuong;
    }

    public String getNgay() {
        return ngay;
    }

    public void setNgay(String ngay) {
        this.ngay = ngay;
    }
    public Map<String, Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("maDanhMucKho",maDanhMucKho);
        result.put("ten",ten);
        result.put("soLuong",soLuong);
        result.put("ngay",ngay);
        result.put("donVi",donVi);
        result.put("gia",gia);
        result.put("moTa",moTa);
        return result;
    }
}
