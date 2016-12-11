package ru.codewar.protocol.link;


import java.util.Vector;


public class Message {
    private short nSeq = 0x0FFF;
    private Vector<Short> confirmedFrames = new Vector<>();
    private Vector<Short> lostedFrames = new Vector<>();
    private String data;

    public Message() {

    }

    public Message(short nSeq, String sData) {
        this.nSeq = nSeq;
        this.data = sData;
    }

    public short getSeq() {
        return nSeq;
    }

    public Vector<Short> getConfirmedFrames() {
        return confirmedFrames;
    }

    public Vector<Short> getLostedFrames() {
        return lostedFrames;
    }

    public String getData() {
        return data;
    }
}
