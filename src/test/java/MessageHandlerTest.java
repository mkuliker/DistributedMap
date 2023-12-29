import messages.*;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import server.TcpServer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MessageHandlerTest {
    @ParameterizedTest
    @ValueSource(strings = {"put 1:one", "put 1: test string with spaces ", "put 1 : one"})
    public void putMessageTest(String value){
        TcpServer.map.clear();
        String expected = value.split(":")[1];
        Message m = MessageHandler.handle(value);
        m.handle();
        assertNull(((MessageWithError) m).getError());
        assertEquals(1, TcpServer.map.size());
        assertEquals(expected, TcpServer.map.get(1));
        TcpServer.map.clear();
    }
    @Test
    public void getMessageTest(){
        TcpServer.map.put(1,"one");
        Message m = MessageHandler.handle("get 1");
        m.handle();
        assertEquals("one", m.getResult());
        assertNull(((MessageWithError) m).getError());
        TcpServer.map.clear();
    }
    @Test
    public void getUnexistedMessageTest(){
        Message m = MessageHandler.handle("get 4");
        m.handle();
        assertNull(((MessageWithError) m).getError());
        assertEquals(GetMessage.DEFAULT_VALUE, m.getResult());
        assertEquals(0, TcpServer.map.size());
    }

    @ParameterizedTest
    @ValueSource(strings = {"get", "get 1:1"})
    public void getWrongFormatNoKeyMessageTest(String value){
        Message m = MessageHandler.handle(value);
        m.handle();
        assertNull(m.getResult());
        assertEquals(GetMessage.WRONG_GET_FORMAT, ((MessageWithError) m).getError());
        assertEquals(0, TcpServer.map.size());
    }
    @ParameterizedTest
    @ValueSource(strings = {"put", "put 1 one", "put one:one", "put 1"})
    public void putWrongFormatNoKeyMessageTest(String value){
        Message m = MessageHandler.handle(value);
        m.handle();
        assertNull(m.getResult());
        assertEquals(PutMessage.WRONG_PUT_FORMAT, ((MessageWithError) m).getError());
        assertEquals(0, TcpServer.map.size());
    }
}
