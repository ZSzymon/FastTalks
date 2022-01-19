package server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class PrimitiveDateBase {

    private String userFilePath;
    private File userFile;
    private Gson gson;
    private Map<String, String> db;

    public PrimitiveDateBase(String userFilePath) throws URISyntaxException {
        this.userFilePath = userFilePath;
        this.gson = new Gson();
        this.userFile = getFileFromResource(this.userFilePath);
    }
    public synchronized void cleanFile(){
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(this.userFile));
            gson.toJson(new HashMap<String, String >(), bw);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public synchronized void connect() throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(this.userFile));
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        this.db = gson.fromJson(br, type);
        br.close();
        if(this.db == null){
            this.db = new HashMap<>();
        }
    }
    public void commit()  {
        try{
            FileWriter fileWriter = new FileWriter(this.userFile);
            Map<String, String> toSave = this.db;
            gson.toJson(toSave, fileWriter);
            fileWriter.flush();
            fileWriter.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public synchronized void reload() throws IOException, URISyntaxException {
        connect();
    }
    public synchronized int usersLenght(){
        return this.db.keySet().size();
    }

    public synchronized boolean exist(String email){
        return this.db.containsKey(email);
    }

    public synchronized boolean addUser(String email, String password){
        if(!exist(email)){
            this.db.put(email, password);
            return true;
        }
        return false;
    }

    private synchronized static File getFileFromResource(String fileName) throws URISyntaxException {

        ClassLoader classLoader = PrimitiveDateBase.class.getClassLoader();
        URL resource = classLoader.getResource(fileName);

        if (resource == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return new File(resource.toURI());
        }

    }

}
