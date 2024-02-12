package com.seb_main_034.SERVER.users.entity;

import com.seb_main_034.SERVER.movie.entity.Movie;
import com.seb_main_034.SERVER.comment.entity.Comment;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, updatable = false, unique = true)
    private String email;

    @Length(min = 8, max = 200)
    @Column(nullable = false)
    private String password;

    @Length(min = 2)
    @Column(nullable = false, unique = true)
    private String nickName;

    private String proFilePicture;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE) // 회원탈퇴시, 작성한 댓글도 삭제됨
    private List<Comment> commentList = new ArrayList<Comment>();

    // The recommendedMovies field is declared here. No need to declare it again.
    @OneToMany(mappedBy = "user")
    private List<Movie> recommendedMovies = new ArrayList<>();

    public List<Movie> getRecommendedMovies() {
        return recommendedMovies;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public Users() {
    }

    public Users(String email) {
        this.email = email;
    } // For social login

    public Users(String email, String password, String nickName, String proFilePicture) {
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.proFilePicture = proFilePicture;
    }
}
