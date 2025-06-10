package com.gustavoresende.FinApp.models;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // Ex: "Conta Corrente Banco X", "Poupança Y", "Cartão de Crédito Z"

    @Column(nullable = false)
    private BigDecimal balance; // Saldo atual da conta

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Construtor padrão
    public Account() {
        this.balance = BigDecimal.ZERO; // Inicializa saldo como zero por padrão
    }

    // Construtor para criação (sem ID)
    public Account(String name, BigDecimal initialBalance, User user) {
        this.name = name;
        this.balance = initialBalance != null ? initialBalance : BigDecimal.ZERO;
        this.user = user;
    }

    // Getters e Setters
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
}
