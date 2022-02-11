package main.controller;

import lombok.RequiredArgsConstructor;
import main.api.response.postsResponse.PostsCommentResponse;
import main.api.response.postsResponse.PostsResponseDTO;
import main.model.ModePosts;
import main.model.MyPostStatus;
import main.service.PostInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class ApiPostController {

    private final PostInfoService postInfoService;

    @PreAuthorize("hasAuthority('user:write')")
    @GetMapping("/my")
    private ResponseEntity<PostsResponseDTO> postsMy(@RequestParam Integer offset,
                                                     @RequestParam Integer limit,
                                                     @RequestParam MyPostStatus status,
                                                     Principal principal) {
        return ResponseEntity.ok(postInfoService.getMyPosts(offset, limit, status, principal));
    }

    @GetMapping()
    private ResponseEntity<PostsResponseDTO> posts(@RequestParam Integer offset,
                                                   @RequestParam Integer limit,
                                                   @RequestParam ModePosts mode) {
        return ResponseEntity.ok(postInfoService.getAllPosts(offset, limit, mode));
    }

    @GetMapping("/search")
    private ResponseEntity<PostsResponseDTO> search(@RequestParam Integer offset,
                                                    @RequestParam Integer limit,
                                                    @RequestParam(required = false) String query) {
        return ResponseEntity.ok(postInfoService.getPostsByQuery(offset, limit, query));
    }

    @GetMapping("/byDate")
    private ResponseEntity<PostsResponseDTO> searchByDate(@RequestParam Integer offset,
                                                          @RequestParam Integer limit,
                                                          @RequestParam String date) {
        return ResponseEntity.ok(postInfoService.getPostsByDate(offset, limit, date));
    }

    @GetMapping("/byTag")
    private ResponseEntity<PostsResponseDTO> searchByTag(@RequestParam Integer offset,
                                                         @RequestParam Integer limit,
                                                         @RequestParam String tag) {
        return ResponseEntity.ok(postInfoService.getPostsByTag(offset, limit, tag));
    }

    @GetMapping("/{id}")
    private ResponseEntity<PostsCommentResponse> searchById(@PathVariable int id, Principal principal) {

        return postInfoService.getPostsById(id, principal) == null ?
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) :
                ResponseEntity.ok(postInfoService.getPostsById(id, principal));
    }

}
