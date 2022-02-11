package main.repository;

import main.model.GlobalSettings;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingsRepository extends CrudRepository<GlobalSettings, Integer> {
    @Query("select gs.value from GlobalSettings gs where CAST(gs.code as string) like :name")
    String findGlobalSettingByName(@Param("name") String name);
}
