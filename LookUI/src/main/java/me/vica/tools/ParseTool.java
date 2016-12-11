package me.vica.tools;

/**
 * Created by Vica-tony on 12/9/2016.
 */
public class ParseTool {

    public static int toInt(String source, int other){
        try {
            return Integer.valueOf(source);
        }catch (Exception e){
            return other;
        }
    }
}
