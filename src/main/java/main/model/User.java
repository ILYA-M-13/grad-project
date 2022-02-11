package main.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "TINYINT(1)",name = "is_moderator",nullable = false)
    private boolean isModerator;

    @Column(name = "reg_time",columnDefinition = "datetime",nullable = false)
    private Date regTime;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String code;//код для восстановления пароля, может быть NULL

    @Column(columnDefinition = "TEXT")
    private String photo;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Post> posts;//написанные посты

    @OneToMany(mappedBy = "moderatorUser",cascade = CascadeType.ALL)
    private List<Post> moderatedPosts;//модерируемые посты

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<PostVote> postVotes;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<PostComment> postComments;

    public Role getRole() {
        return isModerator ? Role.MODERATOR : Role.USER;
    }

}
