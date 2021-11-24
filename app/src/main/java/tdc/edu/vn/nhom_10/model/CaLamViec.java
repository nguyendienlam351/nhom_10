package tdc.edu.vn.nhom_10.model;

import java.util.ArrayList;

public class CaLamViec {
    String maLichLamViec;
    String thuNgay;
    ArrayList<NhanVien> caA = new ArrayList<>();
    ArrayList<NhanVien> caB = new ArrayList<>();
    ArrayList<NhanVien> caC = new ArrayList<>();
    NhanVien nhanVien;

    public CaLamViec(){};

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public String getMaLichLamViec() {
        return maLichLamViec;
    }

    public void setMaLichLamViec(String maLichLamViec) {
        this.maLichLamViec = maLichLamViec;
    }

    public String getThuNgay() {
        return thuNgay;
    }

    public void setThuNgay(String thuNgay) {
        this.thuNgay = thuNgay;
    }

    public ArrayList<NhanVien> getCaA() {
        return caA;
    }

    public void setCaA(ArrayList<NhanVien> caA) {
        this.caA = caA;
    }

    public ArrayList<NhanVien> getCaB() {
        return caB;
    }

    public void setCaB(ArrayList<NhanVien> caB) {
        this.caB = caB;
    }

    public ArrayList<NhanVien> getCaC() {
        return caC;
    }

    public void setCaC(ArrayList<NhanVien> caC) {
        this.caC = caC;
    }
}
