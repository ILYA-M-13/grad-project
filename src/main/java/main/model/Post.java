package main.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;
import main.enumerated.ModerationStatus;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
@Data
@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "TINYINT(1)",name = "is_active",nullable = false)
    private boolean isActive;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ModerationStatus moderationStatus;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private User moderatorUser;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    private List<PostComment> postComments;

    @Column(columnDefinition = "datetime",nullable = false)
    private Date time;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String text;

    @Column(name = "view_count",nullable = false)
    private int viewCount;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "tag2post",
    joinColumns = {@JoinColumn(name = "post_id")},
    inverseJoinColumns = {@JoinColumn(name = "tag_id")})
    private List<Tag> tags;

    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    private List<PostVote> postVotes;


}
