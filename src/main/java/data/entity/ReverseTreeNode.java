package data.entity;

import com.sun.source.tree.Tree;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import service.DataManager;

import java.util.*;

@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
public class ReverseTreeNode {
    private Person person;
    private List<ReverseTreeNode> children;

    private static final DataManager dataManager;

    static {
        dataManager = new DataManager();
    }

    public static ReverseTreeNode getRoot(Person rootPerson) {
        List<Person> childNodes = getChildren(rootPerson);
        List<ReverseTreeNode> RTNChildren = new ArrayList<>();
        if (childNodes.isEmpty()) return new ReverseTreeNode(rootPerson, RTNChildren);

        for(Person child: childNodes) {
            RTNChildren.add(getRoot(child));
        }

        return new ReverseTreeNode(rootPerson, RTNChildren);
    }

    /**
     * @param  parent Person entity
     * @return List of Person entities, employees who work directly under the given person
     */
    public static List<Person> getChildren(Person parent) {
        TreeNode parentTreeNode = dataManager.load(data.entity.TreeNode.class)
                .query("SELECT * FROM tree_node t" +
                        "   WHERE t.person_id = :parentId")
                .parameter("parentId", parent.getId())
                .getSingleResult();


        if (parentTreeNode == null) return new ArrayList<>();
        UUID parentTreeNodeId = parentTreeNode.getId();

        return dataManager.load(Person.class)
                .query("SELECT * " +
                        "FROM person p " +
                        "WHERE p.id IN (SELECT t.person_id " +
                                        "FROM tree_node t " +
                                        "WHERE t.parent_node = :parentTreeNodeId)")
                .parameter("parentTreeNodeId", parentTreeNodeId)
                .list();
    }

    /**
     * Method to return all the people who don't have a parent node and their children, used to create a tree
     * @return List of root people and their children, essentially a tree
     */
    public static List<ReverseTreeNode> getRootPeople() {
        List<TreeNode> rootNodes = dataManager.load(TreeNode.class)
                .query("SELECT * FROM tree_node " +
                        "WHERE parent_node IS NULL")
                .list();

        List<ReverseTreeNode> rootPeople = new ArrayList<>();
        for(TreeNode rootNode: rootNodes) {
            rootPeople.add(getRoot(rootNode.getPerson()));
        }
        return rootPeople;
    }
}
