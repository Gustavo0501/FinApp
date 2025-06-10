package com.gustavoresende.FinApp.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING) // Armazena o nome da enum (INCOME/EXPENSE)
    @Column(nullable = false)
    private TransactionType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(nullable = false)
    private boolean isRecurring;

    @Enumerated(EnumType.STRING)
    @Column(name = "recurrence_frequency")
    private RecurrenceFrequency recurrenceFrequency;

    @Column(name = "recurrence_end_date")
    private LocalDate recurrenceEndDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Construtor padrão
    public Transaction() {
        this.isRecurring = false; // Valor padrão
    }

    // Construtor para criação (sem ID, transação não recorrente)
    public Transaction(BigDecimal amount, String description, LocalDate date, TransactionType type, Category category, Account account, User user) {
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.type = type;
        this.category = category;
        this.account = account;
        this.user = user;
        this.isRecurring = false;
    }

    // Construtor para criação (sem ID, transação recorrente)
    public Transaction(BigDecimal amount, String description, LocalDate date, TransactionType type, Category category, Account account, User user, boolean isRecurring, RecurrenceFrequency recurrenceFrequency, LocalDate recurrenceEndDate) {
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.type = type;
        this.category = category;
        this.account = account;
        this.user = user;
        this.isRecurring = isRecurring;
        this.recurrenceFrequency = recurrenceFrequency;
        this.recurrenceEndDate = recurrenceEndDate;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public boolean isRecurring() {
        return isRecurring;
    }

    public void setRecurring(boolean recurring) {
        isRecurring = recurring;
    }

    public RecurrenceFrequency getRecurrenceFrequency() {
        return recurrenceFrequency;
    }

    public void setRecurrenceFrequency(RecurrenceFrequency recurrenceFrequency) {
        this.recurrenceFrequency = recurrenceFrequency;
    }

    public LocalDate getRecurrenceEndDate() {
        return recurrenceEndDate;
    }

    public void setRecurrenceEndDate(LocalDate recurrenceEndDate) {
        this.recurrenceEndDate = recurrenceEndDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
