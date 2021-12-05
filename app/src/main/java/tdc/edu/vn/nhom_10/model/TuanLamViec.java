package tdc.edu.vn.nhom_10.model;

import java.util.ArrayList;

public class TuanLamViec {
    String maTuanLamViec;
    String tuanLamViec;
    ArrayList<CaLamViec> caLamViec;

    public TuanLamViec(){
        caLamViec = new ArrayList<CaLamViec>();
        caLamViec.add(new CaLamViec("T2"));
        caLamViec.add(new CaLamViec("T3"));
        caLamViec.add(new CaLamViec("T4"));
        caLamViec.add(new CaLamViec("T5"));
        caLamViec.add(new CaLamViec("T6"));
        caLamViec.add(new CaLamViec("T7"));
        caLamViec.add(new CaLamViec("CN"));
    }

    public ArrayList<CaLamViec> getCaLamViec() {
        return caLamViec;
    }

    public void setCaLamViec(ArrayList<CaLamViec> caLamViec) {
        this.caLamViec = caLamViec;
    }

    public String getMaTuanLamViec() {
        return maTuanLamViec;
    }

    public void setMaTuanLamViec(String maTuanLamViec) {
        this.maTuanLamViec = maTuanLamViec;
    }

    public String getTuanLamViec() {
        return tuanLamViec;
    }

    public void setTuanLamViec(String tuanLamViec) {
        this.tuanLamViec = tuanLamViec;
    }
}
