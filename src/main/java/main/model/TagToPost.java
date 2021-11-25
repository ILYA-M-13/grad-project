package main.model;

import javax.persistence.*;

@Entity
@Table(name = "tag2post")
public class TagToPost {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "post_id",nullable = false)
    private int postId;

    @Column(name = "tag_id",nullable = false)
    private int tagId;

}
