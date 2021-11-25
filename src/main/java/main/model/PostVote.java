package main.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "posts_votes")
public class PostVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private User user;

    @Column(name = "post_id",nullable = false)
    private int postId;

    @Column(columnDefinition = "datetime",nullable = false)
    private Date time;

    @Column(columnDefinition = "TINYINT(1)",nullable = false)
    private int value;
}
