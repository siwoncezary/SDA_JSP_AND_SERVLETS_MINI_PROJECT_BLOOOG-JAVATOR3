package entity.article;

import entity.tag.TagEntity;
import entity.user.UserEntity;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "article")
public class ArticleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id", unique = true, nullable = false)
    private long id;

    private String content;

    private String title;

    private LocalDateTime created;

    @ManyToOne
    @JoinColumn(name="author_id", nullable = false)
    private UserEntity author;

    @ManyToMany
    @JoinTable(
            name = "article_tag",
            joinColumns = {@JoinColumn(name="article_id")},
            inverseJoinColumns = {@JoinColumn(name="tag_id")}
    )
    private Set<TagEntity> tags = new HashSet<>();

    public ArticleEntity(NewArticle na){
        this.content = na.content;
        this.title = na.title;
        this.created = LocalDateTime.now();
        this.author = new UserEntity(na.author);
        this.tags = na.tags.stream().map(t->new TagEntity(t)).collect(Collectors.toSet());
    }

    public ArticleEntity(Article a){
        this.id = a.id;
        this.title = a.title;
        this.created = a.created;
        this.content = a.content;
        this.author = new UserEntity(a.author);
        this.tags = a.tags.stream().map(t->new TagEntity(t)).collect(Collectors.toSet());
    }

    public ArticleEntity() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public UserEntity getAuthor() {
        return author;
    } //dodać

    public void setAuthor(UserEntity author) {
        this.author = author;
    } //dodać

    public Set<TagEntity> getTags() {
        return tags;
    }

    public void setTags(Set<TagEntity> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArticleEntity)) return false;
        ArticleEntity that = (ArticleEntity) o;
        return getId() == that.getId() &&
                Objects.equals(getContent(), that.getContent()) &&
                Objects.equals(getTitle(), that.getTitle()) &&
                Objects.equals(getCreated(), that.getCreated()) &&
                Objects.equals(getAuthor(), that.getAuthor()) &&
                Objects.equals(getTags(), that.getTags());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getContent(), getTitle(), getCreated(), getAuthor(), getTags());
    }
}
