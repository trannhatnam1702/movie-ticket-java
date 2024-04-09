package com.example.movie.Entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    //    @NotBlank(message = "Email is required")
//    @Size(max = 50, message = "Email must be less than 50 characters")
    @Column(name = "Email")
    private String Email;

    //    @NotBlank(message = "Name is required")
//    @Size(max = 20, message = "Your name must be less than 20 characters")
    @Column(name = "fullName", nullable = false)
    private String fullName;

    @Size(min = 6, message = "Passwords must be at least 6 characters")
//    @Pattern(
//            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
//            message = "Passwords must be at least 6 characters"
//    )
    @Column(name = "Password", nullable = false)
    private String Password;

    //@NotBlank(message = "Username is required")
    @Size(max = 20, message = "Username must be less than 20 characters")
    @Column(name = "username", nullable = false, unique = true)
//    @ValidUsername
    private String username;

    //@NotBlank(message = "Passwords do not match")
    //@Transient
    //private String confirmPassword;

    @Column(name = "reward_points")
    private int rewardPoints;

    @ManyToOne
    @JoinColumn(name = "ranking_id")
    private Ranking ranking;

    @ManyToMany
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Roles> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade =  CascadeType.ALL)
    Set<Invoice> invoices;

    public String getRoleNames() {
        return roles.stream()
                .map(Roles::getName)
                .collect(Collectors.joining(", "));
    }

}