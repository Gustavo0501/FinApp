package com.gustavoresende.FinApp.models; // Ajuste o pacote conforme seu projeto

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "account") // Nome da tabela no banco de dados
public class Account {

    public interface CreateAccount{}
    public interface UpdateAccount{}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @NotNull(groups = {CreateAccount.class, UpdateAccount.class}, message = "O nome da conta não pode ser nulo.")
    @NotEmpty(groups = {CreateAccount.class, UpdateAccount.class}, message = "O nome da conta não pode ser vazio.")
    @Size(groups = {CreateAccount.class, UpdateAccount.class}, max = 100, message = "O nome da conta não pode exceder 100 caracteres.")
    @Column(nullable = false, length = 100)
    private String name; // Ex: "Conta Corrente Banco X", "Poupança Y", "Cartão de Crédito Z"

    @NotNull(groups = {CreateAccount.class, UpdateAccount.class}, message = "O saldo não pode ser nulo.")
    @DecimalMin(value = "0.00", inclusive = true, message = "O saldo não pode ser negativo.") // Saldo não pode ser menor que 0
    @Digits(integer = 17, fraction = 2, message = "O saldo deve ter no máximo 17 dígitos inteiros e 2 decimais.")
    @Column(nullable = false, precision = 19, scale = 2) // Precisão total de 19 dígitos, 2 para parte decimal
    private BigDecimal balance; // Saldo atual da conta

    // Relacionamento Many-to-One com User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "FK_ACCOUNT_USER")) // user_id é uma coluna obrigatória
    private User user;

    // Relacionamento One-to-Many com Transaction: Uma conta pode ter muitas transações.
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();


    // Construtor padrão (necessário para JPA)
    public Account() {
        this.balance = BigDecimal.ZERO;
    }

    // Construtor para criação de novas contas (sem ID)
    public Account(String name, BigDecimal initialBalance, User user) {
        this.name = name;
        this.balance = (initialBalance != null) ? initialBalance : BigDecimal.ZERO;
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

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
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
            transaction.setAccount(this);
        }
    }

    public void removeTransaction(Transaction transaction) {
        if (transaction != null && this.transactions.contains(transaction)) {
            this.transactions.remove(transaction);
            transaction.setAccount(null);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}