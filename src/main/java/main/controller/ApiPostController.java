package main.controller;


import lombok.RequiredArgsConstructor;
import main.model.ModePosts;
import main.service.PostInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiPostController {

    private final PostInfoService postInfoService;

    @GetMapping("/post")
    private ResponseEntity posts(@RequestParam Integer offset,
                                 @RequestParam Integer limit,
                                 @RequestParam ModePosts mode) {
        return new ResponseEntity<>(postInfoService.getAllPosts(offset, limit, mode), HttpStatus.OK);
    }

    @GetMapping("/post/search")
    private ResponseEntity search(@RequestParam Integer offset,
                                  @RequestParam Integer limit,
                                  @RequestParam(required = false) String query) {
        return new ResponseEntity<>(postInfoService.getPostsByQuery(offset, limit, query), HttpStatus.OK);
    }

    @GetMapping("/post/byDate")
    private ResponseEntity searchByDate(@RequestParam Integer offset,
                                        @RequestParam Integer limit,
                                        @RequestParam String date) {
        return new ResponseEntity<>(postInfoService.getPostsByDate(offset, limit, date), HttpStatus.OK);
    }

    @GetMapping("/post/byTag")
    private ResponseEntity searchByTag(@RequestParam Integer offset,
                                       @RequestParam Integer limit,
                                       @RequestParam String tag) {
        return new ResponseEntity<>(postInfoService.getPostsByTag(offset, limit, tag), HttpStatus.OK);
    }

    @GetMapping("/post/{id}")
    private ResponseEntity searchById(@PathVariable int id) {
        return postInfoService.getPostsById(id) == null ?
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) :
                new ResponseEntity<>(postInfoService.getPostsById(id), HttpStatus.OK);
    }
}
