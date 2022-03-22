package main.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.api.request.ModerationRequest;
import main.api.request.PostRequest;
import main.api.response.ErrorResponse;
import main.api.response.UserResponse;
import main.api.response.postsResponse.PostsCommentResponse;
import main.api.response.postsResponse.PostsInfoResponse;
import main.api.response.postsResponse.PostsResponseDTO;
import main.model.*;
import main.repository.PostInfoRepository;
import main.repository.PostVoteRepository;
import main.repository.SettingsRepository;
import main.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PostInfoServiceTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ObjectMapper objectMapper;
    @Mock
    private ConverterService converter;
    @Mock
    private PostInfoRepository postInfoRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private SettingsRepository settingsRepository;
    @Mock
    PostVoteRepository postVoteRepository;
    @InjectMocks
    PostInfoService postInfoService;
    @Mock
    Principal principal;

    private Page<Post> postPage;
    private int offset = 0;
    private int limit = 10;
    private User user = new User();
    private User moderator = new User();
    private String accepted = "accepted";
    private Post post = new Post();
    private List<Post> posts = new ArrayList<>();
    private PostsInfoResponse postsInfoResponse;
    private PostVote postVote = new PostVote();
    private PostsCommentResponse postsCommentResponse;
    private PostRequest postRequestWithoutError;
    private PostRequest postRequestWithError;

    @BeforeEach
    void setUp() {
        postsInfoResponse = new PostsInfoResponse
                (1, 1000000000, null, "Test", "null", 1, 2, 3, 4);

        postsCommentResponse = new PostsCommentResponse
                (1, 123, true, new UserResponse(1, "Test Test", ""),
                        "Text", "Test", 1, 2, 5, new ArrayList<>(), new ArrayList<>());

        user.setEmail("test@test.ru");
        moderator.setModerator(true);
        moderator.setEmail("test@test.ru");

        post.setTitle("Test");
        post.setUser(user);
        post.setModerationStatus(ModerationStatus.ACCEPTED);
        post.setViewCount(1);
        posts.add(post);
        postPage = new PageImpl(posts);
        postVote.setValue(-1);

        postRequestWithError = new PostRequest();
        postRequestWithError.setTitle("?");
        postRequestWithError.setActive(1);
        postRequestWithError.setText("Next");
        postRequestWithError.setTimestamp(123);
        postRequestWithError.setTags(new HashSet<>());
        postRequestWithoutError = new PostRequest();
        postRequestWithoutError.setTitle("Title");
        postRequestWithoutError.setActive(1);
        postRequestWithoutError.setText("Текст публикации НЕ короткий Текст публикации НЕ короткий Текст публикации НЕ короткий!!!");
        postRequestWithoutError.setTimestamp(123);
        postRequestWithoutError.setTags(new HashSet<>());

    }

    @Test
    void getModerationPosts() {

        Mockito.when(postInfoRepository.findAcceptedPostByModerator(Mockito.any(), Mockito.anyInt())).thenReturn(postPage);
        Mockito.when(postInfoRepository.findNewPostByModerator(Mockito.any())).thenReturn(postPage);
        Mockito.when(userRepository.findByEmail(Mockito.any())).thenReturn(Optional.of(user));
        Mockito.when(converter.convertPostToDTO(Mockito.any())).thenReturn(postsInfoResponse);
        PostsResponseDTO response = postInfoService.getModerationPosts(offset, limit, accepted, principal);
        assertEquals(response.getCount(), 1);
        assertEquals(response.getPosts().get(0).getTitle(), post.getTitle());
    }

    @Test
    void getModerationPostWithModerator() {

        Mockito.when(userRepository.findByEmail(Mockito.any())).thenReturn(Optional.of(moderator));
        Mockito.when(postInfoRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(post));
        ErrorResponse response = postInfoService.getModerationPost(new ModerationRequest(1, "accept"), principal);
        assertEquals(response.isResult(), true);
    }

    @Test
    void getModerationPostWithUser() {

        Mockito.when(userRepository.findByEmail(Mockito.any())).thenReturn(Optional.of(user));
        ErrorResponse response = postInfoService.getModerationPost(new ModerationRequest(1, "accept"), principal);
        assertEquals(response.isResult(), false);
    }

    @Test
    void checkPermissionStatisticIsPublicOff() {

        Mockito.when(userRepository.findByEmail(Mockito.any())).thenReturn(Optional.of(moderator));
        Mockito.when(settingsRepository.findGlobalSettingByName(Mockito.any())).thenReturn("NO");
        boolean response = postInfoService.checkPermission(principal);
        assertEquals(response, true);
    }

    @Test
    void checkPermissionStatisticIsPublicOn() {

        Mockito.when(settingsRepository.findGlobalSettingByName(Mockito.any())).thenReturn("YES");
        boolean response = postInfoService.checkPermission(principal);
        assertEquals(response, true);
    }

    @Test
    void getVote() {
        Mockito.when(postInfoRepository.findActivePostById(Mockito.anyInt())).thenReturn(Optional.of(post));
        Mockito.when(userRepository.findByEmail(Mockito.any())).thenReturn(Optional.of(user));
        Mockito.when(postVoteRepository.getPostVote(Mockito.anyInt(), Mockito.anyInt())).thenReturn(Optional.of(postVote));
        Mockito.when(converter.getPostVote(Mockito.anyInt(), Mockito.any(), Mockito.any(), Mockito.anyInt())).thenReturn(postVote);
        ErrorResponse response = postInfoService.getVote(1, principal, 1);
        assertEquals(response.isResult(), true);
    }

    @Test
    void getAllPosts() {

        Mockito.when(postInfoRepository.findAllOrderByTimeDesc(Mockito.any())).thenReturn(postPage);
        Mockito.when(postInfoRepository.findALLOrderByPostCommentsDesc(Mockito.any())).thenReturn(postPage);
        Mockito.when(converter.convertPostToDTO(Mockito.any())).thenReturn(postsInfoResponse);
        PostsResponseDTO response = postInfoService.getAllPosts(offset, limit, ModePosts.popular);
        assertEquals(response.getCount(), 1);
        assertEquals(response.getPosts().get(0).getTitle(), post.getTitle());
    }

    @Test
    void getPostsByIdWithModerator() {

        Mockito.when(userRepository.findByEmail(Mockito.any())).thenReturn(Optional.of(moderator));
        Mockito.when(postInfoRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(post));
        Mockito.when(converter.convertPostToPostCommentResponse(Mockito.any())).thenReturn(postsCommentResponse);
        PostsCommentResponse response = postInfoService.getPostsById(1, principal);
        verify(converter, times(1)).convertPostToPostCommentResponse(post);

    }

    @Test
    void getPostsByIdWithUser() {

        Mockito.when(userRepository.findByEmail(Mockito.any())).thenReturn(Optional.of(user));
        Mockito.when(postInfoRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(post));
        Mockito.when(postInfoRepository.findActivePostById(Mockito.anyInt())).thenReturn(Optional.of(post));
        Mockito.when(converter.convertPostToPostCommentResponse(Mockito.any())).thenReturn(postsCommentResponse);
        Mockito.when(principal.getName()).thenReturn("1");
        PostsCommentResponse response = postInfoService.getPostsById(1, principal);
        verify(converter, times(1)).convertPostToPostCommentResponse(post);
        assertEquals(post.getViewCount(), 2);
    }

    @Test
    void getNewPostWithError() {

        Mockito.when(userRepository.findByEmail(Mockito.any())).thenReturn(Optional.of(user));
        Mockito.when(settingsRepository.findGlobalSettingByName(Mockito.any())).thenReturn("YES");
        ErrorResponse response = postInfoService.getNewPost(postRequestWithError, principal);
        assertEquals(response.getErrors().get("text"),"Текст публикации слишком короткий");
        assertEquals(response.getErrors().get("title"),"Заголовок не установлен");
    }

    @Test
    void getNewPostWithoutError() {

        Mockito.when(userRepository.findByEmail(Mockito.any())).thenReturn(Optional.of(user));
        Mockito.when(settingsRepository.findGlobalSettingByName(Mockito.any())).thenReturn("YES");
        ErrorResponse response = postInfoService.getNewPost(postRequestWithoutError, principal);
        assertEquals(response.getErrors(),null);
        assertEquals(response.isResult(),true);
    }

    @Test
    void getRefactorPost() {

        Mockito.when(postInfoRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(post));
        Mockito.when(userRepository.findByEmail(Mockito.any())).thenReturn(Optional.of(user));
        Mockito.when(settingsRepository.findGlobalSettingByName(Mockito.any())).thenReturn("YES");
        ErrorResponse response = postInfoService.getRefactorPost(postRequestWithoutError,principal,1);
        assertEquals(post.getModerationStatus(),ModerationStatus.NEW);
        assertEquals(response.isResult(),true);
    }
}
