package dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
public class GetPostResponse {
    private String postName;
    private UUID postId;
}
