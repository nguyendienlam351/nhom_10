package tdc.edu.vn.nhom_10.model;

public class HoatDongTrongNgay {
    String tenHD;
    String ngay;
    String moTa;

    public HoatDongTrongNgay() {
    }

    public HoatDongTrongNgay(String ngay, String moTa) {
        this.ngay = ngay;
        this.moTa = moTa;
    }

    public String getTenHD() {
        return tenHD;
    }

    public void setTenHD(String tenHD) {
        this.tenHD = tenHD;
    }

    public String getNgay() {
        return ngay;
    }

    public void setNgay(String ngay) {
        this.ngay = ngay;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }
}

