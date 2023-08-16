package service;
import data.entity.*;
import data.enums.Direction;
import data.enums.DocumentType;
import data.enums.Gender;
import data.enums.MessageType;
import data.entity.Person;
import data.entity.Post;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;


public class DataManagerTest {

    private static DataManager dataManager;

    @BeforeAll()
    public static void setUp() {
        dataManager = new DataManager();
    }

    @Test
    public void testSave() {
        UUID id = UUID.randomUUID();
        String description = "Writing Business Process descriptions";
        Post entity = Post.builder()
                .id(id)
                .postName("BA")
                .postDescription(description)
                .build();

        dataManager.save(entity);

        Post post = dataManager.load(Post.class)
                .query("SELECT * FROM post WHERE id = :id")
                .parameter("id", id)
                .getSingleResult();

        assert post.getPostDescription().equals(description);
    }

    @Test
    public void testSavePerson() {
        String postName = "testForNewSelect";
        Post mock = Post.builder()
                .id(UUID.randomUUID())
                .postName(postName)
                .postDescription(postName)
                .build();

        dataManager.save(mock);

        String firstName = "Anton";
        String lastName = "Peikrishvili";
        String idNumber = "10016710016";
        Person personMock = Person.builder()
                .id(UUID.randomUUID())
                .idNumber(idNumber)
                .firstName(firstName)
                .lastName(lastName)
                .gender(Gender.FEMALE)
                .phoneNumber("555671235")
                .post(mock)
                .build();

        dataManager.save(personMock);

        Person person = dataManager.load(Person.class)
                .query("SELECT * FROM person WHERE first_name = :firstName and id_number = :idNumber")
                .parameter("firstName", firstName)
                .parameter("idNumber", idNumber)
                .getSingleResult();

        assert person.getLastName().equals(lastName) && person.getPost().getPostName().equals(postName);
    }

    @Test
    public void testSaveMessage() {
        String postName = "testForMessage";
        Post mock = Post.builder()
                .id(UUID.randomUUID())
                .postName(postName)
                .postDescription(postName)
                .build();

        dataManager.save(mock);

        String firstName = "ISHow";
        String lastName = "Speed";
        String idNumber = "91777560016";
        Person personMock = Person.builder()
                .id(UUID.randomUUID())
                .idNumber(idNumber)
                .firstName(firstName)
                .lastName(lastName)
                .gender(Gender.MALE)
                .phoneNumber("555671235")
                .post(mock)
                .build();


        dataManager.save(personMock);
        User mockUser = User.builder()
                .userName("testMessage")
                .password("ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad")
                .id(UUID.randomUUID())
                .person(personMock)
                .email("test@yahoo.com")
                .build();

        dataManager.save(mockUser);


        UUID id = UUID.randomUUID();
        Message message = Message.builder()
                .id(id)
                .messageType(MessageType.NOTIFICATION)
                .text("text")
                .permission(false)
                .sender(mockUser)
                .receiver(mockUser)
                .build();

        dataManager.save(message);

        assert dataManager.load(Message.class).id(id).getSingleResult().getReceiver().equals(mockUser);
    }

    @Test
    public void testSavePersonDay() {

        String postName = "Test For Person Day";
        Post mock = Post.builder()
                .id(UUID.randomUUID())
                .postName(postName)
                .postDescription(postName)
                .build();

        dataManager.save(mock);

        String firstName = "Dan";
        String lastName = "Biggar";
        String idNumber = "10016709046";
        Person personMock = Person.builder()
                .id(UUID.randomUUID())
                .idNumber(idNumber)
                .firstName(firstName)
                .lastName(lastName)
                .gender(Gender.FEMALE)
                .phoneNumber("555671235")
                .post(mock)
                .build();

        dataManager.save(personMock);


        UUID id = UUID.randomUUID();
        PersonDay personDay =  PersonDay.builder()
                .person(personMock)
                .accountingDate(Calendar.getInstance().getTime())
                .startMinutes(600L)
                .endMinutes(1200L)
                .weekend(false)
                .holiday(false)
                .id(id)
                .build();

        dataManager.save(personDay);

        assert dataManager.load(PersonDay.class).id(id).getSingleResult().getPerson().equals(personMock);
    }

    @Test
    public void testSavePersonDocument() {

        String postName = "testForNewSelect";
        Post mock = Post.builder()
                .id(UUID.randomUUID())
                .postName(postName)
                .postDescription(postName)
                .build();

        dataManager.save(mock);

        String firstName = "Anton";
        String lastName = "Peikrishvili";
        String idNumber = "10416739446";
        Person personMock = Person.builder()
                .id(UUID.randomUUID())
                .idNumber(idNumber)
                .firstName(firstName)
                .lastName(lastName)
                .gender(Gender.FEMALE)
                .phoneNumber("555671235")
                .post(mock)
                .build();

        dataManager.save(personMock);


        UUID id = UUID.randomUUID();
        PersonDocument personDocument = PersonDocument
                .builder()
                .id(id)
                .documentNumber("011")
                .documentType(DocumentType.ID)
                .person(personMock)
                .build();

        dataManager.save(personDocument);
        assert dataManager.load(PersonDocument.class).id(id).getSingleResult().equals(personDocument);
    }

    @Test
    public void testSelect() {
        String postName = "testForNewSelect";
        Post mock = Post.builder()
                .id(UUID.randomUUID())
                .postName(postName)
                .postDescription(postName)
                .build();

        dataManager.save(mock);

        String firstName = "Marine";
        String lastName = "Avalian";
        String idNumber = "10016510016";
        Person personMock = Person.builder()
                .id(UUID.randomUUID())
                .idNumber(idNumber)
                .firstName(firstName)
                .lastName(lastName)
                .gender(Gender.FEMALE)
                .birthDate(Calendar.getInstance().getTime())
                .hireDate(Calendar.getInstance().getTime())
                .salary(BigDecimal.valueOf(10000))
                .phoneNumber("555671235")
                .post(mock)
                .build();

        dataManager.save(personMock);

        Person person = dataManager.load(Person.class)
                .query("SELECT * FROM person WHERE first_name = :firstName and id_number = :idNumber")
                .parameter("firstName", firstName)
                .parameter("idNumber", idNumber)
                .getSingleResult();

        assert person.getLastName().equals(lastName) && person.getPost().getPostName().equals(postName);
    }

    @Test
    public void testSaveWorkTimeTemplate() {
        String postName = "testForNewSelect";
        Post mock = Post.builder()
                .id(UUID.randomUUID())
                .postName(postName)
                .postDescription(postName)
                .build();

        dataManager.save(mock);

        String firstName = "Marine";
        String lastName = "Avalian";
        String idNumber = "10134510016";
        Person personMock = Person.builder()
                .id(UUID.randomUUID())
                .idNumber(idNumber)
                .firstName(firstName)
                .lastName(lastName)
                .gender(Gender.FEMALE)
                .birthDate(Calendar.getInstance().getTime())
                .hireDate(Calendar.getInstance().getTime())
                .salary(BigDecimal.valueOf(10000))
                .phoneNumber("555671235")
                .post(mock)
                .build();

        dataManager.save(personMock);

        UUID id = UUID.randomUUID();
        WorkTimeTemplate workTimeTemplate = WorkTimeTemplate.builder()
                .id(id)
                .direction(Direction.IN)
                .person(personMock)
                .typeName("nn")
                .build();

        dataManager.save(workTimeTemplate);
        assert dataManager.load(WorkTimeTemplate.class).id(id).getSingleResult().equals(workTimeTemplate);
    }

    public void testUpdate() {
        UUID id = UUID.fromString("e095639c-c75b-4ab2-8d44-0a91aee27b9e");
        String description = "Doing some shit Business analytics";
        dataManager.save(Post.builder()
                        .id(id)
                        .postName("test")
                        .postDescription(description)
                .build());

        Post post = dataManager.load(Post.class)
                .query("SELECT * FROM post WHERE id = :id")
                .parameter("id", id)
                .getSingleResult();

        assert post.getPostDescription().equals(description);
    }

    @Test
    public void testId() {
        UUID id = UUID.randomUUID();
        String testName = "TestId";
        Post mock = Post.builder()
                .id(id)
                .postName(testName)
                .postDescription(testName)
                .build();

        dataManager.save(mock);
        Post post = dataManager.load(Post.class)
                .id(id)
                .getSingleResult();

        assert post.getPostName().equals(testName);
    }

    @Test
    public void testList() {
        Post mock1 = Post.builder()
                .id(UUID.randomUUID())
                .postName("testName1")
                .postDescription("testName1")
                .build();

        Post mock2 = Post.builder()
                .id(UUID.randomUUID())
                .postName("testName2")
                .postDescription("testName2")
                .build();


        dataManager.save(mock1);
        dataManager.save(mock2);
        List<Post> result = dataManager.load(Post.class)
                .query("SELECT * FROM post")
                .list();

        assert result.contains(mock1) && result.contains(mock2);
    }

    @Test
    public void testList1() {
        String postName = "testForNewSelect";
        Post mock = Post.builder()
                .id(UUID.randomUUID())
                .postName(postName)
                .postDescription(postName)
                .build();

        dataManager.save(mock);

        String firstName = "Marine";
        String lastName = "Avalian";
        String idNumber = "10973110916";
        Person personMock = Person.builder()
                .id(UUID.randomUUID())
                .idNumber(idNumber)
                .firstName(firstName)
                .lastName(lastName)
                .gender(Gender.FEMALE)
                .birthDate(Calendar.getInstance().getTime())
                .hireDate(Calendar.getInstance().getTime())
                .salary(BigDecimal.valueOf(10000))
                .phoneNumber("555671235")
                .post(mock)
                .build();

        dataManager.save(personMock);

        TreeNode treeNode = TreeNode.builder()
                .id(UUID.randomUUID())
                .person(personMock)
                .parentNode(null)
                .build();

        TreeNode treeNode1 = TreeNode.builder()
                .id(UUID.randomUUID())
                .person(personMock)
                .parentNode(treeNode)
                .build();


        dataManager.save(treeNode);
        dataManager.save(treeNode1);
        List<TreeNode> result = dataManager.load(TreeNode.class)
                .query("SELECT * FROM tree_node")
                .list();

        assert result.contains(treeNode) && result.contains(treeNode1);
    }

    @Test
    public void testList2() {
        List<Message> result = dataManager.load(Message.class)
                .query("SELECT * FROM message")
                .list();
        assert result.size() == 2;
    }

    @Test
    public void testJoin() {
        UUID mockId = UUID.randomUUID();
        String postName = "testForJoin";
        Post mock = Post.builder()
                .id(mockId)
                .postName(postName)
                .postDescription(postName)
                .build();

        dataManager.save(mock);

        String firstName = "Saba";
        String lastName = "Sazanov";
        String idNumber = "610010016916";
        Person personMock = Person.builder()
                .id(UUID.randomUUID())
                .idNumber(idNumber)
                .firstName(firstName)
                .lastName(lastName)
                .gender(Gender.MALE)
                .birthDate(Calendar.getInstance().getTime())
                .hireDate(Calendar.getInstance().getTime())
                .salary(BigDecimal.valueOf(10000))
                .phoneNumber("555671235")
                .post(mock)
                .build();

        dataManager.save(personMock);

        Post post = dataManager.load(Post.class)
                .query("SELECT * FROM post" +
                        "    JOIN person p on post.id = p.post_id" +
                        "    WHERE p.id_number = :idNumber and p.first_name = :firstName" +
                        "    AND post.post_name = :postName")
                .parameter("idNumber", idNumber)
                .parameter("firstName", firstName)
                .parameter("postName", postName)
                .getSingleResult();

        assert post.getId().equals(mockId);
    }
}