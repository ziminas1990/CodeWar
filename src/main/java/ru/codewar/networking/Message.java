package ru.codewar.networking;


public class Message {

    public String data;

    public Message(String data) {
        this.data = data;
    }

    public Message addHeader(String header) {
        data = header + " " + data;
        return this;
    }

    public String toString() { return data; }

    @Override // from Object
    public boolean equals(Object other) {
        if(other == this)
            return true;
        if(other == null || getClass() != other.getClass())
            return false;

        return data.equals(((Message)other).data);
    }
}
