package main.service;

import main.api.response.SettingsResponseDTO;
import main.model.GlobalSettings;
import main.repository.SettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SettingsService {
    SettingsRepository settingsRepository;

    @Autowired
    public SettingsService(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;

    }

    public SettingsResponseDTO getGlobalSettings() {

        Iterable<GlobalSettings> settings = settingsRepository.findAll();
        List<GlobalSettings> settingsList = new ArrayList<>();
        settings.forEach(a -> settingsList.add(a));
        SettingsResponseDTO settingsResponse = new SettingsResponseDTO();


        for (GlobalSettings globalSettings : settingsList) {

            switch (globalSettings.getCode()) {
                case MULTIUSER_MODE:
                    settingsResponse.setMultiuserMode(globalSettings.getValue().isValue());
                case POST_PREMODERATION:
                    settingsResponse.setPostPremoderation(globalSettings.getValue().isValue());
                case STATISTICS_IS_PUBLIC:
                    settingsResponse.setStatisticsIsPublic(globalSettings.getValue().isValue());
            }
        }
        return settingsResponse;
    }

}
