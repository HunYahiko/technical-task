package com.gringotts.technicaltask.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "POST_COMMENTS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PostComment {

    @Id
    @Column(name = "ID", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_comments_seq")
    @SequenceGenerator(
            name = "post_comments_seq",
            sequenceName = "post_comments_seq",
            allocationSize = 1)
    private Long id;

    @Column(name = "REVIEW")
    @Setter
    private String review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID")
    @Setter
    private Post post;

    public PostComment(String review) {
        this.review = review;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof PostComment that)) return false;
        return id != null && id.equals(that.id);
    }
}
