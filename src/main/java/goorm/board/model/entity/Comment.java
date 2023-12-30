package goorm.board.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    private String commentContent;

    private Boolean status = Boolean.TRUE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @JsonIgnore
    private Post post;

    public void deleteComment() {
        this.status = Boolean.FALSE;
    }

    public Comment(String commentContent, Post post) {
        this.commentContent = commentContent;
        this.post = post;
        post.getComments().add(this);
    }

//    public void addComment(Post post) {
//        this.post = post;
//        post.getComments().add(this);
//    }

    public Comment changeComment(String content) {
        this.commentContent = content;
        return this;
    }
}
