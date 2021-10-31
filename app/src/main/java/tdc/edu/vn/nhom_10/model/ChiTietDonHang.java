package tdc.edu.vn.nhom_10.model;

import android.media.Image;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ChiTietDonHang {
    String maMon;
    String tenMon;
    String loaiMon;
    int gia;
    String moTa;
    String anh;
    int soLuong = 1;
    String trangThai = "";

    public ChiTietDonHang() {
    }

    public String getMaMon() {
        return maMon;
    }

    public void setMaMon(String maMon) {
        this.maMon = maMon;
    }

    public String getTenMon() {
        return tenMon;
    }

    public void setTenMon(String tenMon) {
        this.tenMon = tenMon;
    }

    public String getLoaiMon() {
        return loaiMon;
    }

    public void setLoaiMon(String loaiMon) {
        this.loaiMon = loaiMon;
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

    public String getAnh() {
        return anh;
    }

    public void setAnh(String anh) {
        this.anh = anh;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public String toString() {
        return "Mon{" +
                "maMon='" + maMon + '\'' +
                ", tenMon='" + tenMon + '\'' +
                ", loaiMon='" + loaiMon + '\'' +
                ", gia=" + gia +
                ", moTa='" + moTa + '\'' +
                ", anh='" + anh + '\'' +
                ", soLuong=" + soLuong +
                '}';
    }
}
