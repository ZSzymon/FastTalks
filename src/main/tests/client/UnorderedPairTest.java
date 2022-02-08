package client;

import org.junit.jupiter.api.Test;
import utils.Message;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UnorderedPairTest {

    @Test
    void testCreation(){
        UnorderedPair<String> stringUnorderedPair1 = new UnorderedPair<>("test", "szymon");
        UnorderedPair<String> stringUnorderedPair2 = new UnorderedPair<>("szymon", "test");
    }

    @Test
    void testEquals(){
        UnorderedPair<String> stringUnorderedPair1 = new UnorderedPair<>("test", "szymon");
        UnorderedPair<String> stringUnorderedPair2 = new UnorderedPair<>("szymon", "test");
        assertEquals(stringUnorderedPair1, stringUnorderedPair2);
    }
    @Test
    void testEquals2(){
        UnorderedPair<String> stringUnorderedPair1 = new UnorderedPair<>("test", "szymon");
        UnorderedPair<String> stringUnorderedPair2 = new UnorderedPair<>("szymon", "test");
        assertTrue(stringUnorderedPair1.equals(stringUnorderedPair2));
    }
    @Test
    void testHash(){
        UnorderedPair<String> stringUnorderedPair1 = new UnorderedPair<>("test", "szymon");
        UnorderedPair<String> stringUnorderedPair2 = new UnorderedPair<>("szymon", "test");
        assertEquals(stringUnorderedPair1.hashCode(), stringUnorderedPair2.hashCode());

    }
    @Test
    void testOnMap(){
        UnorderedPair<String> stringUnorderedPair1 = new UnorderedPair<>("test", "szymon");
        UnorderedPair<String> stringUnorderedPair2 = new UnorderedPair<>("szymon", "test");
        Message message = new Message("test", "szymon", "From test to szymon");

        Map<UnorderedPair, Conversation> unorderedPairMap = new HashMap<>();
        unorderedPairMap.put(stringUnorderedPair1, new Conversation(message));

        Conversation conversation = unorderedPairMap.get(stringUnorderedPair2);

        assertNotNull(conversation);


    }

}