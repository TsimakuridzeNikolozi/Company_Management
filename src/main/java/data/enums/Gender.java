package data.enums;

public enum Gender {
    MALE,
    FEMALE;


    /**
     * Parses a string and returns a corresponding gender, or throws an exception if the
     * provided string is not a valid gender
     * @param name String
     * @return Gender enum
     */
    public static Gender parseString(String name) {
        if (name == null) return null;
        if (name.equals("MALE") || name.equals("FEMALE")) return Gender.valueOf(name);
        throw new IllegalArgumentException("Invalid gender, it can only be male or female");
    }
}
