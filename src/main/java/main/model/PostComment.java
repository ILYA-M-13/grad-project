package main.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "post_comments")
public class PostComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "parent_id")
    private int parentId;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Post post;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private User user;

    @Column(columnDefinition = "datetime",nullable = false)
    private Date time;

    @Column(nullable = false)
    private String text;
}
