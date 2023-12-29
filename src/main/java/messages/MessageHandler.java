package messages;

import java.lang.reflect.InvocationTargetException;

public class MessageHandler {
    public static Message handle(String value) {
        String[] splitted = value.split(" ");
        String command = splitted[0];
        try {
            String packageName = MessageHandler.class.getPackageName();
            String className = packageName + "." + command.substring(0, 1).toUpperCase() + command.substring(1).toLowerCase() + "Message";
            Class<?> classObject = Class.forName(className);
            return (Message) classObject.getDeclaredConstructor(String.class).newInstance(value);
        } catch (ClassNotFoundException | NoSuchMethodException
                 | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            return new DefaultMessage(value);
        }

    }
}
