package main.controller;

import main.api.response.InitResponseDTO;
import main.enumerated.ModePosts;
import main.service.AuthCheckService;
import main.service.PostInfoService;
import main.service.SettingsService;
import main.service.TagsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {

    private final InitResponseDTO initResponse;
    private final SettingsService settingsService;
    private final AuthCheckService authCheckService;
    private final PostInfoService postInfoService;
    private  final TagsService tagsService;

    public ApiGeneralController(InitResponseDTO initResponse, SettingsService settingsService, AuthCheckService authCheckService, PostInfoService postInfoService, TagsService tagsService) {
        this.initResponse = initResponse;
        this.settingsService = settingsService;
        this.authCheckService = authCheckService;
        this.postInfoService = postInfoService;
        this.tagsService = tagsService;
    }

    @GetMapping("/init")
    private InitResponseDTO init() {
        return initResponse;
    }

    @GetMapping("/settings")
    private ResponseEntity settings() {
        if (settingsService.getGlobalSettings() == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return new ResponseEntity<>(settingsService.getGlobalSettings(), HttpStatus.OK);
    }

    @GetMapping("/auth/check")
    private ResponseEntity authCheck(){
        return new ResponseEntity<>(authCheckService.getAuthCheckInfo(), HttpStatus.OK);
    }

    @GetMapping("/post")
    private ResponseEntity posts(@RequestParam Integer offset,
                                 @RequestParam Integer limit,
                                 @RequestParam ModePosts mode){
        return new ResponseEntity<>(postInfoService.getAllPosts(offset,limit,mode), HttpStatus.OK);
    }

    @GetMapping("/tag")
    private ResponseEntity tags(@RequestParam(value = "query",required = false) String query){
        return new ResponseEntity<>(tagsService.getTags(query),HttpStatus.OK);
    }

}
