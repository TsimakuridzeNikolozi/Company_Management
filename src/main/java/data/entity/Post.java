package data.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
public class Post extends BaseEntity {
    private String postName;
    private String postDescription;
}
