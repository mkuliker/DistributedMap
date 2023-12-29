package messages;

public abstract class MessageWithError implements Message {
    String error;

    public String getError() {
        return error;
    }
}
