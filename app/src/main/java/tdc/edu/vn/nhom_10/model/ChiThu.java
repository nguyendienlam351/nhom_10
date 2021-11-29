package tdc.edu.vn.nhom_10.model;

public class ChiThu {
    String maThuChi;
    String loaiThuChi;
    String ngayNhap;
    String nguoiNhap;
    String loai;
    int soTien;
    String moTa;

    public String getMaThuChi() {
        return maThuChi;
    }

    public void setMaThuChi(String maThuChi) {
        this.maThuChi = maThuChi;
    }

    public String getLoaiThuChi() {
        return loaiThuChi;
    }

    public void setLoaiThuChi(String loaiThuChi) {
        this.loaiThuChi = loaiThuChi;
    }

    public String getNgayNhap() {
        return ngayNhap;
    }

    public void setNgayNhap(String ngayNhap) {
        this.ngayNhap = ngayNhap;
    }

    public String getNguoiNhap() {
        return nguoiNhap;
    }

    public void setNguoiNhap(String nguoiNhap) {
        this.nguoiNhap = nguoiNhap;
    }

    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }

    public int getSoTien() {
        return soTien;
    }

    public void setSoTien(int soTien) {
        this.soTien = soTien;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public ChiThu() {
    }

    public ChiThu(String maThuChi, String loaiThuChi, String ngayNhap, String nguoiNhap, String loai, int soTien, String moTa) {
        this.maThuChi = maThuChi;
        this.loaiThuChi = loaiThuChi;
        this.ngayNhap = ngayNhap;
        this.nguoiNhap = nguoiNhap;
        this.loai = loai;
        this.soTien = soTien;
        this.moTa = moTa;
    }

}
