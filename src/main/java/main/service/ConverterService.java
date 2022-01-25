package main.service;

import lombok.AllArgsConstructor;
import main.api.response.postsResponse.PostsInfoResponse;
import main.api.response.postsResponse.PostsUserResponse;
import main.model.Post;
import main.model.User;
import main.repository.PostInfoRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ConverterService {
    static final int MAX_CHAR_IN_POST = 150;
    static final int MILLISEC_IN_SEC = 1000;
    PostInfoRepository postInfoRepository;

public PostsInfoResponse convertPostToDTO(Post post){
    return new PostsInfoResponse(post.getId(),
            post.getTime().getTime() / MILLISEC_IN_SEC,
            convertUserToDto(post.getUser()),
            post.getTitle(),
            formatText(post.getText()),
            post.getPostVotes().size(),
            post.getPostVotes().size(),
            post.getPostComments().size(),
            post.getViewCount());
}

public PostsUserResponse convertUserToDto(User user){
    return new PostsUserResponse(user.getId(), user.getName());

}
    private String formatText(String text) {
        text = text.replaceAll("\\<.*?>", "");
        if (text.length() > MAX_CHAR_IN_POST) {
            text = text.substring(0, MAX_CHAR_IN_POST) + "...";
        }
        return text;
    }
}
