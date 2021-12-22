package main.model;

import lombok.Data;


import javax.persistence.*;
import java.util.Date;
@Data
@Entity
@Table(name = "post_comments")
public class PostComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    //комментарий, на который оставлен этот комментарий
    // (может быть NULL, если комментарий оставлен просто к посту)
    @ManyToOne
    @JoinColumn(name="parent_id", referencedColumnName = "id")
    private PostComment parentComment;


    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Post post;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private User user;

    @Column(columnDefinition = "datetime",nullable = false)
    private Date time;

    @Column(nullable = false)
    private String text;


}
