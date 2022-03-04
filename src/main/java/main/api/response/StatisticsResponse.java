package main.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatisticsResponse {
    private Long postsCount;
    private Long likesCount;
    private Long dislikesCount;
    private Long viewsCount;
    private Long firstPublication;

}
