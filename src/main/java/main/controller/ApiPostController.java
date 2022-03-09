package main.controller;

import lombok.RequiredArgsConstructor;
import main.api.request.PostRequest;
import main.api.request.PostRequestId;
import main.api.response.ErrorResponse;
import main.api.response.postsResponse.PostsCommentResponse;
import main.api.response.postsResponse.PostsResponseDTO;
import main.model.ModePosts;
import main.model.MyPostStatus;
import main.repository.PostInfoRepository;
import main.service.PostInfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class ApiPostController {

    private final PostInfoService postInfoService;
    private final PostInfoRepository postInfoRepository;
    private final int LIKE_VALUE = 1;
    private final int DISLIKE_VALUE = -1;

    @GetMapping()
    public ResponseEntity<PostsResponseDTO> posts(@RequestParam Integer offset,
                                                  @RequestParam Integer limit,
                                                  @RequestParam ModePosts mode) {
        return ResponseEntity.ok(postInfoService.getAllPosts(offset, limit, mode));
    }

    @GetMapping("/search")
    public ResponseEntity<PostsResponseDTO> search(@RequestParam Integer offset,
                                                   @RequestParam Integer limit,
                                                   @RequestParam(required = false) String query) {
        return ResponseEntity.ok(postInfoService.getPostsByQuery(offset, limit, query));
    }

    @GetMapping("/byDate")
    public ResponseEntity<PostsResponseDTO> searchByDate(@RequestParam Integer offset,
                                                         @RequestParam Integer limit,
                                                         @RequestParam String date) {
        return ResponseEntity.ok(postInfoService.getPostsByDate(offset, limit, date));
    }

    @GetMapping("/byTag")
    public ResponseEntity<PostsResponseDTO> searchByTag(@RequestParam Integer offset,
                                                        @RequestParam Integer limit,
                                                        @RequestParam String tag) {
        return ResponseEntity.ok(postInfoService.getPostsByTag(offset, limit, tag));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostsCommentResponse> searchById(@PathVariable int id, Principal principal) {
        if (principal == null) {
            return postInfoRepository.findActivePostById(id).isPresent() ?
                    ResponseEntity.ok(postInfoService.getPostsById(id)) : ResponseEntity.badRequest().body(null);
        }
        if (!postInfoRepository.findById(id).isPresent()) {
            return ResponseEntity.badRequest().body(null);
        } else return ResponseEntity.ok(postInfoService.getPostsById(id, principal));
    }

    @PreAuthorize("hasAuthority('user:moderate')")
    @GetMapping("/moderation")
    public ResponseEntity<PostsResponseDTO> moderationPosts(@RequestParam Integer offset,
                                                            @RequestParam Integer limit,
                                                            @RequestParam String status,
                                                            Principal principal) {
        return ResponseEntity.ok(postInfoService.getModerationPosts(offset, limit, status, principal));
    }

    @PreAuthorize("hasAuthority('user:write')")
    @GetMapping("/my")
    public ResponseEntity<PostsResponseDTO> postsMy(@RequestParam Integer offset,
                                                    @RequestParam Integer limit,
                                                    @RequestParam MyPostStatus status,
                                                    Principal principal) {
        return ResponseEntity.ok(postInfoService.getMyPosts(offset, limit, status, principal));
    }

    @PreAuthorize("hasAuthority('user:write')")
    @PostMapping()
    public ResponseEntity<ErrorResponse> addPost(@RequestBody PostRequest postRequest, Principal principal) {
        return ResponseEntity.ok(postInfoService.getNewPost(postRequest, principal));
    }

    @PreAuthorize("hasAuthority('user:write')")
    @PutMapping("/{id}")
    public ResponseEntity<?> refactorPost(@RequestBody PostRequest postRequest,
                                          Principal principal,
                                          @PathVariable int id) {
        return postInfoService.checkPost(id, principal)
                ? ResponseEntity.ok(postInfoService.getRefactorPost(postRequest, principal, id))
                : ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasAuthority('user:write')")
    @PostMapping("/like")
    public ResponseEntity<ErrorResponse> like(@RequestBody PostRequestId postRequestId, Principal principal) {
        return ResponseEntity.ok(postInfoService.getVote(postRequestId.getPostId(), principal, LIKE_VALUE));
    }

    @PreAuthorize("hasAuthority('user:write')")
    @PostMapping("/dislike")
    public ResponseEntity<ErrorResponse> dislike(@RequestBody PostRequestId postRequestId, Principal principal) {
        return ResponseEntity.ok(postInfoService.getVote(postRequestId.getPostId(), principal, DISLIKE_VALUE));
    }

}
