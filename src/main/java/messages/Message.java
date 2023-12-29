package messages;

public interface Message {
    void handle();

    default String getResult() {
        return null;
    }
}
