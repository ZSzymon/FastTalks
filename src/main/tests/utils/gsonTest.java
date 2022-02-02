package utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Stream;

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

    @Test
    void testCollectionToJson(){
        //GIVEN
        String email1 = "szymon@test.pl";
        String email2 = "second@test.pl";
        Message message1 = new Message(email1,email2, "HelloWorld");
        Message message2 = new Message(email2, email1, "Hi there.");
        Set<Message> messages = new HashSet<>();
        messages.add(message1);
        messages.add(message2);
        //When
        Type type = new TypeToken<HashSet<Message>>(){}.getType();
        String json = Message.collectionToJson(messages);
        //Then NO CRASH. :)
        System.out.println(json);
    }
    @Test
    void jsonToCollection(){
        //GIVEN
        String email1 = "szymon@test.pl";
        String email2 = "second@test.pl";
        Message message1 = new Message(email1,email2, "HelloWorld");
        Message message2 = new Message(email2, email1, "Hi there.");
        Set<Message> messages = new HashSet<>();
        messages.add(message1);
        messages.add(message2);
        String json = Message.collectionToJson(messages);
        Type type = new TypeToken<HashSet<Message>>(){}.getType();

        //WHEN
        Set<Message> messagesRestored = (Set<Message>) Message.fromJson(json, type);

        messagesRestored.forEach(System.out::println);

        assertStreamEquals(messages.stream(), messagesRestored.stream());
    }
    static void assertStreamEquals(Stream<?> s1, Stream<?> s2) {
        Iterator<?> it2 = s2.iterator();
        assert s1.allMatch(o -> it2.hasNext() && Objects.equals(o, it2.next())) && !it2.hasNext();
    }


}
