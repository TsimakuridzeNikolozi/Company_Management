package data.enums;

public enum Direction {
    IN,
    OUT;

    /**
     * Parses a string and returns a corresponding Direction, or throws an exception if the
     * provided string is not a valid direction
     * @param name String
     * @return Direction enum
     */
    public static Direction parseString(String name) {
        if (name == null) return null;
        if (name.equals("IN") || name.equals("OUT")) return Direction.valueOf(name);
        throw new IllegalArgumentException("Invalid Direction, it can only be In or Out");
    }
}
