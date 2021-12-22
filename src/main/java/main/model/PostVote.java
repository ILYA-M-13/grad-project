package main.model;

import lombok.Data;


import javax.persistence.*;
import java.util.Date;
@Data
@Entity
@Table(name = "posts_votes")
public class PostVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Post post;

    @Column(columnDefinition = "datetime",nullable = false)
    private Date time;

    @Column(columnDefinition = "TINYINT(1)",nullable = false)
    private int value;


}
