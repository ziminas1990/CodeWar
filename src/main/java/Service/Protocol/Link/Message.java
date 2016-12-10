package Service.Protocol.Link;


import java.util.Vector;

/**
 * Created by Александр on 15.10.2016.
 */
public class Message
{
    private short         m_nSeq = 0x0FFF;
    private Vector<Short> m_ConfirmedFrames = new Vector<Short>();
    private Vector<Short> m_LostedFrames = new Vector<Short>();
    private String        m_Data = new String();

    public Message() {
        m_nSeq = 0x0FFF;
    }
    public Message(short nSeq, String sData) {
        m_nSeq = nSeq;
        m_Data = sData;
    }

    public short GetSeq() { return m_nSeq; }
    public Vector<Short> GetConfirmedFrames() { return m_ConfirmedFrames; }
    public Vector<Short> GetLostedFrames() { return m_LostedFrames; }
    public String        GetData() { return m_Data; }
}
