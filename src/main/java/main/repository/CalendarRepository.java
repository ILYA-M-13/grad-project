package main.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class CalendarRepository {
    @Autowired
    EntityManager entityManager;

    public List findPostsInCalendar() {
        return entityManager.createQuery("select YEAR(p.time) as pt " +
                "from Post p " +
                "where p.moderationStatus='ACCEPTED' and p.isActive='1' and p.time<current_date() " +
                "group by pt order by pt asc").getResultList();
    }

    public Map<String, Long> findCountPostsByDateInCalendar(String year) {
        String yearQuery = year==null?"":" and YEAR(p.time) = "+year;
        return entityManager.createQuery("select DATE_FORMAT(p.time,'%Y-%m-%d') as date, count(p.id) as count " +
                "from Post p " +
                "where p.moderationStatus='ACCEPTED' and p.isActive='1' and p.time<current_date()"+yearQuery+"" +
                " group by date", Tuple.class).getResultStream()
                .collect(Collectors.toMap(tuple -> ((String) tuple.get("date")), tuple -> ((Long) tuple.get("count"))));
    }

}
