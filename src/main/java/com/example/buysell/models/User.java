package com.example.buysell.models;

import com.example.buysell.models.enums.Role;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "users")
@Data
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "name")
    private String name;
    @Column(name = "active")
    private boolean active;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "image_id")
    private Image avatar;
    private String activationCode;
    @Column(name = "password", length = 1000)
    private String password;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();
    //Установка соединения между таблицами User и Product oneToMany
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "user")
    private List<Product> products = new ArrayList<>();


    public void addProductToUser(Product product) {
        product.setUser(this);
        products.add(product);
    }
    private LocalDateTime dateOfCreate;

    @PrePersist
    private void init(){
        dateOfCreate = LocalDateTime.now();
    }


    public boolean isAdmin(){return roles.contains(Role.ROLE_ADMIN);}
    // security config

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

    public User() {

    }

    public User(Long id, String email, String phoneNumber, String name, boolean active, Image avatar, String activationCode, String password, Set<Role> roles, List<Product> products, LocalDateTime dateOfCreate) {
        this.id = id;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.active = active;
        this.avatar = avatar;
        this.activationCode = activationCode;
        this.password = password;
        this.roles = roles;
        this.products = products;
        this.dateOfCreate = dateOfCreate;
    }
}
