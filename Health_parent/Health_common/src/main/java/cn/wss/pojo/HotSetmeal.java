package cn.wss.pojo;
import java.io.Serializable;

public class HotSetmeal implements Serializable{
    private String name;
    private String setmeal_count;
    private Double proportion;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSetmeal_count() {
        return setmeal_count;
    }

    public void setSetmeal_count(String setmeal_count) {
        this.setmeal_count = setmeal_count;
    }

    public Double getProportion() {
        return proportion;
    }

    public void setProportion(Double proportion) {
        this.proportion = proportion;
    }
}
