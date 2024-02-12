package com.seb_main_034.SERVER.movie.entity;

import com.seb_main_034.SERVER.rating.entity.Rating;
import com.seb_main_034.SERVER.users.entity.Users;
import com.seb_main_034.SERVER.comment.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movieId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "userId")
    private Users user;

    @Column(nullable = true)
    private double averageRating;

    @Column(nullable = true)
    private String genre;

    @Column
    private String previewPicture;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.REMOVE)
    private List<Comment> commentList = new ArrayList<>(); // 이 부분을 수정

    @OneToMany(mappedBy = "movie", cascade = CascadeType.REMOVE)
    private List<Rating> ratingList = new ArrayList<>();  // 새로운 필드 추가

    private String streamingURL;

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void updateAverageRating() {
        this.averageRating = ratingList.stream()
                .mapToDouble(Rating::getRating)
                .average()
                .orElse(0.0);
    }
}


