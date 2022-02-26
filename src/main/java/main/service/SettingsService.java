package main.service;

import lombok.AllArgsConstructor;
import main.api.request.SettingsRequest;
import main.api.response.SettingsResponse;
import main.model.ValuesGlobalSetting;
import main.repository.SettingsRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SettingsService {
    private final SettingsRepository settingsRepository;
    private final PostInfoService postInfoService;

    public SettingsResponse getGlobalSettings() {
        return new SettingsResponse(
                settingsRepository.findGlobalSettingByName("MULTIUSER_MODE").equals("YES"),
                settingsRepository.findGlobalSettingByName("POST_PREMODERATION").equals("YES"),
                settingsRepository.findGlobalSettingByName("STATISTICS_IS_PUBLIC").equals("YES"));
    }

    public void setGlobalSettings(SettingsRequest sr) {
        ValuesGlobalSetting.Value multiuserMode = sr.isMultiuserMode() ?
                ValuesGlobalSetting.Value.YES : ValuesGlobalSetting.Value.NO;
        ValuesGlobalSetting.Value postPremoderation = sr.isPostPremoderation() ?
                ValuesGlobalSetting.Value.YES : ValuesGlobalSetting.Value.NO;
        ValuesGlobalSetting.Value statisticsIsPublic = sr.isStatisticsIsPublic() ?
                ValuesGlobalSetting.Value.YES : ValuesGlobalSetting.Value.NO;
        settingsRepository.updateSettings(multiuserMode, postPremoderation, statisticsIsPublic);
    }
}
