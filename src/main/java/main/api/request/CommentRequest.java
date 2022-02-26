package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Setter
@Getter
public class CommentRequest {
    @Nullable
    @JsonProperty("parent_id")
    private Integer parentId;
    @JsonProperty("post_id")
    private Integer postId;
    private String text;
}
