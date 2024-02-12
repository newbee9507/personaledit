package com.seb_main_034.SERVER.comment.entity;

import com.seb_main_034.SERVER.movie.entity.Movie;
import com.seb_main_034.SERVER.rating.entity.Rating;
import com.seb_main_034.SERVER.users.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(nullable = false)
    private String text;

    private String nickName;

    private LocalDateTime createAt;

    private LocalDateTime modifyAt;

//    private List<Comment> reComment; // 대댓글. 댓글기능 완성 후 구현 고려

    @ManyToOne
    @JoinColumn(name = "user_id") // 나를 쓴 유저의 id를 알고 있어야 하지 않을까?
    private Users user;

    @ManyToOne
    @JoinColumn(name = "movie_id") // 내가 무슨 영화의 댓글인지 알아야할까? 잘 모르겠음. 내가 작성한 댓글 -> 영화..?
    private Movie movie;

    @OneToOne(cascade = CascadeType.REMOVE) // 댓글이 삭제되면 평점도 같이 삭제됨
    @JoinColumn(name = "id")
    private Rating rating;

    // 만들어진 일시
    @PrePersist
    public void setCreateAt() {
        this.createAt = LocalDateTime.now();
    }

    // 수정된 시간
    @PreUpdate
    public void setModifyAt() {
        this.modifyAt = LocalDateTime.now();
    }

    public Comment() {
    }
}
