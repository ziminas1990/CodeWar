package ru.codewar.util;

import ru.codewar.protocol.link.Message;

/**
 * Created by Александр on 15.10.2016.
 */
public class StringCoder implements Coder {

    public String encode(Object message) {
        if (!(message instanceof Message))
            return "";

        Message linkMessage = (Message) message;
        String result = "#" + linkMessage.getSeq();
        if (!linkMessage.getConfirmedFrames().isEmpty()) {
            result += " CONF ";
            for (Short confirmedFrame : linkMessage.getConfirmedFrames())
                result += confirmedFrame;
        }
        if (!linkMessage.getLostedFrames().isEmpty()) {
            result += " LOST ";
            for (Short lostedFrame : linkMessage.getLostedFrames())
                result += lostedFrame;
        }
        if (!linkMessage.getData().isEmpty())
            result += " [" + linkMessage.getData() + "]";
        return result;
    }

    public Object decode(String buffer) {
        return new Message();
    }
}
