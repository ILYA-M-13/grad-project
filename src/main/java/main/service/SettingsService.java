package main.service;

import lombok.AllArgsConstructor;
import main.api.response.SettingsResponse;
import main.repository.SettingsRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SettingsService {
    SettingsRepository settingsRepository;

    public SettingsResponse getGlobalSettings() {
        return new SettingsResponse(
                settingsRepository.findGlobalSettingByName("MULTIUSER_MODE").equals("YES"),
                settingsRepository.findGlobalSettingByName("POST_PREMODERATION").equals("YES"),
                settingsRepository.findGlobalSettingByName("STATISTICS_IS_PUBLIC").equals("YES"));

    }
}
