package com.gustavoresende.FinApp.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "user")
public class User {
    public interface CreateUser{

    }

    public interface UpdateUser{

    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @NotNull(groups = CreateUser.class)
    @NotEmpty(groups = CreateUser.class)
    @Email(message = "O nome de usuário deve ser um email válido.")
    @Column(nullable = false, unique = true, length = 100) // Não nulo, único e com tamanho máximo de 100 caracteres
    private String username; // Geralmente utilizado como o email para login

    @JsonProperty(access = Access.WRITE_ONLY)
    @NotNull(groups = {CreateUser.class, UpdateUser.class})
    @NotEmpty(groups = {CreateUser.class, UpdateUser.class})
    @Size(groups = {CreateUser.class, UpdateUser.class},min = 8, max = 100)
    @Column(nullable = false, length = 255) // Não nulo. Armazena a senha criptografada (hashed)
    private String password;

    @NotNull(groups = CreateUser.class)
    @NotEmpty(groups = CreateUser.class)
    @Size(groups = CreateUser.class,min = 4, max = 100)
    @Column(nullable = false, length = 200) // Não nulo
    private String fullName;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Account> accounts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Goal> goals = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Category> categories = new ArrayList<>();

    // Construtor padrão (necessário para JPA)
    public User() {
    }

    // Construtor para facilitar a criação de novos usuários (sem ID)
    public User(String username, String password, String fullName) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public List<Account> getAccounts() {
        return Collections.unmodifiableList(accounts);
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public List<Goal> getGoals() {
        return Collections.unmodifiableList(goals);
    }

    public void setGoals(List<Goal> goals) {
        this.goals = goals;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    // Métodos utilitários para gerenciar coleções (opcional, mas boa prática)
    public void addAccount(Account account) {
        this.accounts.add(account);
        account.setUser(this);
    }

    public void removeAccount(Account account) {
        this.accounts.remove(account);
        account.setUser(null);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null)
            return false;
        if(! (obj instanceof User)){
            return false;
        }
        User other = (User) obj;
        if (this.id == null){
            if (other.id != null)
                return false;
            else if(!this.id.equals(other.id))
                return false;
        }
        return Objects.equals(this.id, other.id) && Objects.equals(this.username, other.username)
                && Objects.equals(this.password, other.password);

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.id == null ? 0 : this.id.hashCode());
        return result;
    }
}
