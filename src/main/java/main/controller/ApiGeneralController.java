package main.controller;

import lombok.RequiredArgsConstructor;
import main.api.request.CommentRequest;
import main.api.request.ModerationRequest;
import main.api.request.SettingsRequest;
import main.api.response.*;
import main.api.response.tagsResponse.TagsResponseDTO;
import main.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Set;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiGeneralController {

    private final InitResponseDTO initResponse;
    private final SettingsService settingsService;
    private final TagsService tagsService;
    private final CalendarService calendarService;
    private final PostInfoService postInfoService;
    private final CommentService commentService;
    private final ImageService imageService;

    @GetMapping("/init")
    public InitResponseDTO init() {
        return initResponse;
    }

    @GetMapping("/settings")
    public ResponseEntity<SettingsResponse> settings() {
        return ResponseEntity.ok(settingsService.getGlobalSettings());
    }

    @GetMapping("/tag")
    public ResponseEntity<TagsResponseDTO> tags(@RequestParam(value = "query", required = false) String query) {
        return ResponseEntity.ok(tagsService.getTags(query));
    }

    @GetMapping("/calendar")
    public ResponseEntity<CalendarResponse> calendar(@RequestParam(value = "year", required = false) Set<Integer> years) {
        return ResponseEntity.ok(calendarService.getPostsInCalendar(years));
    }

    @PreAuthorize("hasAuthority('user:write')")
    @PostMapping("/comment")
    public ResponseEntity<?> comment(@RequestBody CommentRequest commentRequest, Principal principal) {
        if (!commentService.checkRequest(commentRequest).isEmpty()) {
            return ResponseEntity.badRequest().body(commentService.errorResponse(commentService.checkRequest(commentRequest)));
        }
        return ResponseEntity.ok().body(commentService.addComment(commentRequest, principal));
    }

    @PreAuthorize("hasAuthority('user:write')")
    @GetMapping("/statistics/my")
    public ResponseEntity<StatisticsResponse> statisticsMy(Principal principal) {
        return ResponseEntity.ok(postInfoService.getStatisticsMy(principal));
    }

    @GetMapping("/statistics/all")
    public ResponseEntity<StatisticsResponse> statisticsAll(Principal principal) {
        if (postInfoService.checkPermission(principal)) {
            return ResponseEntity.ok(postInfoService.getStatisticsAll());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    @PreAuthorize("hasAuthority('user:moderate')")
    @PutMapping("/settings")
    public ResponseEntity setGlobalSettings(@RequestBody SettingsRequest settingsRequest) {
        settingsService.setGlobalSettings(settingsRequest);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('user:moderate')")
    @PostMapping("/moderation")
    public ResponseEntity<ErrorResponse> getModerationPost(@RequestBody ModerationRequest moderationRequest,
                                                           Principal principal) {
        return ResponseEntity.ok(postInfoService.getModeration(moderationRequest, principal));
    }

    @PreAuthorize("hasAuthority('user:write')")
    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> image(@RequestParam("file") MultipartFile file) throws IOException {
        System.out.println(file.getOriginalFilename());
        if (imageService.checkImage(file)) {
            return ResponseEntity.ok(imageService.loadImage(file));
        } else return ResponseEntity.badRequest().body(imageService.errorLoad());
    }


}
