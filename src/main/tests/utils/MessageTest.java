package utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageTest {

    @Test
    void toJson(){
        Message message = new Message("szymon", "natalia","Hi there.");
        String json = message.toJson();
        System.out.println(json);

    }


    @Test
    void FromJson(){
        //GIVEN
        Message source = new Message("szymon", "natalia","Hi there.");
        String json = source.toJson();
        //WHEN
        Message message = Message.fromJson(json);
        //THEN
        assertEquals(message, source);


    }
}