package messages;

public class DefaultMessage implements Message {
    public static final String WRONG_COMMAND = "Unsupported operation";

    public DefaultMessage(String value) {
    }

    @Override
    public String getResult() {
        return WRONG_COMMAND;
    }
    @Override
    public void handle() {
    }
}
