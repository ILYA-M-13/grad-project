package main.enumerated;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ModePosts {

    recent("сортировать по дате публикации"),
    popular("сортировать по убыванию количества комментариев"),
    best("сортировать по убыванию количества лайков"),
    early("сортировать по дате публикации");
    private final String name;


}


