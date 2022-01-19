package utils;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;

class PrimitiveDateBaseTest {

    private PrimitiveDateBase primitiveDateBase;
    Path resourcePath;
    @org.junit.jupiter.api.BeforeEach
    void beforeAll() throws URISyntaxException, IOException {
        this.primitiveDateBase = new PrimitiveDateBase("usertests.json");
        this.primitiveDateBase.cleanFile();
        this.primitiveDateBase.connect();
    }
    @Test
    void connect(){
        try{
            this.primitiveDateBase.connect();
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void commit() {
        this.primitiveDateBase.commit();
    }

    @Test
    void reload() {
    }

    @Test
    void exist() {
    }

    @Test
    void addUserReloadExistTrue() throws IOException, URISyntaxException {
        assertTrue(this.primitiveDateBase.addUser("test@gmail.com", "hasdas"));
        assertFalse(this.primitiveDateBase.addUser("test@gmail.com", "hasdas"));
        this.primitiveDateBase.commit();
        this.primitiveDateBase.reload();
        assertTrue(this.primitiveDateBase.exist("test@gmail.com"));
    }
    @Test
    void addUserReloadAddCheckWhatHappen() throws IOException, URISyntaxException {
        assertTrue(this.primitiveDateBase.addUser("test@gmail.com", "hasdas"));
        assertFalse(this.primitiveDateBase.addUser("test@gmail.com", "hasdas"));
        this.primitiveDateBase.commit();
        this.primitiveDateBase.reload();
        assertTrue(this.primitiveDateBase.addUser("test2@gmail.com", "hasdas"));
        assertTrue(this.primitiveDateBase.addUser("test3@gmail.com", "hasdas"));
        this.primitiveDateBase.commit();
        this.primitiveDateBase.reload();
        assertTrue(this.primitiveDateBase.usersLenght() == 3);
    }
    @Test
    void addUser() throws FileNotFoundException, URISyntaxException {
        assertTrue(this.primitiveDateBase.addUser("test@gmail.com", "hasdas"));
        assertTrue(this.primitiveDateBase.exist("test@gmail.com"));
    }
}