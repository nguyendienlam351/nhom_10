package tdc.edu.vn.nhom_10.model;

import java.util.HashMap;
import java.util.Map;

public class LoaiMon {
    String tenLoaiMon;
    String maLoaiMon;

    public LoaiMon() {
    }

    public LoaiMon(String tenLoaiMon) {
        this.tenLoaiMon = tenLoaiMon;
    }

    public String getTenLoaiMon() {
        return tenLoaiMon;
    }

    public void setTenLoaiMon(String tenLoaiMon) {
        this.tenLoaiMon = tenLoaiMon;
    }

    public String getMaLoaiMon() {
        return maLoaiMon;
    }

    public void setMaLoaiMon(String maLoaiMon) {
        this.maLoaiMon = maLoaiMon;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("tenLoaiMon", tenLoaiMon);
        return result;
    }

    @Override
    public String toString() {
        return tenLoaiMon;
    }
}
