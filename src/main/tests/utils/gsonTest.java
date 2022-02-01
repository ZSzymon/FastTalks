package utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class gsonTest {

    @Test
    void testToJson(){
        Gson gson = new Gson();
        Map<String, String> map = new HashMap<>();
        map.put("dupa", "3");
        System.out.println(gson.toJson(map));
    }

    @Test
    void testFromJsonToMap(){
        //GIVEN
        Gson gson = new Gson();
        Map<String, String> exampleMap = new HashMap<>();
        exampleMap.put("dupa", "3");
        Type mapType = new TypeToken<HashMap<String, String>>(){}.getType();
        String json = gson.toJson(exampleMap,mapType);
        //WHEN
        Map<String, String> exampleMapDeserialize  = gson.fromJson(json, mapType);
        //THEN
        assertEquals(exampleMap, exampleMapDeserialize);
    }

}
