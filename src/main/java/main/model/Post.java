package main.model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "TINYINT(1)",name = "is_active",nullable = false)
    private boolean isActive;

    @Enumerated(EnumType.STRING)
    private ModerationStatus moderationStatus;

    @Column(name = "moderator_id")
    private int moderatorId;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    private List<PostComment>postComments;

    @Column(columnDefinition = "datetime",nullable = false)
    private Date time;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String text;

    @Column(name = "view_count",nullable = false)
    private int viewCount;



}
