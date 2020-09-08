package com.touchbiz.common.utils.tools;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Date;

// /Date(1562947200000+0800)/
public class DateStringFormatTypeAdapter extends TypeAdapter<Date> {

    @Override
    public void write(JsonWriter out, Date value) throws IOException {
        if(value != null) {
            out.value(value.getTime());
        }else{
            out.nullValue();
        }
    }

    @Override
    public Date read(JsonReader in) throws IOException {
        String str = in.nextString();
        str = str.replaceAll("/","").replaceAll("Date\\(","").replaceAll("\\)","");
        String[] strs = str.split("\\+");
        long time = Long.parseLong(strs[0]);
        return new Date(time);
    }
}