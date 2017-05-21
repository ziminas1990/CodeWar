package ru.codewar.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArgumentsReader {
    final private static Pattern integerReader =
            Pattern.compile("\\s*(?<NUMBER>[-+]?\\d+)\\s*(?<TAIL>.*)");
    final private static Pattern doubleReader =
            Pattern.compile("\\s*(?<NUMBER>[-+]?\\d+(\\.\\d+)?([eE][-+]?\\d+)?)\\s*(?<TAIL>.*)");

    final private static Pattern arrayContent =
            Pattern.compile("\\{\\s*(?<CONTENT>\\s*\\[.*\\])*\\s*\\}(?<TAIL>.*)");
    final private static Pattern arrayContentReader =
            Pattern.compile("\\[\\s*(?<ELEMENT>(\\{.*\\})|(.*?))\\s*\\]\\s*(?<TAIL>.*)");

    private String arguments;

    public ArgumentsReader(String arguments) {
        this.arguments = arguments;
    }


    public String getTail() { return arguments; }

    public Double readDouble()
    {
        if(arguments == null)
            return null;
        Matcher matcher = doubleReader.matcher(arguments);
        if(!matcher.matches()) {
            return null;
        }
        arguments = matcher.group("TAIL");
        try {
            return Double.valueOf(matcher.group("NUMBER"));
        } catch (Exception ex) {
            return null;
        }
    }

    public Integer readInteger()
    {
        if(arguments == null)
            return null;
        Matcher matcher = integerReader.matcher(arguments);
        if(!matcher.matches()) {
            return null;
        }
        arguments = matcher.group("TAIL");
        try {
            return Integer.valueOf(matcher.group("NUMBER"));
        } catch (Exception ex) {
            return null;
        }
    }

    public ArrayList<String> readArray()
    {
        // "{ [element1] [ element2 ] }" -> "element1", " element2 "
        Matcher matcher = arrayContent.matcher(arguments);
        if(!matcher.matches()) {
            return null;
        }
        arguments = matcher.group("TAIL");

        String arrayContent = matcher.group("CONTENT");
        if(arrayContent == null)
            return new ArrayList<>();

        ArrayList<String> result = new ArrayList<>();
        matcher = arrayContentReader.matcher(arrayContent);
        while(matcher.matches()) {
            arrayContent = matcher.group("TAIL");
            result.add(matcher.group("ELEMENT"));
            matcher = arrayContentReader.matcher(arrayContent);
        }
        return result;
    }

}
