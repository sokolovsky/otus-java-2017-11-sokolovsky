package ru.otus.sokolovsky.hw8;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class JsonWriterTest {


    @Test
    public void gsonUseWithGeneric() {
        Gson instance = new GsonBuilder().create();
        LinkedList<Integer> list = new LinkedList<>(Arrays.asList(1, 2, 4));
        String json = instance.toJson(new Integer[] {1,2,4});
        assertThat(list.toArray(), equalTo(new Integer[] {1,2,4}));

        instance = new GsonBuilder().create();
        list = instance.fromJson(json, new TypeToken<LinkedList<Integer>>(){}.getType());

        assertThat(list.size(), is(3));
        assertThat(list.toArray(), equalTo(new Integer[] {1,2,4}));
    }

    @Test
    public void useJsonSimple() {
        JSONObject obj = new JSONObject();
        obj.put("name", "this is name");
        obj.put("age", 100);

        JSONArray list = new JSONArray();
        list.add("1");
        list.add("2");
        list.add("3");

        obj.put("ints", list);

        assertThat(obj.toJSONString(), containsString("{\"ints\":[\"1\",\"2\",\"3\"],\"name\":\"this is name\",\"age\":100}"));
        obj.put("aa", new Object());
        System.out.println(obj.toJSONString());
    }

    @Test
    public void shouldGeneratePrimitives() {
        String one = JsonWriter.parse(1);
        assertThat(one, is("1"));

        String two = JsonWriter.parse("2");
        assertThat(two, is("2"));
    }

    @Test
    public void shouldParseArray() {
        String res = JsonWriter.parse(new int[]{1,2,3});
        assertThat(res, containsString("[1,2,3]"));
    }
}