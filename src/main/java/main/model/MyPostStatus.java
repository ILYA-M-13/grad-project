package main.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MyPostStatus {
    inactive("скрытые, ещё не опубликованы"),             //(is_active = 0)
    pending("активные, ожидают утверждения модератором"), //(is_active = 1, moderation_status = NEW)
    declined("отклонённые по итогам модерации"),          //(is_active = 1, moderation_status = DECLINED)
    published("опубликованные по итогам модерации");      //(is_active = 1, moderation_status = ACCEPTED)
    private final String name;


}
