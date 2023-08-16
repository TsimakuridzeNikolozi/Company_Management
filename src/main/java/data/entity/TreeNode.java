package data.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
public class TreeNode extends BaseEntity {
    private Person person;
    private TreeNode parentNode;
}
