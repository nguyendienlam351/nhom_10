package tdc.edu.vn.nhom_10.model;

import java.util.HashMap;
import java.util.Map;

public class Ban {
    String tenBan;
    String maBan;

    public String getMaBan() {
        return maBan;
    }

    public void setMaBan(String maBan) {
        this.maBan = maBan;
    }

    public Ban() {

    }
    public String getTenBan() {
        return tenBan;
    }

    public void setTenBan(String tenBan) {
        this.tenBan = tenBan;
    }

    public Ban(String tenBan) {
        this.tenBan = tenBan;
    }


    public Map<String, Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("tenBan",tenBan);
        return result;
    }
}
