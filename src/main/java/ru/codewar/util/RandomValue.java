package ru.codewar.util;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Random;

public class RandomValue {
    private double min;
    private double max;

    public static RandomValue readFromJson(JSONArray param) {
        RandomValue value = new RandomValue();
        if(param.length() != 2)
            return null;
        try {
            value.min = param.getDouble(0);
            value.max = param.getDouble(1);
        } catch (JSONException exception) {
            return null;
        }
        return value;
    }

    public RandomValue() { this(0, 0); }
    public RandomValue(double min, double max) {
        this.min = min;
        this.max = max;
    }

    public double getNextValue(Random random) {
        return min + (max - min) * random.nextDouble();
    }
}