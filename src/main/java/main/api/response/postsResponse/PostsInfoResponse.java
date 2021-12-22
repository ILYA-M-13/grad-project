package main.api.response.postsResponse;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
public class PostsInfoResponse {

    private int id;
    private long timestamp;
    private PostsUserResponse user;
    private String title;
    private String announce;
    private int likeCount;
    private int dislikeCount;
    private int commentCount;
    private int viewCount;
}
//{
//        "count": 390,
//        "posts": [
//          {
//          "id": 345,
//          "timestamp": 1592338706,
//          "user":
//            {
//             "id": 88,
//              "name": "Дмитрий Петров"
//             },
//          "title": "Заголовок поста",
//          "announce": "Текст анонса поста без HTML-тэгов",
//          "likeCount": 36,
//          "dislikeCount": 3,
//          "commentCount": 15,
//          "viewCount": 55
//        },
//        {...}
//        ]
//        }