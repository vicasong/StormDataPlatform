package me.vica.tools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.vica.po.AccessLog;

import java.util.Date;

/**
 * The Json Format Parser For AccessLog
 * Created by Vica-tony on 10/31/2016.
 */
public class LogParser {

    // Create Gson By Specify The Custom Date Adapter
    private static final Gson gson = new GsonBuilder().
            registerTypeAdapter(Date.class,new DateTimeAdapter())
            .setDateFormat("d/MMM/yy:HH:mm:ss Z").create();

    /**
     * Parse Json String To AccessLog Object
     * @param json The AccessLog Json String
     * @return The AccessLog Object
     */
    public static AccessLog parse(String json){
        AccessLog entity = gson.fromJson(json, AccessLog.class);
        String[] fields = entity.getRequest().split(" ");
        if(fields.length==3){
            // correct format
            entity.setRequest_method(fields[0].toUpperCase());
            entity.setRequest_url(fields[1]);
            entity.setRequest_version(fields[2]);
        }else{
            // dirt data, drop it
            return null;
        }
        return entity;
    }

}
