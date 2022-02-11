package main.controller;

import lombok.RequiredArgsConstructor;
import main.api.response.CalendarResponse;
import main.api.response.InitResponseDTO;
import main.api.response.SettingsResponse;
import main.api.response.tagsResponse.TagsResponseDTO;
import main.service.CalendarService;
import main.service.SettingsService;
import main.service.TagsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiGeneralController {

    private final InitResponseDTO initResponse;
    private final SettingsService settingsService;
    private final TagsService tagsService;
    private final CalendarService calendarService;

    @GetMapping("/init")
    private InitResponseDTO init() {
        return initResponse;
    }

    @GetMapping("/settings")
    private ResponseEntity<SettingsResponse> settings() {
        return ResponseEntity.ok(settingsService.getGlobalSettings());
    }

    @GetMapping("/tag")
    private ResponseEntity<TagsResponseDTO> tags(@RequestParam(value = "query", required = false) String query) {
        return ResponseEntity.ok(tagsService.getTags(query));
    }

    @GetMapping("/calendar")
    private ResponseEntity<CalendarResponse> calendar(@RequestParam(value = "year", required = false) Set<Integer> years) {
        return ResponseEntity.ok(calendarService.getPostsInCalendar(years));
    }
}
