package ru.otus.sokolovsky.hw8;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class SampleObject {
    public int iNumber;
    public double dNumber;
    public boolean bool;
    public String str;
    public SampleObject obj;

    public int[] arNumber;
    public String[] arString;

    public Object[] array;
    public List<Object> list;
    public Map<String, ?> map;

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            return true;
        }
        if (!(obj instanceof SampleObject)) {
            return false;
        }
        for (Field field : obj.getClass().getFields()) {
            try {

                Object thisValue = field.get(this);
                Object objValue = field.get(obj);
                if (thisValue == null && objValue == null) {
                    continue;
                }
                if (thisValue == null || objValue == null) {
                    return false;
                }
                if (!thisValue.equals(objValue)){
                    return false;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
