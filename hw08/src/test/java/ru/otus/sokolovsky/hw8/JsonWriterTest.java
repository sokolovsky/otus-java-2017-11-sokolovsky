package ru.otus.sokolovsky.hw8;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

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
    @SuppressWarnings("unchecked")
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

    @Test
    public void viewOfArrayWithSingleString() {
        String res = JsonWriter.parse(new String[]{"tested string"});
        assertThat(res, containsString("[\"tested string\"]"));
    }

    @Test
    public void testPrimitiveTypes() {
        SampleObject object = new SampleObject();
        object.str = "tested string";
        object.iNumber = 22;
        object.dNumber = 22.0;
        object.bool = false;

        String json = JsonWriter.parse(object);
        SampleObject deserializedObject = deserializeSampleObject(json);
        assertThat(deserializedObject, equalTo(deserializedObject));
    }

    @Test
    public void testLinkedType() {
        SampleObject object = new SampleObject();
        object.obj = new SampleObject();
        object.obj.str = "tested string";

        String json = JsonWriter.parse(object);
        SampleObject deserializedObject = deserializeSampleObject(json);
        assertThat(deserializedObject.obj.str, equalTo("tested string"));
    }

    @Test
    public void testLinkedArray() {
        SampleObject object = new SampleObject();

        // integer values unpack in float, gson has the same problem
        object.array = new Double[]{1.0, 2.0, 3.0};

        String json = JsonWriter.parse(object);
        SampleObject deserializedObject = deserializeSampleObject(json);

        assertThat(deserializedObject.array, equalTo(new Double[]{1.0, 2.0, 3.0}));
    }

    @Test
    public void testDeepLinkedList() {
        SampleObject object = new SampleObject();
        object.list = new LinkedList<>();

        SampleObject iFirstObject = new SampleObject();
        iFirstObject.dNumber = 2.0;
        object.list.add(iFirstObject);

        SampleObject iSecondObject = new SampleObject();
        iFirstObject.dNumber = 5.0;
        object.list.add(iSecondObject);

        String json = JsonWriter.parse(object);
    }

    @Test
    public void mapChecking() {
        Map<String, SampleObject> map = new HashMap<>();

        SampleObject first = new SampleObject();
        first.str = "first";

        SampleObject second = new SampleObject();
        second.str = "second";

        map.put("first", first);
        map.put("second", second);

        String json = JsonWriter.parse(map);

        Gson gson = new GsonBuilder().create();
        Map<String, SampleObject> deserializedMap = gson.fromJson(json, new TypeToken<Map<String, SampleObject>>() {}.getType());

        assertThat(deserializedMap.size(), is(2));
        assertThat(deserializedMap.get("first").str, equalTo("first"));
        assertThat(deserializedMap.get("second").str, equalTo("second"));
    }

    @Test
    public void arrayOfObjectsChecking() {
        SampleObject first = new SampleObject();
        first.str = "first";

        SampleObject second = new SampleObject();
        second.str = "second";

        SampleObject[] array = new SampleObject[]{first, second};

        String json = JsonWriter.parse(array);

        Gson gson = new GsonBuilder().create();
        array = gson.fromJson(json, SampleObject[].class);

        assertThat(array.length, is(2));

        assertThat(array[0], equalTo(first));
        assertThat(array[1], equalTo(second));
    }

    private SampleObject deserializeSampleObject(String json) {
        return deserializeObject(json, SampleObject.class);
    }

    private <T> T deserializeObject(String json, Class<T> cl) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, cl);
    }
}