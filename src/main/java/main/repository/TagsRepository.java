package main.repository;

import main.api.response.tagsResponse.TagsResponse;
import main.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagsRepository extends JpaRepository<Tag, Integer> {

    @Query("SELECT new main.api.response.tagsResponse.TagsResponse(t.name," +
            "1.0 * (count(p.id) / (SELECT count(p) " +
            "from Post p where p.moderationStatus='ACCEPTED' AND p.isActive='1' and p.time <= now()))" +
            "* ( Select(1 / (count(p.id) / (select count(p.id) " +
            "from Post p where p.moderationStatus='ACCEPTED' AND p.isActive='1' and p.time <= now()))) " +
            "from Tag t JOIN t.posts p where p.moderationStatus='ACCEPTED' AND p.isActive='1' and p.time <= now() AND size(t.posts) " +
            "= (SELECT max(size(t2.posts)) " +
            "from Tag t2 join t2.posts p2 " +
            "where p2.moderationStatus='ACCEPTED' AND p2.isActive='1' and p.time <= now() ) GROUP BY t.name )) " +
            "FROM Tag t " +
            "JOIN t.posts p " +
            "WHERE p.moderationStatus='ACCEPTED' AND p.isActive='1' and p.time <= now() " +
            "GROUP BY t.name")
    List<TagsResponse>findTagWithWeight();

    Optional<Tag>findTagByName(String name);
}
