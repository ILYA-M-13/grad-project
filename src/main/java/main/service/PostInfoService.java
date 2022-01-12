package main.service;

import main.api.response.postsResponse.PostsInfoResponse;
import main.api.response.postsResponse.PostsResponseDTO;
import main.api.response.postsResponse.PostsUserResponse;
import main.enumerated.ModePosts;
import main.model.Post;
import main.repository.PostInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class PostInfoService {

    PostInfoRepository postInfoRepository;

    @Autowired
    public PostInfoService(PostInfoRepository postInfoRepository) {
        this.postInfoRepository = postInfoRepository;

    }

    public PostsResponseDTO getAllPosts(int offset, int limit, ModePosts modePosts) {
        Collection<Post> collection = postInfoRepository.postCount();
        int postCount = collection.size();

        Pageable pageable = PageRequest.of((offset == 0) ? offset : offset / limit, limit);

        Page<Post> infoRepositoryAll = postInfoRepository.findAllOrderByTimeDesc(pageable);

        switch (modePosts) {

            case early:
                infoRepositoryAll = postInfoRepository.findALLOrderByTimeAsc(pageable);
                break;
            case popular:
                infoRepositoryAll = postInfoRepository.findALLOrderByPostCommentsDesc(pageable);
                break;
            case best:
                infoRepositoryAll = postInfoRepository.findALLOrderByPostVotes(pageable);
                break;
        }

        List<Post> posts = new ArrayList<>();
        infoRepositoryAll.forEach(a -> posts.add(a));

        List<PostsInfoResponse> list = new ArrayList<>();

        for (Post p : posts) {

            PostsInfoResponse postsInfoResponse = new PostsInfoResponse(
                    p.getId(),
                    (p.getTime().getTime()) / 1000,
                    new PostsUserResponse(p.getUser().getId(), p.getUser().getName()),
                    p.getTitle(),
                    formatText(p.getText()),
                    p.getPostVotes().size(),
                    p.getPostVotes().size(),
                    p.getPostComments().size(),
                    p.getViewCount());
            list.add(postsInfoResponse);

        }
        PostsResponseDTO postsResponse = new PostsResponseDTO(postCount, list);

        return postsResponse;
    }

    private String formatText(String text) {
        text = text.replaceAll("\\<.*?>", "");
        if (text.length() > 150) {
            text = text.substring(0, 150) + "...";
        }
        return text;
    }
}
