package data;

public abstract class DataMapper {
    public static String getMapping(Class<?> clazz) {
        return switch (clazz.getSimpleName()) {
            case "Person" -> "person";
            case "Message" -> "message";
            case "PersonDay" -> "person_day";
            case "PersonDocument" -> "person_document";
            case "Post" -> "post";
            case "TreeNode" -> "tree_node";
            case "User" -> "user";
            case "WorkTimeTemplate" -> "work_time_template";
            default -> "";
        };
    }
}
