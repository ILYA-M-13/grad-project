package main.repository;

import main.model.GlobalSettings;
import main.model.ValuesGlobalSetting;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SettingsRepository extends CrudRepository<GlobalSettings, Integer> {
    @Query("select gs.value from GlobalSettings gs where CAST(gs.code as string) like :name")
    String findGlobalSettingByName(@Param("name") String name);

    @Transactional
    @Modifying
    @Query("update GlobalSettings gs set gs.value = " +
            "case " +
            "when gs.name = 'Многопользовательский режим' then (:multiuserMode) " +
            "when gs.name = 'Премодерация постов' then (:postPremoderation) " +
            "when gs.name = 'Показывать всем статистику блога' then (:statisticsIsPublic) " +
            "end")
    void updateSettings(@Param("multiuserMode") ValuesGlobalSetting.Value multiuserMode,
                        @Param("postPremoderation")ValuesGlobalSetting.Value postPremoderation,
                        @Param("statisticsIsPublic")ValuesGlobalSetting.Value statisticsIsPublic);
}

