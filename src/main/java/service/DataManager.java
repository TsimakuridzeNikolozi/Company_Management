package service;

import data.DataMapper;
import data.entity.*;
import data.enums.Direction;
import data.enums.DocumentType;
import data.enums.Gender;
import data.enums.MessageType;
import properties.DBProperties;

import javax.persistence.criteria.CriteriaBuilder;
import javax.sql.rowset.serial.SerialBlob;
import javax.validation.constraints.NotNull;
import java.io.ByteArrayInputStream;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Date;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class DataManager {
    private Statement statement = null;

    public <T> QueryBuilder<T> load(Class<T> entityClass) {
        if (statement == null) initializeStatement();

        return new QueryBuilder<>(entityClass, statement);
    }

    public void save(BaseEntity entity) {
        try {
            if (statement == null) initializeStatement();
            String table = DataMapper.getMapping(entity.getClass());
            UUID id = entity.getId();
            String query = String.format("SELECT * FROM %s WHERE id = %s", table, "'" + id.toString() + "'");
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) update(entity);
            else insert(entity);
        } catch (SQLException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    private void initializeStatement() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establish a connection to the database
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://" + DBProperties.DB_SERVER,
                    DBProperties.USERNAME,
                    DBProperties.PASSWORD);

            // Use the specified database
            statement = connection.createStatement();
            statement.execute("USE " + DBProperties.SCHEMA_NAME);
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    private void insert(BaseEntity entity) {
        try {
            if (statement == null) {
                initializeStatement();
            }
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("INSERT INTO ");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            switch (entity.getClass().getSimpleName()) {
                case "Person" -> {
                    queryBuilder.append("person ");
                    Person person = (Person) entity;
                    queryBuilder.append("(id, first_name, last_name, id_number, birth_date, gender, salary, hire_date, post_id, phone_number) VALUES (");
                    queryBuilder.append("'").append(person.getId()).append("', ");
                    queryBuilder.append(person.getFirstName() != null ? "'" + person.getFirstName() + "'" : "NULL").append(", ");
                    queryBuilder.append(person.getLastName() != null ? "'" + person.getLastName() + "'" : "NULL").append(", ");
                    queryBuilder.append(person.getIdNumber() != null ? "'" + person.getIdNumber() + "'" : "NULL").append(", ");
                    queryBuilder.append(person.getBirthDate() != null ? "'" + dateFormat.format(person.getBirthDate()) + "'" : "NULL").append(", ");
                    queryBuilder.append(person.getGender() != null ? "'" + person.getGender() + "'" : "NULL").append(", ");
                    queryBuilder.append(person.getSalary() != null ? "'" + person.getSalary() + "'" : "NULL").append(", ");
                    queryBuilder.append(person.getHireDate() != null ? "'" + dateFormat.format(person.getHireDate()) + "'" : "NULL").append(", ");
                    queryBuilder.append(person.getPost() != null && person.getPost().getId() != null ? "'" + person.getPost().getId() + "'" : "NULL").append(", ");
                    queryBuilder.append(person.getPhoneNumber() != null ? "'" + person.getPhoneNumber() + "'" : "NULL");
                    queryBuilder.append(")");
                }
                case "Message" -> {
                    queryBuilder.append("message ");
                    Message message = (Message) entity;
                    queryBuilder.append("(id, subject, text, sender_id, receiver_id, message_type, timestamp) VALUES (");
                    queryBuilder.append("'").append(message.getId()).append("', ");
                    queryBuilder.append(message.getSubject() != null ? "'" + message.getSubject() + "'" : "NULL").append(", ");
                    queryBuilder.append(message.getText() != null ? "'" + message.getText() + "'" : "NULL").append(", ");
                    queryBuilder.append(message.getSender() != null ? "'" + message.getSender().getId() + "'" : "NULL").append(", ");
                    queryBuilder.append(message.getReceiver() != null ? "'" + message.getReceiver().getId() + "'" : "NULL").append(", ");
                    queryBuilder.append(message.getMessageType() != null ? "'" + message.getMessageType() + "'" : "NULL").append(", ");
                    queryBuilder.append("'").append(message.getTimestamp()).append("'");
                    queryBuilder.append(")");
                }
                case "PersonDay" -> {
                    queryBuilder.append("person_day ");
                    PersonDay personDay = (PersonDay) entity;
                    queryBuilder.append("(id, person_id, accounting_date, start_minutes, end_minutes, holiday, weekend) VALUES (");
                    queryBuilder.append("'").append(personDay.getId()).append("', ");
                    queryBuilder.append(personDay.getPerson() != null && personDay.getPerson().getId() != null ? "'" + personDay.getPerson().getId() + "'" : "NULL").append(", ");
                    queryBuilder.append(personDay.getAccountingDate() != null ? "'" + dateFormat.format(personDay.getAccountingDate()) + "'" : "NULL").append(", ");
                    queryBuilder.append(personDay.getStartMinutes() != null ? "'" + personDay.getStartMinutes() + "'" : "NULL").append(", ");
                    queryBuilder.append(personDay.getEndMinutes() != null ? "'" + personDay.getEndMinutes() + "'" : "NULL").append(", ");
                    queryBuilder.append(personDay.getHoliday() != null ? personDay.getHoliday() : "NULL").append(", ");
                    queryBuilder.append(personDay.getWeekend() != null ? personDay.getWeekend() : "NULL");
                    queryBuilder.append(")");
                }
                case "PersonDocument" -> {
                    PreparedStatement ps = statement.getConnection().prepareStatement(
                            "INSERT INTO person_document (id, person_id, document_number, issue_date, expiration_date, document_type, file_content) VALUES(" +
                                    "?, ?, ?, ?, ?, ?, ?)");
                    PersonDocument personDocument = (PersonDocument) entity;
                    ps.setString(1, personDocument.getId().toString());
                    ps.setString(2, personDocument.getPerson().getId().toString());
                    ps.setString(3, personDocument.getDocumentNumber());
                    ps.setDate(4, new java.sql.Date(personDocument.getIssueDate().getTime()));
                    ps.setDate(5, new java.sql.Date(personDocument.getExpirationDate().getTime()));
                    ps.setString(6, String.valueOf(personDocument.getDocumentType()));
                    ps.setBinaryStream(7, new ByteArrayInputStream(personDocument.getFileContent()), personDocument.getFileContent().length);
                    ps.execute();
                }
                case "Post" -> {
                    queryBuilder.append("post ");
                    Post post = (Post) entity;
                    queryBuilder.append("(id, post_name, post_description) VALUES (");
                    queryBuilder.append("'").append(post.getId()).append("', ");
                    queryBuilder.append(post.getPostName() != null ? "'" + post.getPostName() + "'" : "NULL").append(", ");
                    queryBuilder.append(post.getPostDescription() != null ? "'" + post.getPostDescription() + "'" : "NULL");
                    queryBuilder.append(")");
                }
                case "TreeNode" -> {
                    queryBuilder.append("tree_node ");
                    TreeNode treeNode = (TreeNode) entity;
                    queryBuilder.append("(id, person_id, parent_node) VALUES (");
                    queryBuilder.append("'").append(treeNode.getId()).append("', ");
                    queryBuilder.append(treeNode.getPerson() != null ? "'" + treeNode.getPerson().getId() + "'" : "NULL").append(", ");
                    queryBuilder.append(treeNode.getParentNode() != null && treeNode.getParentNode().getId() != null ? "'" + treeNode.getParentNode().getId() + "'" : "NULL");
                    queryBuilder.append(")");
                }
                case "User" -> {
                    queryBuilder.append("user ");
                    User user = (User) entity;
                    queryBuilder.append("(id, username, password, email, person_id) VALUES (");
                    queryBuilder.append("'").append(user.getId()).append("', ");
                    queryBuilder.append(user.getUserName() != null ? "'" + user.getUserName() + "'" : "NULL").append(", ");
                    queryBuilder.append(user.getPassword() != null ? "'" + user.getPassword() + "'" : "NULL").append(", ");
                    queryBuilder.append(user.getEmail() != null ? "'" + user.getEmail() + "'" : "NULL").append(", ");
                    queryBuilder.append(user.getPerson() != null && user.getPerson().getId() != null ? "'" + user.getPerson().getId() + "'" : "NULL");
                    queryBuilder.append(")");
                }
                case "WorkTimeTemplate" -> {
                    queryBuilder.append("work_time_template ");
                    WorkTimeTemplate workTimeTemplate = (WorkTimeTemplate) entity;
                    queryBuilder.append("(id, direction, is_controlled, event_minutes, type_name, person_id) VALUES (");
                    queryBuilder.append("'").append(workTimeTemplate.getId()).append("', ");
                    queryBuilder.append(workTimeTemplate.getDirection() != null ? "'" + workTimeTemplate.getDirection() + "'" : "NULL").append(", ");
                    queryBuilder.append(workTimeTemplate.getIsControlled() != null ? workTimeTemplate.getIsControlled() : "NULL").append(", ");
                    queryBuilder.append(workTimeTemplate.getEventMinutes() != null ? workTimeTemplate.getEventMinutes() : "NULL").append(", ");
                    queryBuilder.append(workTimeTemplate.getTypeName() != null ? "'" + workTimeTemplate.getTypeName() + "'" : "NULL").append(", ");
                    queryBuilder.append(workTimeTemplate.getPerson() != null && workTimeTemplate.getPerson().getId() != null ? "'" + workTimeTemplate.getPerson().getId() + "'" : "NULL");
                    queryBuilder.append(")");
                }
                default -> throw new IllegalArgumentException("Unsupported entity type: " + entity.getClass().getSimpleName());
            }

            if (!entity.getClass().getSimpleName().equals("PersonDocument"))
                statement.executeUpdate(queryBuilder.toString());
        } catch (SQLException e) {
            if (e instanceof SQLIntegrityConstraintViolationException) update(entity);
            else throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    private void update(BaseEntity entity) {
        try {
            if (statement == null) {
                initializeStatement();
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("UPDATE ");
            switch (entity.getClass().getSimpleName()) {
                case "Person" -> {
                    queryBuilder.append("person ");
                    Person person = (Person) entity;
                    queryBuilder.append("SET ");
                    queryBuilder.append("first_name = ").append(person.getFirstName() != null ? "'" + person.getFirstName() + "'" : "NULL").append(", ");
                    queryBuilder.append("last_name = ").append(person.getLastName() != null ? "'" + person.getLastName() + "'" : "NULL").append(", ");
                    queryBuilder.append("id_number = ").append(person.getIdNumber() != null ? "'" + person.getIdNumber() + "'" : "NULL").append(", ");
                    queryBuilder.append("birth_date = ").append(person.getBirthDate() != null ? "'" + dateFormat.format(person.getBirthDate()) + "'" : "NULL").append(", ");
                    queryBuilder.append("gender = ").append(person.getGender() != null ? "'" + person.getGender() + "'" : "NULL").append(", ");
                    queryBuilder.append("salary = ").append(person.getSalary() != null ? "'" + person.getSalary() + "'" : "NULL").append(", ");
                    queryBuilder.append("hire_date = ").append(person.getHireDate() != null ? "'" + dateFormat.format(person.getHireDate()) + "'" : "NULL").append(", ");
                    queryBuilder.append("post_id = ").append(person.getPost() != null && person.getPost().getId() != null ? "'" + person.getPost().getId() + "'" : "NULL").append(", ");
                    queryBuilder.append("phone_number = ").append(person.getPhoneNumber() != null ? "'" + person.getPhoneNumber() + "'" : "NULL");
                }
                case "Message" -> {
                    queryBuilder.append("message ");
                    Message message = (Message) entity;
                    queryBuilder.append("SET ");
                    queryBuilder.append("subject = ").append(message.getSubject() != null ? "'" + message.getSubject() + "'" : "NULL").append(", ");
                    queryBuilder.append("text = ").append(message.getText() != null ? "'" + message.getText() + "'" : "NULL").append(", ");
                    queryBuilder.append("sender_id = ").append(message.getSender() != null ? "'" + message.getSender().getId() + "'" : "NULL").append(", ");
                    queryBuilder.append("receiver_id = ").append(message.getReceiver() != null ? "'" + message.getReceiver().getId() + "'" : "NULL").append(", ");
                    queryBuilder.append("message_type = ").append(message.getMessageType() != null ? "'" + message.getMessageType() + "'" : "NULL").append(", ");
                    queryBuilder.append("timestamp = ").append(message.getTimestamp());
                }
                case "PersonDay" -> {
                    queryBuilder.append("person_day ");
                    PersonDay personDay = (PersonDay) entity;
                    queryBuilder.append("SET ");
                    queryBuilder.append("person_id = ").append(personDay.getPerson() != null && personDay.getPerson().getId() != null ? "'" + personDay.getPerson().getId() + "'" : "NULL").append(", ");
                    queryBuilder.append("accounting_date = ").append(personDay.getAccountingDate() != null ? "'" + dateFormat.format(personDay.getAccountingDate()) + "'" : "NULL").append(", ");
                    queryBuilder.append("start_minutes = ").append(personDay.getStartMinutes() != null ? "'" + personDay.getStartMinutes() + "'" : "NULL").append(", ");
                    queryBuilder.append("end_minutes = ").append(personDay.getEndMinutes() != null ? "'" + personDay.getEndMinutes() + "'" : "NULL").append(", ");
                    queryBuilder.append("holiday = ").append(personDay.getHoliday() != null ? personDay.getHoliday() : "NULL").append(", ");
                    queryBuilder.append("weekend = ").append(personDay.getWeekend() != null ? personDay.getWeekend() : "NULL");
                }
                case "PersonDocument" -> {
                    queryBuilder.append("person_document ");
                    PersonDocument personDocument = (PersonDocument) entity;
                    queryBuilder.append("SET ");
                    queryBuilder.append("person_id = ").append(personDocument.getPerson() != null && personDocument.getPerson().getId() != null ? "'" + personDocument.getPerson().getId() + "'" : "NULL").append(", ");
                    queryBuilder.append("document_number = ").append(personDocument.getDocumentNumber() != null ? "'" + personDocument.getDocumentNumber() + "'" : "NULL").append(", ");
                    queryBuilder.append("issue_date = ").append(personDocument.getIssueDate() != null ? "'" + dateFormat.format(personDocument.getIssueDate()) + "'" : "NULL").append(", ");
                    queryBuilder.append("expiration_date = ").append(personDocument.getExpirationDate() != null ? "'" + dateFormat.format(personDocument.getExpirationDate()) + "'" : "NULL").append(", ");
                    queryBuilder.append("document_type = ").append(personDocument.getDocumentType() != null ? "'" + personDocument.getDocumentType() + "'" : "NULL").append(", ");

                    StringBuilder fileContentBytesToString = new StringBuilder();
                    for(int i = 0; i < personDocument.getFileContent().length; i++) fileContentBytesToString.append(personDocument.getFileContent()[i]);
                    queryBuilder.append("file_content = ").append("'").append(fileContentBytesToString).append("'");
                }
                case "TreeNode" -> {
                    queryBuilder.append("tree_node ");
                    TreeNode treeNode = (TreeNode) entity;
                    queryBuilder.append("SET ");
                    queryBuilder.append("person_id = ").append(treeNode.getPerson() != null ? "'" + treeNode.getPerson().getId() + "'" : "NULL").append(", ");
                    queryBuilder.append("tree_node_id = ").append(treeNode.getParentNode() != null && treeNode.getParentNode().getId() != null ? "'" + treeNode.getParentNode().getId() + "'" : "NULL");
                }
                case "User" -> {
                    queryBuilder.append("user ");
                    User user = (User) entity;
                    queryBuilder.append("SET ");
                    queryBuilder.append("username = ").append(user.getUserName() != null ? "'" + user.getUserName() + "'" : "NULL").append(", ");
                    queryBuilder.append("password = ").append(user.getPassword() != null ? "'" + user.getPassword() + "'" : "NULL").append(", ");
                    queryBuilder.append("email = ").append(user.getEmail() != null ? "'" + user.getEmail() + "'" : "NULL").append(", ");
                    queryBuilder.append("person_id = ").append(user.getPerson() != null && user.getPerson().getId() != null ? "'" + user.getPerson().getId() + "'" : "NULL");
                }
                case "WorkTimeTemplate" -> {
                    queryBuilder.append("work_time_template ");
                    WorkTimeTemplate workTimeTemplate = (WorkTimeTemplate) entity;
                    queryBuilder.append("SET ");
                    queryBuilder.append("direction = ").append(workTimeTemplate.getDirection() != null ? "'" + workTimeTemplate.getDirection() + "'" : "NULL").append(", ");
                    queryBuilder.append("is_controlled = ").append(workTimeTemplate.getIsControlled() != null ? workTimeTemplate.getIsControlled() : "NULL").append(", ");
                    queryBuilder.append("event_minutes = ").append(workTimeTemplate.getEventMinutes() != null ? workTimeTemplate.getEventMinutes() : "NULL").append(", ");
                    queryBuilder.append("type_name = ").append(workTimeTemplate.getTypeName() != null ? "'" + workTimeTemplate.getTypeName() + "'" : "NULL").append(", ");
                    queryBuilder.append("person_id = ").append(workTimeTemplate.getPerson() != null && workTimeTemplate.getPerson().getId() != null ? "'" + workTimeTemplate.getPerson().getId() + "'" : "NULL");
                }
                default ->
                        throw new IllegalArgumentException("Unsupported entity type: " + entity.getClass().getSimpleName());
            }
            queryBuilder.append(" WHERE id = '").append(entity.getId()).append("';");
            statement.executeUpdate(queryBuilder.toString());

        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    public void updatePersonDayApprovalStatus(long personDayId, boolean approved) {

    }


    public static class QueryBuilder<T> {
        private Class<T> entityClass;
        private final Statement statement;
        private ResultSet resultSet = null;
        private String query;
        private final Map<String, String> parameters;

        public QueryBuilder(Class<T> entityClass, Statement statement) {
            this.entityClass = entityClass;
            this.statement = statement;
            this.parameters = new HashMap<>();
        }

        public QueryBuilder<T> query(String query) {
            this.query = query;
            return this;
        }

        public QueryBuilder<T> parameter(@NotNull String name, @NotNull Object object) {
            try {
                String objectAsString = null;
                if (object instanceof Date) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    objectAsString = dateFormat.format((Date) object);
                }

                if (object instanceof UUID) objectAsString = object.toString();
                if (objectAsString == null) {
                    assert object instanceof String;
                    objectAsString = (String) object;
                }
                parameters.put(name, objectAsString);
                return this;
            } catch (Exception e) {
                throw new RuntimeException(e.getLocalizedMessage());
            }
        }


        // TODO: resultSet is lost when generating multiple entities
        public List<T> list() {
            try {
                if (resultSet == null) formResultSet();;

                List<T> result = new ArrayList<>();
                String currentQuery = this.query;
                Class<T> currentEntityClass = this.entityClass;
                int count = 0;

                while (resultSet.next()) {
                    result.add(currentEntityClass.cast(formEntityFromResultSet()));

                    resultSet = statement.executeQuery(currentQuery);
                    count++;
                    int countT = count;
                    while(countT > 0) {
                        countT--;
                        resultSet.next();
                    }
                }
                return result;
            } catch (Exception e) {
                throw new RuntimeException(e.getLocalizedMessage());
            }
        }

        /**
         * Used for executing query when there is no resultSet
         */
        public void execute() {
            try {
                for (String name : parameters.keySet()) {
                    query = query.replaceAll(":" + name, "'" + parameters.get(name) + "'");
                }
                statement.execute(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public T getSingleResult() {
            try {
                if (resultSet == null) formResultSet();
                if (resultSet.next()) return (this.entityClass.cast(formEntityFromResultSet()));
                return null;
            } catch (Exception e) {
                throw new RuntimeException(e.getLocalizedMessage());
            }
        }

        public QueryBuilder<T> id(UUID id) {
            try {
                String table = DataMapper.getMapping(entityClass);
                String query = String.format("SELECT * FROM %s WHERE id = '%s'", table, id);
                resultSet = statement.executeQuery(query);
                return this;
            } catch (Exception e) {
                throw new RuntimeException(e.getLocalizedMessage());
            }
        }

        protected BaseEntity formEntityFromResultSet() {
            try {
                String tableName = resultSet.getMetaData().getTableName(1);
                UUID id = UUID.fromString(resultSet.getString("id"));

                return switch (tableName) {
                    case "person" : {
                        String firstName = resultSet.getString("first_name");
                        String lastName = resultSet.getString("last_name");
                        String idNumber = resultSet.getString("id_number");
                        Date birthDate = resultSet.getDate("birth_date");
                        String gender = resultSet.getString("gender");

                        Person person = Person.builder()
                                .id(id)
                                .firstName(firstName)
                                .lastName(lastName)
                                .idNumber(idNumber)
                                .birthDate(birthDate)
                                .gender(Gender.parseString(gender))
                                .salary(resultSet.getBigDecimal("salary"))
                                .hireDate(resultSet.getDate("hire_date"))
                                .phoneNumber(resultSet.getString("phone_number"))
                                .build();

                        UUID postId = UUID.fromString(resultSet.getString("post_id"));
                        this.entityClass = (Class<T>) Post.class;
                        Post post = (Post) this.id(postId).getSingleResult();
                        person.setPost(post);
                        yield person;
                    }
                    case "message" : {
                        Message message = Message.builder()
                                .id(id)
                                .subject(resultSet.getString("subject"))
                                .text(resultSet.getString("text"))
                                .messageType(MessageType.parseString(resultSet.getString("message_type")))
                                .timestamp(resultSet.getTimestamp("timestamp"))
                                .build();

                        UUID senderId = UUID.fromString(resultSet.getString("sender_id"));
                        UUID receiverId = UUID.fromString(resultSet.getString("receiver_id"));
                        this.entityClass = (Class<T>) User.class;
                        User sender = (User) this.id(senderId).getSingleResult();
                        this.entityClass = (Class<T>) User.class;
                        User receiver = (User) this.id(receiverId).getSingleResult();
                        message.setSender(sender);
                        message.setReceiver(receiver);
                        yield message;
                    }
                    case "person_day" : {
                        PersonDay personDay = PersonDay.builder()
                                .id(id)
                                .accountingDate(resultSet.getDate("accounting_date"))
                                .startMinutes(resultSet.getLong("start_minutes"))
                                .endMinutes(resultSet.getLong("end_minutes"))
                                .holiday(resultSet.getBoolean("holiday"))
                                .weekend(resultSet.getBoolean("weekend"))
                                .build();

                        UUID personId = UUID.fromString(resultSet.getString("person_id"));
                        this.entityClass = (Class<T>) Person.class;
                        Person person = (Person) this.id(personId).getSingleResult();
                        personDay.setPerson(person);
                        yield personDay;
                    }
                    case "person_document" : {
                        PersonDocument personDocument = PersonDocument.builder()
                                .id(id)
                                .documentNumber(resultSet.getString("document_number"))
                                .issueDate(resultSet.getDate("issue_date"))
                                .expirationDate(resultSet.getDate("expiration_date"))
                                .documentType(DocumentType.parseString(resultSet.getString("document_type")))
                                .fileContent((resultSet.getBinaryStream("file_content")).readAllBytes())
                                .build();

                        UUID personId = UUID.fromString(resultSet.getString("person_id"));
                        this.entityClass = (Class<T>) Person.class;
                        Person person = (Person) this.id(personId).getSingleResult();
                        personDocument.setPerson(person);
                        yield personDocument;
                    }
                    case "post" : {
                        yield Post.builder()
                                .id(id)
                                .postName(resultSet.getString("post_name"))
                                .postDescription(resultSet.getString("post_description"))
                                .build();
                    }
                    case "tree_node" : {
                        TreeNode treeNode = TreeNode.builder()
                                .id(id)
                                .build();

                        UUID personId = UUID.fromString(resultSet.getString("person_id"));
                        String parentNodeIdString = resultSet.getString("parent_node");

                        this.entityClass = (Class<T>) Person.class;
                        Person person = (Person) this.id(personId).getSingleResult();
                        treeNode.setPerson(person);

                        if (parentNodeIdString == null) {
                            treeNode.setParentNode(null);
                        } else {
                            this.entityClass = (Class<T>) TreeNode.class;
                            UUID parentNodeId = UUID.fromString(parentNodeIdString);
                            TreeNode parentNode = (TreeNode) this.id(parentNodeId).getSingleResult();
                            treeNode.setParentNode(parentNode);
                        }

                        yield treeNode;
                    }
                    case "user" : {
                        User user = User.builder()
                                .id(id)
                                .userName(resultSet.getString("username"))
                                .password(resultSet.getString("password"))
                                .email(resultSet.getString("email"))
                                .build();

                        UUID personId = UUID.fromString(resultSet.getString("person_id"));
                        this.entityClass = (Class<T>) Person.class;
                        Person person = (Person) this.id(personId).getSingleResult();
                        user.setPerson(person);
                        yield user;
                    }
                    case "work_time_template" : {
                        WorkTimeTemplate workTimeTemplate = WorkTimeTemplate.builder()
                                .id(id)
                                .direction(Direction.parseString(resultSet.getString("direction")))
                                .isControlled(resultSet.getBoolean("is_controlled"))
                                .eventMinutes(resultSet.getLong("event_minutes"))
                                .typeName(resultSet.getString("type_name"))
                                .build();

                        UUID personId = UUID.fromString(resultSet.getString("person_id"));
                        this.entityClass = (Class<T>) Person.class;
                        Person person = (Person) this.id(personId).getSingleResult();
                        workTimeTemplate.setPerson(person);
                        yield workTimeTemplate;
                    }
                    default : throw new IllegalArgumentException("Unsupported table name: " + tableName);
                };
            } catch (Exception e) {
                throw new RuntimeException("Failed to form entity from ResultSet: " + e.getLocalizedMessage(), e);
            }
        }

        private void formResultSet() {
            try {
                for (String name : parameters.keySet()) {
                    query = query.replaceAll(":" + name, "'" + parameters.get(name) + "'");
                }
                resultSet = statement.executeQuery(query);
            } catch (Exception e) {
                throw new RuntimeException(e.getLocalizedMessage());
            }
        }
    }
}
