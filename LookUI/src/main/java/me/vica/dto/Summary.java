package me.vica.dto;

/**
 * Created by Vica-tony on 12/9/2016.
 */
public class Summary {
    private int time;
    private int pv;
    private int uv;
    private String resource;
    private String max;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getPv() {
        return pv;
    }

    public void setPv(int pv) {
        this.pv = pv;
    }

    public int getUv() {
        return uv;
    }

    public void setUv(int uv) {
        this.uv = uv;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    @Override
    public String toString() {
        return "Summary{" +
                "time='" + time + '\'' +
                ", pv=" + pv +
                ", uv=" + uv +
                ", resource='" + resource + '\'' +
                ", max='" + max + '\'' +
                '}';
    }
}
