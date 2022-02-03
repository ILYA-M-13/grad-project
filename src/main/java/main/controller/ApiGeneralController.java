package main.controller;

import lombok.RequiredArgsConstructor;
import main.api.response.InitResponseDTO;
import main.service.CalendarService;
import main.service.PostInfoService;
import main.service.SettingsService;
import main.service.TagsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    private ResponseEntity settings() {
        if (settingsService.getGlobalSettings() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return new ResponseEntity<>(settingsService.getGlobalSettings(), HttpStatus.OK);
    }

    @GetMapping("/tag")
    private ResponseEntity tags(@RequestParam(value = "query", required = false) String query) {
        return new ResponseEntity<>(tagsService.getTags(query), HttpStatus.OK);
    }

    @GetMapping("/calendar")
    private ResponseEntity calendar(@RequestParam(value = "year", required = false) String year){
        return new ResponseEntity<>(calendarService.getPostsInCalendar(year), HttpStatus.OK);
    }
}
