package main.service;

import lombok.AllArgsConstructor;
import main.api.response.SettingsResponseDTO;
import main.repository.SettingsRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class SettingsService {
    SettingsRepository settingsRepository;

    public SettingsResponseDTO getGlobalSettings() {

        return new SettingsResponseDTO(
                Objects.equals(settingsRepository.findGlobalSettingByName("Многопользовательский режим")
                        .get().getValue().toString(), "YES"),
                Objects.equals(settingsRepository.findGlobalSettingByName("Премодерация постов")
                        .get().getValue().toString(), "YES"),
                Objects.equals(settingsRepository.findGlobalSettingByName("Показывать всем статистику блога")
                        .get().getValue().toString(), "YES"));
    }
}
