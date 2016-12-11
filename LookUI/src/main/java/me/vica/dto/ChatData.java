package me.vica.dto;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Vica-tony on 12/9/2016.
 */
public class ChatData {
    private int[] data;
    private String name;
    private String[] others;
    private String[] categories;

    public ChatData(String name, List<Integer> l_data, List<String> l_others, List<String> l_categories){
        setName(name);
        data = new int[l_data.size()];
        for(int i = 0; i<data.length; i++){
            data[i] = l_data.get(i);
        }
        setOthers(l_others.toArray(new String[0]));
        setCategories(l_categories.toArray(new String[0]));
    }

    public ChatData(String name, int[] data, String[] others, String[] categories){
        setName(name);
        setData(data);
        setOthers(others);
        setCategories(categories);
    }

    public int[] getData() {
        return data;
    }

    public void setData(int[] data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getOthers() {
        return others;
    }

    public void setOthers(String[] others) {
        this.others = others;
    }

    public String[] getCategories() {
        return categories;
    }

    public void setCategories(String[] categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "ChatData{" +
                "data=" + Arrays.toString(data) +
                ", name='" + name + '\'' +
                ", others=" + Arrays.toString(others) +
                ", categories=" + Arrays.toString(categories) +
                '}';
    }
}
