package tdc.edu.vn.nhom_10.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NhanVien {
    String hoTen;
    String soDienThoai;
    String ngaySinh;
    String soCCCD;
    String email;
    String diaChi;
    String chucVu;
    String maNV;
    String anh;

    public NhanVien() {
      //Mặc định của firebase, khi nhận data
    }
    public NhanVien(String hoTen, String soDienThoai, String ngaySinh, String soCCCD, String email, String diaChi, String chucVu, String maNV, String anh) {
        this.hoTen = hoTen;
        this.soDienThoai = soDienThoai;
        this.ngaySinh = ngaySinh;
        this.soCCCD = soCCCD;
        this.email = email;
        this.diaChi = diaChi;
        this.maNV = maNV;
        this.chucVu = chucVu;
        this.anh = anh;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getSoCCCD() {
        return soCCCD;
    }

    public void setSoCCCD(String soCCCD) {
        this.soCCCD = soCCCD;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getAnh() {
        return anh;
    }

    public void setanh(String hinhAnh) {
        this.anh = hinhAnh;
    }

    public Map<String, Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("hoTen",hoTen);
        result.put("soDienThoai",soDienThoai);
        result.put("ngaySinh",ngaySinh);
        result.put("soCCCD",soCCCD);
        result.put("email",email);
        result.put("diaChi",diaChi);
        result.put("chucVu",chucVu);
        result.put("maNV",maNV);
        result.put("anh",anh);
        return result;
    }
}
