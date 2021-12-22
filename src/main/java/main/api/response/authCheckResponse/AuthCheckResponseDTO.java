package main.api.response.authCheckResponse;

import lombok.Data;

@Data
public class AuthCheckResponseDTO {

    private boolean result;
    private AuthCheckUser user;

}


//{
//        "result": true,
//        "user": {
//            "id": 576,
//            "name": "Дмитрий Петров",
//            "photo": "/avatars/ab/cd/ef/52461.jpg",
//            "email": "petrov@petroff.ru",
//            "moderation": true,
//            "moderationCount": 56,
//            "settings": true
//        }
//        }