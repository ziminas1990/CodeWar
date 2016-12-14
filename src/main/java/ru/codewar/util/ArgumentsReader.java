package ru.codewar.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Александр on 14.12.2016.
 */
public class ArgumentsReader {
    final private static Pattern integerReader =
            Pattern.compile("\\s*(?<NUMBER>[-+]?[0-9]+)\\s*(?<TAIL>.*)");
    final private static Pattern doubleReader =
            Pattern.compile("\\s*(?<NUMBER>[-+]?[0-9]+(\\.[0-9]+)?([eE][-+]?[0-9]+)?)\\s*(?<TAIL>.*)");

    String arguments;

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
}
