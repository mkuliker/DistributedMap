package messages;

import java.util.Map;
import server.*;

public class PutMessage extends MessageWithError {
    public static final String WRONG_PUT_FORMAT = "Correct format for PUT command is PUT key:value";

    int key;
    String value;

    public PutMessage(String inputString) {
        String[] sSplitted = inputString.split(":");
        if (sSplitted.length != 2){
            error = WRONG_PUT_FORMAT;
            return;
        }
        String[] putKey = sSplitted[0].split(" ");
        if (putKey.length != 2) {
            error = WRONG_PUT_FORMAT;
            return;
        }
        try {
            key = Integer.parseInt(putKey[1]);
            value = sSplitted[1];
        } catch (NumberFormatException e) {
            error = WRONG_PUT_FORMAT;
        }
    }

    @Override
    public void handle() {
        if (error != null){
            return;
        }
        Map<Integer, String> map = TcpServer.map;
        map.put(key, value);
    }
}
