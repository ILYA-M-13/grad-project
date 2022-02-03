package main.repository;

import main.model.GlobalSettings;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SettingsRepository extends CrudRepository<GlobalSettings,Integer> {

Optional<GlobalSettings> findGlobalSettingByName(@Param("name")String name);
}
