package main.repository;

import main.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface TagsRepository extends JpaRepository<Tag,Integer> {

    @Query(value = "select * from tags " +
            "join tag2post on tags.id = tag2post.tag_id " +
            "join posts on posts.id = tag2post.post_id " +
            "where posts.moderation_status='ACCEPTED' and posts.is_active = 1 and posts.time < current_date() "+
            "group by name",
            countQuery = "select count(*) from tags " +
                    "join tag2post on tags.id = tag2post.tag_id " +
                    "join posts on posts.id = tag2post.post_id " +
                    "where posts.moderation_status='ACCEPTED' and posts.is_active = 1 and posts.time < current_date() group by name",
            nativeQuery = true)
    Collection<Tag> getAllTags();

    @Query(value = "SELECT * FROM dp.tags where name like CONCAT('%',:tag,'%');",
            countQuery = "SELECT count(*) FROM dp.tags where name like CONCAT('%',:tag,'%');",nativeQuery = true)
    Collection<Tag>getTagsByName(@Param("tag")String tag);

    @Query(value = "SELECT count(*) FROM dp.posts " +
            "where time < current_date() and is_active = 1 " +
            "and moderation_status = 'ACCEPTED'",
             nativeQuery = true)
    Optional<Integer> postCount();
}
