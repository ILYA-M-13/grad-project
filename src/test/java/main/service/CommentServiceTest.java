package main.service;

import main.api.request.CommentRequest;
import main.api.response.commentResponse.IdCommentResponse;
import main.model.Post;
import main.model.PostComment;
import main.model.User;
import main.repository.CommentRepository;
import main.repository.PostInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Principal;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @InjectMocks
    CommentService commentService;
    @Mock
    PostInfoRepository postInfoRepository;
    @Mock
    CommentRepository commentRepository;
    @Mock
    PostInfoService postInfoService;
    @Mock
    Principal principal;

    private CommentRequest incorrectRequest;
    private CommentRequest validRequest;
    private Post post;
    private PostComment postComment;
    private User user;

    @BeforeEach
    void setUp() {
        incorrectRequest = new CommentRequest();
        incorrectRequest.setParentId(1);
        incorrectRequest.setText("qwe");
        incorrectRequest.setPostId(1);

        validRequest = new CommentRequest();
        validRequest.setPostId(1);
        validRequest.setText("qwert");
        validRequest.setParentId(1);

        post = new Post();
        post.setTitle("Test");
        post.setId(1);
        post.setText("Text");

        user = new User();
        user.setEmail("Test@test.ru");
        user.setPassword("123456");
        user.setName("Test");

        postComment = new PostComment();
        postComment.setPost(post);
        postComment.setText("qwert");
        postComment.setTime(new Date());
        postComment.setUser(user);

    }

    @Test
    void checkRequestWithIncorrectRequest() {

        Mockito.when(postInfoRepository.findActivePostById(Mockito.anyInt())).thenReturn(Optional.of(post));
        boolean response = commentService.checkRequest(incorrectRequest);
        assertEquals(response,false);
    }

    @Test
    void checkRequestWithValidRequest() {

        Mockito.when(postInfoRepository.findActivePostById(Mockito.anyInt())).thenReturn(Optional.of(post));
        Mockito.when(commentRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(new PostComment()));
        boolean response = commentService.checkRequest(validRequest);
        assertEquals(response,true);
    }

    @Test
    void checkRequestWithValidRequestAndEmptyPostComment() {

        Mockito.when(postInfoRepository.findActivePostById(Mockito.anyInt())).thenReturn(Optional.of(post));
        Mockito.when(commentRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
        boolean response = commentService.checkRequest(validRequest);
        assertEquals(response,false);
    }

    @Test
    void  addComment() {

        Mockito.when(postInfoService.getAuthUser(Mockito.any())).thenReturn(user);
        Mockito.when(postInfoRepository.findActivePostById(Mockito.anyInt())).thenReturn(Optional.of(post));
        Mockito.when(commentRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
        IdCommentResponse response = commentService.addComment(validRequest,principal);
        verify(commentRepository, times(1)).save(Mockito.any(PostComment.class));
    }
}
