package main.controller;


import lombok.RequiredArgsConstructor;
import main.enumerated.ModePosts;
import main.service.PostInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiPostController {

    private final PostInfoService postInfoService;

    @GetMapping("/post")
    private ResponseEntity posts(@RequestParam Integer offset,
                                 @RequestParam Integer limit,
                                 @RequestParam ModePosts mode){
        return new ResponseEntity<>(postInfoService.getAllPosts(offset,limit,mode), HttpStatus.OK);
    }

    @GetMapping("/post/search")
    private ResponseEntity search(@RequestParam Integer offset,
                                 @RequestParam Integer limit,
                                 @RequestParam (required = false)String query){
        return new ResponseEntity<>(postInfoService.getPostsByQuery(offset,limit,query), HttpStatus.OK);
    }
}
