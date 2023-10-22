package com.gringotts.technicaltask.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "POSTS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Post {

    @Id
    @Column(name = "ID", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "posts_seq")
    @SequenceGenerator(
            name = "posts_seq",
            sequenceName = "posts_seq",
            allocationSize = 1)
    private Long id;

    @Column(name = "TITLE")
    @Setter
    private String title;

    @Column(name = "DESCRIPTION")
    @Setter
    private String description;

    @Column(name = "CONTENT")
    @Setter
    private String content;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "post")
    private final List<PostComment> comments = new ArrayList<>();

    public Post(String title,
                String content,
                String description) {
        this.title = title;
        this.description = description;
        this.content = content;
    }

    public void addComment(PostComment comment) {
        comments.add(comment);
        comment.setPost(this);
    }

    public void removeComment(PostComment comment) {
        comments.remove(comment);
        comment.setPost(null);
    }

    public List<PostComment> getComments() {
        return Collections.unmodifiableList(comments);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Post that)) return false;
        return id != null && id.equals(that.id);
    }
}
