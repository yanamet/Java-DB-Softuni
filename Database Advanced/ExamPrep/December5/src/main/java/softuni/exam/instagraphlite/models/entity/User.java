package softuni.exam.instagraphlite.models.entity;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @ManyToOne(optional = false)
    private Picture profilePicture;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Post> posts;

    public User() {
    }

    public Picture getProfilePicture() {
        return profilePicture;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public void setProfilePicture(Picture profilePicture) {
        this.profilePicture = profilePicture;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String userToString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("User: %s", this.getUsername())).append(System.lineSeparator());
        sb.append(String.format("Post count: %d", this.posts.size())).append(System.lineSeparator());

        String postsString = this.getPosts().stream()
                .map(Post::toString)
                .collect(Collectors.joining("\n"));

        sb.append(postsString);

        return sb.toString();

    }

}
