package main.repository;

import main.model.PostVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostVoteRepository extends JpaRepository<PostVote,Integer> {

    @Query("select pv from PostVote pv " +
            "join pv.user u " +
            "join pv.post p " +
            "where u.id like :userId and p.id like :postId")
    Optional<PostVote>getPostVote(@Param("userId") int userId,@Param("postId") int postId);
}
