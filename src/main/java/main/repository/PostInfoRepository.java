package main.repository;

import antlr.collections.List;
import main.enumerated.ModePosts;
import main.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;


@Repository
public interface PostInfoRepository extends JpaRepository<Post, Integer> {

    @Query(value = "SELECT * FROM dp.posts " +
            "where time < current_date() " +
            "and is_active = 1 " +
            "and moderation_status = 'ACCEPTED' order by unix_timestamp(time) desc",
            countQuery = "SELECT count(*) from posts where moderation_status='ACCEPTED' and is_active = 1 and posts.time < current_date()"
            , nativeQuery = true)
    Page<Post> postListFromNew(Pageable pageable);

    @Query(value = "SELECT * FROM dp.posts " +
            "where time < current_date() " +
            "and is_active = 1 " +
            "and moderation_status = 'ACCEPTED' order by unix_timestamp(time) asc",
            countQuery = "SELECT count(*) from posts where moderation_status='ACCEPTED' and is_active = 1 and posts.time < current_date()"
            , nativeQuery = true)
    Page<Post> postListFromOld(Pageable pageable);

    @Query(value = "SELECT *, count(post_comments.post_id) as comment_count " +
            "from posts left join post_comments on posts.id=post_comments.post_id " +
            "where moderation_status='ACCEPTED' and is_active = 1 and posts.time < current_date()" +
            "group by posts.id  order by comment_count desc",
            countQuery = "SELECT count(*) from posts where moderation_status='ACCEPTED' and is_active = 1 and posts.time < current_date()"
            , nativeQuery = true)
    Page<Post> postListFromCountComment(Pageable pageable);


    @Query(value = "SELECT *, count(posts_votes.value) as post_count " +
            "    FROM posts left join posts_votes on posts.id=posts_votes.post_id " +
            "    where moderation_status='ACCEPTED' and is_active = 1 and posts.time < current_date() " +
            "    group by posts.id  order by post_count desc",
            countQuery = "SELECT count(*) from posts where moderation_status='ACCEPTED' and is_active = 1 and posts.time < current_date()"
            , nativeQuery = true)
    Page<Post> postListFromCountVotes(Pageable pageable);

    @Query(value = "SELECT * from posts where moderation_status='ACCEPTED' and is_active = 1 and posts.time < current_date()",
            nativeQuery = true)
    Collection<Post> postCount();
}
