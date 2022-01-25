package main.repository;

import main.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostInfoRepository extends JpaRepository<Post, Integer> {

    @Query("SELECT p from Post p where p.moderationStatus='ACCEPTED' and p.isActive='1' " +
            "and p.time<current_date() ORDER BY UNIX_TIMESTAMP(p.time) DESC")
    Page<Post> findAllOrderByTimeDesc(Pageable pageable);

    @Query("SELECT p from Post p where p.moderationStatus='ACCEPTED' and p.isActive='1' " +
            "and p.time<current_date() ORDER BY UNIX_TIMESTAMP(p.time) ASC")
    Page<Post> findALLOrderByTimeAsc(Pageable pageable);

    @Query("SELECT p from Post p where p.moderationStatus='ACCEPTED' and p.isActive='1' " +
            "and p.time<current_date() ORDER BY function('size', p.postComments) DESC")
    Page<Post> findALLOrderByPostCommentsDesc(Pageable pageable);

    @Query("SELECT p from Post p where p.moderationStatus='ACCEPTED' and p.isActive='1' " +
            "and p.time<current_date() ORDER BY function('size', p.postVotes) DESC")
    Page<Post> findALLOrderByPostVotes(Pageable pageable);

    @Query("SELECT p from Post p where p.title like CONCAT('%',:query,'%') AND p.moderationStatus='ACCEPTED' " +
            "AND p.isActive='1' and p.time<current_date()")
    Page<Post> findAllByQuery(Pageable pageable,@Param("query") String query);
}
