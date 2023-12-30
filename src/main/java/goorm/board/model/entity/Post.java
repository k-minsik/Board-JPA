package goorm.board.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    private String postTitle;

    private String postContent;

    private Boolean status = Boolean.TRUE;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    List<Comment> comments = new ArrayList<>();


    public void deletePost() {
        this.status = Boolean.FALSE;
        for (Comment comment : comments) {
            comment.deleteComment();
        }
    }

    public Post(String postTitle, String postContent) {
        this.postTitle = postTitle;
        this.postContent = postContent;
    }

    public Post changePost(String title, String content) {
        this.postTitle = title;
        this.postContent = content;
        return this;
    }
}
