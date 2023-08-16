package model;

import data.entity.Post;
import dto.response.GetPostResponse;
import service.DataManager;

import java.util.List;
import java.util.stream.Collectors;

public class GetPost {

    private final DataManager dataManager;

    public GetPost(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public List<GetPostResponse> getAllPosts() {
        List<Post> results = dataManager.load(Post.class)
                .query("SELECT * FROM post")
                .list();

        return results.stream().map(post -> GetPostResponse.builder()
                .postName(post.getPostName())
                .postId(post.getId())
                .build()).collect(Collectors.toList());
    }
}
