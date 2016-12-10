package Service.Protocol.Link;

import Service.Protocol.Abstract.Coder;

/**
 * Created by Александр on 15.10.2016.
 */
public class StringCoder implements Coder {

    public String Encode(Object message)    {
        if(!(message instanceof Service.Protocol.Link.Message))
            return "";

        Message linkMessage = (Service.Protocol.Link.Message) message;
        String result = "#" + linkMessage.GetSeq();
        if(!linkMessage.GetConfirmedFrames().isEmpty()) {
            result += " CONF ";
            for(Short confirmedFrame : linkMessage.GetConfirmedFrames())
                result += confirmedFrame;
        }
        if(!linkMessage.GetLostedFrames().isEmpty()) {
            result += " LOST ";
            for(Short lostedFrame : linkMessage.GetLostedFrames())
                result += lostedFrame;
        }
        if(!linkMessage.GetData().isEmpty())
            result += " [" + linkMessage.GetData() + "]";
        return result;
    }

    public Object Decode(String buffer) {
        return new Service.Protocol.Link.Message();
    }
}
