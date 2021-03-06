package tdc.edu.vn.nhom_10.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MonAn implements Serializable {

        String maMon,tenMon,anh,loaiMon,moTa;
        int Gia;

        public MonAn() {
        }

        public MonAn(String maMon, String tenMon, String anh, String loaiMon, String moTa, int gia) {
            this.maMon = maMon;
            this.tenMon = tenMon;
            this.anh = anh;
            this.loaiMon = loaiMon;
            this.moTa = moTa;
            Gia = gia;
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

        public String getAnh() {
            return anh;
        }

        public void setAnh(String anh) {
            this.anh = anh;
        }

        public String getLoaiMon() {
            return loaiMon;
        }

        public void setLoaiMon(String loaiMon) {
            this.loaiMon = loaiMon;
        }

        public String getMoTa() {
            return moTa;
        }

        public void setMoTa(String moTa) {
            this.moTa = moTa;
        }

        public int getGia() {
            return Gia;
        }

        public void setGia(int gia) {
            Gia = gia;
        }

        @Override
        public String toString() {
            return "MonAn{" +
                    "maMon='" + maMon + '\'' +
                    ", tenMon='" + tenMon + '\'' +
                    ", anh='" + anh + '\'' +
                    ", loaiMon='" + loaiMon + '\'' +
                    ", moTa='" + moTa + '\'' +
                    ", Gia=" + Gia +
                    '}';
        }

    public Map<String, Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("tenMon",tenMon);
        result.put("loaiMon",loaiMon);
        result.put("moTa",moTa);
        result.put("maMon",maMon);
        result.put("Gia",Gia);
        result.put("anh",anh);
        return result;
    }
    }
