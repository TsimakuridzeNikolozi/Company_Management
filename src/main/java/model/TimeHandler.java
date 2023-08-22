package model;

import data.entity.Person;
import data.entity.PersonDay;
import data.entity.TreeNode;
import dto.payload.PersonDTO;
import dto.payload.PersonDayDTO;
import dto.request.TimeRequest;
import dto.response.TimeResponse;
import service.DataManager;
import service.DateUtils;

import java.time.LocalDate;
import java.util.*;

public class TimeHandler {
    private final DataManager dataManager;
    public TimeHandler(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public TimeResponse handleTime(TimeRequest request) {
        UUID personId = request.getPersonId();
        Date date = request.getDate();

        List<PersonDayDTO> mainPersonTime = findTimeForPerson(Integer.parseInt(request.getNumDays()), personId, date);

        TreeNode root = findNodeForPerson(personId);

        List<TreeNode> branch = getBranchForNode(root);

        HashMap<PersonDTO, List<PersonDayDTO>> map = getBranchTime(Integer.parseInt(request.getNumDays()), branch, date);

        return TimeResponse.builder()
                .map(map)
                .person(getPersonDTO(dataManager.load(Person.class).id(personId).getSingleResult()))
                .personDayList(mainPersonTime)
                .build();
    }

    private HashMap<PersonDTO,List<PersonDayDTO>> getBranchTime(int numDays, List<TreeNode> branch, Date startDate) {
        HashMap<PersonDTO, List<PersonDayDTO>> map = new HashMap<>();

        for (TreeNode treeNode : branch) {
            map.put(getPersonDTO(treeNode.getPerson()), findTimeForPerson(numDays, treeNode.getPerson().getId(), startDate));
        }
        return map;
    }

    private List<PersonDayDTO> findTimeForPerson(int numDays, UUID personId, Date startDate) {
        List<PersonDayDTO> personTime = new ArrayList<>();
        for (int day = 0; day < numDays; day++) {
            Date curDate = getDateWithDaysBefore(startDate, day);
            PersonDay personDay = dataManager.load(PersonDay.class)
                    .query("SELECT * from person_day " +
                            "WHERE person_id = :personID " +
                            "AND accounting_date = :date")
                    .parameter("personID", personId)
                    .parameter("date", curDate)
                    .getSingleResult();
            personTime.add(getPersonDayDTO(personDay, curDate));
        }
        return personTime;
    }

    private TreeNode findNodeForPerson(UUID personId) {
        return dataManager.load(TreeNode.class)
                .query("SELECT * from tree_node "+
                        "WHERE person_id = :personID")
                .parameter("personID", personId)
                .getSingleResult();
    }

    private List<TreeNode> getBranchForNode(TreeNode root) {
        return dataManager.load(TreeNode.class)
                .query("SELECT * from tree_node "+
                        "WHERE parent_node = :rootNode")
                .parameter("rootNode",root.getId())
                .list();
    }

    private Date getDateWithDaysBefore(Date initialDate, int daysBefore) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(initialDate);
        calendar.add(Calendar.DAY_OF_MONTH, -daysBefore);
        return calendar.getTime();
    }

    private PersonDTO getPersonDTO(Person person) {
        return PersonDTO.builder()
                .personId(person.getId())
                .personName(person.getFirstName() + " " + person.getLastName())
                .build();
    }

    private PersonDayDTO getPersonDayDTO(PersonDay personDay, Date date) {
        if (personDay == null) {
            LocalDate localDate = DateUtils.convertToLocalDate(date, false);
            return PersonDayDTO.builder()
                    .personDayId(UUID.randomUUID())
                    .accountingDate(date)
                    .weekend(DateUtils.isWeekend(localDate))
                    .holiday(DateUtils.isHoliday(localDate))
                    .build();
        }
        Date accountingDate = personDay.getAccountingDate();
        return PersonDayDTO.builder()
                .personDayId(personDay.getId())
                .accountingDate(accountingDate)
                .startTime(DateUtils.adjustDate(accountingDate, personDay.getStartMinutes()))
                .endTime(DateUtils.adjustDate(accountingDate, personDay.getEndMinutes()))
                .weekend(personDay.getWeekend())
                .holiday(personDay.getHoliday())
                .build();
    }
}
