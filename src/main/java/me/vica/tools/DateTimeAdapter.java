package me.vica.tools;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * The DateTime Adapter Of Gson For Specified DateTime Format
 * Created by Vica-tony on 10/31/2016.
 */
public class DateTimeAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {
    private static final SimpleDateFormat format = new SimpleDateFormat("d/MMM/yy:HH:mm:ss Z", Locale.ENGLISH);


    public JsonElement serialize(Date date, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(format.format(date));
    }

    public Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        if (!(jsonElement instanceof JsonPrimitive)) {
            throw new JsonParseException("The date should be a string value");
        }
        try {
            return format.parse(jsonElement.getAsString());
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
