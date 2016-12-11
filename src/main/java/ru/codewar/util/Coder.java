package ru.codewar.util;

/**
 * Created by Александр on 15.10.2016.
 */
public interface Coder {
    String encode(Object message);

    Object decode(String buffer);
}
