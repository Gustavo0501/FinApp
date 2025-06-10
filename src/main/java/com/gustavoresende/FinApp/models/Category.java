package com.gustavoresende.FinApp.models; // Ajuste o pacote conforme seu projeto

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "category", uniqueConstraints = {
        // Garante que um usuário não possa ter duas categorias com o mesmo nome
        @UniqueConstraint(columnNames = {"name", "user_id"}, name = "UK_CATEGORY_NAME_USER")
})
public class Category {

    public interface CreateCategory{}
    public interface UpdateCategory{}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @NotNull(groups = {CreateCategory.class, UpdateCategory.class}, message = "O nome da categoria não pode ser nulo.")
    @NotEmpty(groups = {CreateCategory.class, UpdateCategory.class}, message = "O nome da categoria não pode ser vazio.")
    @Size(groups = {CreateCategory.class, UpdateCategory.class}, max = 100, message = "O nome da categoria não pode exceder 100 caracteres.")
    @Column(nullable = false, length = 100)
    private String name;

    // Relacionamento Many-to-One com User
    @NotNull(groups = CreateCategory.class, message = "O usuário da categoria não pode ser nulo.") // Uma categoria sempre pertence a um usuário
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "FK_CATEGORY_USER"))
    private User user;

    // Relacionamento One-to-Many com Transaction (opcional para navegação bidirecional)
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();

    // Construtor padrão
    public Category() {
    }

    // Construtor para criação
    public Category(String name, User user) {
        this.name = name;
        this.user = user;
    }

    // --- Getters e Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    // --- Métodos utilitários para gerenciar coleções ---
    public void addTransaction(Transaction transaction) {
        if (transaction != null && !this.transactions.contains(transaction)) {
            this.transactions.add(transaction);
            transaction.setCategory(this);
        }
    }

    public void removeTransaction(Transaction transaction) {
        if (transaction != null && this.transactions.contains(transaction)) {
            this.transactions.remove(transaction);
            transaction.setCategory(null);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}