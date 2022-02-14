package main.repository;

import main.api.response.CalendarProjection;
import main.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@Repository
public interface PostInfoRepository extends JpaRepository<Post, Integer> {

    String SELECT = " select p from Post p ";
    String WHERE = " where p.moderationStatus='ACCEPTED' and p.isActive='1' and p.time<=current_date() ";

    @Query(SELECT + WHERE + " ORDER BY UNIX_TIMESTAMP(p.time) DESC")
    Page<Post> findAllOrderByTimeDesc(Pageable pageable);

    @Query(SELECT + WHERE + " ORDER BY UNIX_TIMESTAMP(p.time) ASC")
    Page<Post> findALLOrderByTimeAsc(Pageable pageable);

    @Query(SELECT + WHERE + " ORDER BY function('size', p.postComments) DESC")
    Page<Post> findALLOrderByPostCommentsDesc(Pageable pageable);

    @Query(SELECT + " left join p.postVotes pv " + WHERE + " group by p.id " +
            "ORDER BY (sum(case when pv.value = 1 then 1 else 0 end)) DESC")
    Page<Post> findALLOrderByPostVotes(Pageable pageable);

    @Query(SELECT + " join p.user pu " +
            "where pu.email like :email and p.moderationStatus='NEW' and p.isActive='1' and p.time<=current_date() ")
    Page<Post> findMyPendingPost(Pageable pageable,@Param("email") String email);

    @Query(SELECT + " join p.user pu " +
            "where pu.email like :email and p.moderationStatus='ACCEPTED' and p.isActive='0' and p.time<=current_date() ")
    Page<Post> findMyInactivePost(Pageable pageable,@Param("email") String email);

    @Query(SELECT + " join p.user pu " +
            "where pu.email like :email and p.moderationStatus='DECLINED' and p.isActive='1' and p.time<=current_date() ")
    Page<Post> findMyDeclinedPost(Pageable pageable,@Param("email") String email);

    @Query(SELECT + " join p.user pu " +
            "where pu.email like :email and p.moderationStatus='ACCEPTED' and p.isActive='1' and p.time<=current_date() ")
    Page<Post> findMyPublishedPost(Pageable pageable,@Param("email") String email);

    @Query(SELECT + WHERE + " and p.title like CONCAT('%',:query,'%')")
    Page<Post> findAllByQuery(Pageable pageable, @Param("query") String query);

    @Query(SELECT + WHERE + " and DATE_FORMAT(p.time,'%Y-%m-%d') like :date")
    Page<Post> findAllByDate(Pageable pageable, @Param("date") String date);

    @Query(SELECT + " join p.tags t " + WHERE + " and t.name like :tag ")
    Page<Post> findAllByTag(Pageable pageable, @Param("tag") String tag);

    @Query(SELECT + WHERE + " and p.id like :id")
    Optional<Post> findActivePostById(@Param("id") int id);

    @Query("select YEAR(p.time) as pt " +
            "from Post p " + WHERE + " group by pt order by pt asc")
    List findYearInCalendar();

    @Query("select DATE_FORMAT(p.time,'%Y-%m-%d') as date, count(p.id) as count " +
            "from Post p " + WHERE + " and YEAR(p.time) IN (:year) " +
            "group by date")
    List<CalendarProjection> findDateAndCountPostsByDateInCalendar(@Param("year") Set<Integer> year);

}
