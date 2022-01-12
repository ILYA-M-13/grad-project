package main.repository;

import main.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface TagsRepository extends JpaRepository<Tag, Integer> {

    @Query("SELECT t FROM Tag t where t.name like CONCAT('%',:tag,'%')")
    Collection<Tag> findTagsByName(@Param("tag") String tag);

}
