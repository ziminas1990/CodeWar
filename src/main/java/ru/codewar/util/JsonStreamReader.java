package ru.codewar.util;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JsonStreamReader {

    public static JSONObject read(InputStream stream) {
        if(stream == null)
            return null;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder jsonString = new StringBuilder();

        try {
            String line;
            while ((line = bufferedReader.readLine()) != null)
                jsonString.append(line);
        } catch (IOException exception) {
            return null;
        }

        try {
            return new JSONObject(jsonString.toString());
        } catch (JSONException exception) {
            return null;
        }
    }
}
