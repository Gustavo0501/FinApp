package com.gustavoresende.FinApp.models; // Ajuste o pacote conforme seu projeto

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "transaction")
public class Transaction {

    public interface CreateTransaction{}
    public interface UpdateTransaction{}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @NotNull(groups = {CreateTransaction.class, UpdateTransaction.class}, message = "O valor da transação não pode ser nulo.")
    @DecimalMin(value = "0.01", message = "O valor da transação deve ser positivo e maior que zero.")
    @Digits(integer = 17, fraction = 2, message = "O valor da transação deve ter no máximo 17 dígitos inteiros e 2 decimais.")
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @NotNull(groups = {CreateTransaction.class, UpdateTransaction.class}, message = "A descrição não pode ser nula.")
    @NotEmpty(groups = {CreateTransaction.class, UpdateTransaction.class}, message = "A descrição não pode ser vazia.")
    @Size(groups = {CreateTransaction.class, UpdateTransaction.class}, max = 255, message = "A descrição não pode exceder 255 caracteres.")
    @Column(nullable = false, length = 255)
    private String description;

    @NotNull(groups = {CreateTransaction.class, UpdateTransaction.class}, message = "A data da transação não pode ser nula.")
    @PastOrPresent(message = "A data da transação não pode ser futura.") // Transação não pode ser em data futura
    @Column(nullable = false)
    private LocalDate date;

    @NotNull(groups = {CreateTransaction.class, UpdateTransaction.class}, message = "O tipo de transação não pode ser nulo.")
    @Enumerated(EnumType.STRING) // Armazena o nome do enum (INCOME/EXPENSE) no banco
    @Column(nullable = false, length = 10) // Tamanho suficiente para "EXPENSE"
    private TransactionType type;

    // Relacionamento Many-to-One com Category
    @NotNull(groups = {CreateTransaction.class, UpdateTransaction.class}, message = "A categoria não pode ser nula.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false, foreignKey = @ForeignKey(name = "FK_TRANSACTION_CATEGORY"))
    private Category category;

    // Relacionamento Many-to-One com Account
    @NotNull(groups = {CreateTransaction.class, UpdateTransaction.class}, message = "A conta não pode ser nula.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false, foreignKey = @ForeignKey(name = "FK_TRANSACTION_ACCOUNT"))
    private Account account;

    @Column(nullable = false)
    private boolean isRecurring; // Indica se a transação é recorrente

    @Enumerated(EnumType.STRING)
    @Column(name = "recurrence_frequency", length = 20) // Frequência da recorrência (ex: MONTHLY)
    private RecurrenceFrequency recurrenceFrequency;

    @Column(name = "recurrence_end_date")
    private LocalDate recurrenceEndDate; // Data de término da recorrência (pode ser nula para recorrência infinita)

    // Relacionamento Many-to-One com User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "FK_TRANSACTION_USER"))
    private User user;

    // Construtor padrão
    public Transaction() {
        this.isRecurring = false;
    }

    // Construtor para transação não recorrente
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

    // Construtor para transação recorrente
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

    // --- Getters e Setters ---
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}