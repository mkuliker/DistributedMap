package messages;

import server.TcpServer;

public class GetMessage extends MessageWithError {
    public static final String WRONG_GET_FORMAT = "Correct format for GET command is GET key";
    public static final String DEFAULT_VALUE = "ERR";

    int key;
    String value;

    public GetMessage(String inputString) {
        try {
            String[] sSplitted = inputString.split(" ");
            if (sSplitted.length != 2){
                error = WRONG_GET_FORMAT;
                return;
            }
            key = Integer.parseInt(sSplitted[1]);
        } catch (NumberFormatException e) {
            error = WRONG_GET_FORMAT;
        }
    }

    @Override
    public String getResult() {
        return value;
    }

    @Override
    public void handle() {
        if (error != null){
            return;
        }
        value = TcpServer.map.getOrDefault(key, DEFAULT_VALUE);
    }
}
