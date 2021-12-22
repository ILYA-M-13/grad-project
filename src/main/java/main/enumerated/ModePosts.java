package main.enumerated;

public enum ModePosts {

    recent("сортировать по дате публикации"),
    popular("сортировать по убыванию количества комментариев"),
    best("сортировать по убыванию количества лайков"),
    early("сортировать по дате публикации");
    private final String name;

    ModePosts(String name) {
        this.name = name;
    }
}


