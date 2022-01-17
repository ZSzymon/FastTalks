package utils;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public class PrimitiveDateBase {

    String userFile;
    PrimitiveDateBase(){

    }
    public void connect(){

    }

    public void getSession(){

    }

    public void commit(){

    }

    public void reload(){

    }
    public void AddUser(){

    }

    public boolean Exist(String email){
        return false;
    }

    private File getFileFromResource(String fileName) throws URISyntaxException {

        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {

            // failed if files have whitespaces or special characters
            //return new File(resource.getFile());

            return new File(resource.toURI());
        }

    }

}
