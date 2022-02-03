package main.service;

import lombok.AllArgsConstructor;
import main.api.response.postsResponse.PostsCommentResponse;
import main.api.response.postsResponse.PostsInfoResponse;
import main.api.response.postsResponse.PostsResponseDTO;
import main.model.ModePosts;
import main.model.Post;
import main.repository.PostInfoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PostInfoService {
    PostInfoRepository postInfoRepository;
    ConverterService converter;

    public PostsResponseDTO getAllPosts(int offset, int limit, ModePosts modePosts) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
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
        Pageable pageable = PageRequest.of(offset / limit, limit);

            Page<Post> pagePosts = query.matches("")
                    ? postInfoRepository.findAllOrderByTimeDesc(pageable)
                    : postInfoRepository.findAllByQuery(pageable, query);

        List<Post> posts = new ArrayList<>();
        pagePosts.forEach(posts::add);

        List<PostsInfoResponse> list = new ArrayList<>();

        for (Post p : posts) {
            list.add(converter.convertPostToDTO(p));
        }

        return new PostsResponseDTO(pagePosts.getTotalElements(), list);
    }

    public PostsResponseDTO getPostsByDate(int offset, int limit, String date) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        Page<Post> pagePosts = postInfoRepository.findAllByDate(pageable, date);
        List<Post> posts = new ArrayList<>();
        pagePosts.forEach(posts::add);
        List<PostsInfoResponse> list = new ArrayList<>();

        for (Post p : posts) {
            list.add(converter.convertPostToDTO(p));
        }
        return new PostsResponseDTO(pagePosts.getTotalElements(), list);
    }

    public PostsResponseDTO getPostsByTag(int offset, int limit, String tag) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        Page<Post> pagePosts = postInfoRepository.findAllByTag(pageable, tag);
        List<Post> posts = new ArrayList<>();
        pagePosts.forEach(posts::add);
        List<PostsInfoResponse> list = new ArrayList<>();

        for (Post p : posts) {
            list.add(converter.convertPostToDTO(p));
        }
        return new PostsResponseDTO(pagePosts.getTotalElements(), list);
    }

    public PostsCommentResponse getPostsById(int id) {
        /**todo: При успешном запросе необходимо увеличивать количество просмотров поста на 1 (поле view_count),
         кроме случаев:
         Если модератор авторизован, то не считаем его просмотры вообще
         Если автор авторизован, то не считаем просмотры своих же публикаций**/
        Optional<Post> post = postInfoRepository.findPostById(id);
        return post.map(value -> converter.convertPostToPostCommentResponse(value)).orElse(null);
    }
}
