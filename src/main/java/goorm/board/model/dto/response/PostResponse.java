package goorm.board.model.dto.response;

import goorm.board.model.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    private String title;
    private String content;
    private List<CommentResponse> comments;
}
