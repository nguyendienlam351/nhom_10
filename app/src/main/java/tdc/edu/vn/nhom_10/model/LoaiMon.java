package tdc.edu.vn.nhom_10.model;

import java.util.HashMap;
import java.util.Map;

public class LoaiMon {
    String tenLoaiMon;
    String maLoaiMon;

    public LoaiMon() {

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

    public LoaiMon(String tenLoaiMon) {
        this.tenLoaiMon = tenLoaiMon;
    }

    public void setMaLoaiMon(String maLoaiMon) {
        this.maLoaiMon = maLoaiMon;
    }

    public Map<String, Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("tenLoaiMon",tenLoaiMon);
        return result;
    }
}
