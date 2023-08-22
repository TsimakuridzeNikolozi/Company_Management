package service;

import data.entity.TreeNode;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class CycleChecker {

    private final DataManager dataManager = new DataManager();

    public boolean makesCycle(TreeNode root) {
        TreeNode parentNode = root.getParentNode();

        Queue<TreeNode> queue = new LinkedList<>();

        queue.add(root);

        while(!queue.isEmpty()) {
            TreeNode current = queue.poll();

            if (parentNode.getId().equals(current.getId())) return true;

            List<TreeNode> children = getChildrenNodes(current);

            queue.addAll(children);
        }

        return false;
    }

    private List<TreeNode> getChildrenNodes(TreeNode root) {
        List<TreeNode> list = dataManager.load(TreeNode.class)
                .query("SELECT * FROM tree_node " +
                        "WHERE parent_node = :rootID")
                .parameter("rootID",root.getId())
                .list();
        return list;
    }
}
