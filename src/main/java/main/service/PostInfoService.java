package main.service;

import main.api.response.postsResponse.PostsInfoResponse;
import main.api.response.postsResponse.PostsResponseDTO;
import main.enumerated.ModePosts;
import main.model.Post;
import main.repository.PostInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostInfoService {

    PostInfoRepository postInfoRepository;
    ConverterService converter;

    @Autowired
    public PostInfoService(PostInfoRepository postInfoRepository, ConverterService converter) {
        this.postInfoRepository = postInfoRepository;
        this.converter = converter;
    }

    public PostsResponseDTO getAllPosts(int offset, int limit, ModePosts modePosts) {
        Pageable pageable = PageRequest.of((offset == 0) ? offset : offset / limit, limit);
        Page<Post> pagePosts = postInfoRepository.findAllOrderByTimeDesc(pageable);

        switch (modePosts) {

            case early:
                pagePosts = postInfoRepository.findALLOrderByTimeAsc(pageable);
                break;
            case popular:
                pagePosts = postInfoRepository.findALLOrderByPostCommentsDesc(pageable);
                break;
            case best:
                pagePosts = postInfoRepository.findALLOrderByPostVotes(pageable);
                break;
        }

        List<Post> posts = new ArrayList<>();
        pagePosts.forEach(posts::add);

        List<PostsInfoResponse> list = new ArrayList<>();

        for (Post p : posts) {
            list.add(converter.convertPostToDTO(p));
        }

        return new PostsResponseDTO(pagePosts.getTotalElements(), list);
    }

    public PostsResponseDTO getPostsByQuery(int offset, int limit, String query) {
        if (query == null || query.matches("\\s+")) {
            return getAllPosts(offset, limit, ModePosts.recent);
        }
        Pageable pageable = PageRequest.of((offset == 0) ? offset : offset / limit, limit);
        Page<Post> pagePosts = postInfoRepository.findAllByQuery(pageable, query);
        List<Post> posts = new ArrayList<>();
        pagePosts.forEach(posts::add);

        List<PostsInfoResponse> list = new ArrayList<>();

        for (Post p : posts) {
            list.add(converter.convertPostToDTO(p));
        }
        return new PostsResponseDTO(pagePosts.getTotalElements(), list);
    }
}
