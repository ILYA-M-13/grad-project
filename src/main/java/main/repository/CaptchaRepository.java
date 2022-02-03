package main.repository;

import main.model.CaptchaCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CaptchaRepository extends JpaRepository<CaptchaCode,Integer> {
  @Query("select c from CaptchaCode c where c.time < (current_timestamp() - :hour)")
    List<CaptchaCode> findOld(@Param("hour") long hour);

  Optional<CaptchaCode> findCaptchaCodeBySecretCode(@Param("secretCode") String secretCode);
}
