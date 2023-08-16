package data.enums;

public enum MessageType {
    WARNING,
    PERMIT,
    NOTIFICATION;


    /**
     * Parses a string and returns a corresponding Message type, or throws an exception if the
     * provided string is not a valid Message type
     * @param name String
     * @return MessageType enum
     */
    public static MessageType parseString(String name) {
        if (name == null) return null;
        if (name.equals("WARNING") || name.equals("PERMIT") || name.equals("NOTIFICATION"))
            return MessageType.valueOf(name);
        throw new IllegalArgumentException("Invalid message type, it can only be warning, permit or notification");
    }

}
