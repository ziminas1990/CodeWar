package Service.Protocol.Abstract;

/**
 * Created by Александр on 15.10.2016.
 */
public interface Coder {
    String Encode(Object message);
    Object Decode(String buffer);
}
