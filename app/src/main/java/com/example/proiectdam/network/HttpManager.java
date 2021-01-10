package com.example.proiectdam.network;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;

//conexiune la url
public class HttpManager implements Callable<String> {
    //"https://jsonkeeper.com/b/DHQ3"
    //verifica daca exista si o conexiune
    private URL url;
    private HttpURLConnection connection;

    //intoarce totul sub forma de string
    //informatia pe care dorim sa o citim
    private InputStream inputStream;
    //subdiviziune a lui inputStream
    private InputStreamReader inputStreamReader;
    //buffer - o zona de memorie din aplicatie, ca o zona tampon
    //lucreaza informatia bucatica cu bucatica
    private BufferedReader bufferedReader;

    private final String urlAddress;

    public HttpManager(String urlAddress) {
        this.urlAddress = urlAddress;
    }

    @Override
    public String call() {
        try {
            return getContentFromHttp();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return null;
    }

    private void closeConnection() {
        //inchidem fiecare clasa utilitara -> daca pot duce la memory leak daca nu sunt inchise
        //de la ultima initializata la prima
        try {
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            inputStreamReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connection.disconnect();
    }

    private String getContentFromHttp() throws IOException {
        url = new URL(urlAddress);
        connection = (HttpURLConnection) url.openConnection();
        inputStream = connection.getInputStream();
        inputStreamReader = new InputStreamReader(inputStream);
        bufferedReader = new BufferedReader(inputStreamReader);
        Log.i("buffer",String.valueOf(bufferedReader));
        //concatenez cat mai multe linii
        StringBuilder result = new StringBuilder();
        String line;
        while((line = bufferedReader.readLine()) != null) {
            //
            result.append(line);
        }
        return result.toString();
    }
}
