package com.gustavoresende.FinApp.models; // Ajuste o pacote conforme seu projeto

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "goal")
public class Goal {

    public interface CreateGoal{}
    public interface UpdateGoal{}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @NotNull(groups = {CreateGoal.class, UpdateGoal.class}, message = "O nome da meta não pode ser nulo.")
    @NotEmpty(groups = {CreateGoal.class, UpdateGoal.class}, message = "O nome da meta não pode ser vazio.")
    @Size(groups = {CreateGoal.class, UpdateGoal.class}, max = 200, message = "O nome da meta não pode exceder 200 caracteres.")
    @Column(nullable = false, length = 200)
    private String name;

    @NotNull(groups = {CreateGoal.class, UpdateGoal.class}, message = "O valor alvo não pode ser nulo.")
    @DecimalMin(value = "0.01", message = "O valor alvo deve ser positivo e maior que zero.")
    @Digits(integer = 17, fraction = 2, message = "O valor alvo deve ter no máximo 17 dígitos inteiros e 2 decimais.")
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal targetAmount; // O valor total que o usuário deseja alcançar para esta meta

    @NotNull(groups = {CreateGoal.class, UpdateGoal.class}, message = "O valor atual não pode ser nulo.")
    @DecimalMin(value = "0.00", inclusive = true, message = "O valor atual não pode ser negativo.")
    @Digits(integer = 17, fraction = 2, message = "O valor atual deve ter no máximo 17 dígitos inteiros e 2 decimais.")
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal currentAmount; // O valor já acumulado para a meta

    @FutureOrPresent(groups = {CreateGoal.class, UpdateGoal.class}, message = "A data de término da meta não pode ser passada.")
    @Column(name = "end_date")
    private LocalDate endDate; // Data de término da meta (pode ser nula se não houver prazo)

    @Size(groups = {CreateGoal.class, UpdateGoal.class}, max = 1000, message = "A descrição não pode exceder 1000 caracteres.")
    @Column(columnDefinition = "TEXT") // Permite armazenar textos mais longos no MySQL
    private String description; // Descrição detalhada da meta (opcional)

    // Relacionamento Many-to-One com User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "FK_GOAL_USER"))
    private User user;

    // Construtor padrão
    public Goal() {
        this.currentAmount = BigDecimal.ZERO;
    }

    // Construtor para criação de novas metas (sem ID)
    public Goal(String name, BigDecimal targetAmount, LocalDate endDate, String description, User user) {
        this.name = name;
        this.targetAmount = targetAmount;
        this.endDate = endDate;
        this.description = description;
        this.user = user;
        this.currentAmount = BigDecimal.ZERO;
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

    public BigDecimal getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(BigDecimal targetAmount) {
        this.targetAmount = targetAmount;
    }

    public BigDecimal getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(BigDecimal currentAmount) {
        this.currentAmount = currentAmount;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // --- Métodos utilitários para gerenciar o progresso da meta ---
    public void addToCurrentAmount(BigDecimal amount) {
        if (amount != null && amount.compareTo(BigDecimal.ZERO) > 0) {
            this.currentAmount = this.currentAmount.add(amount);
            // Garante que o valor acumulado não exceda o valor alvo
            if (this.currentAmount.compareTo(this.targetAmount) > 0) {
                this.currentAmount = this.targetAmount;
            }
        }
    }

    // Calcula a porcentagem de progresso da meta
    public double getProgressPercentage() {
        if (targetAmount == null || targetAmount.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }
        return (this.currentAmount.doubleValue() / this.targetAmount.doubleValue()) * 100.0;
    }

    // Verifica se a meta foi alcançada
    public boolean isAchieved() {
        return this.currentAmount != null && this.targetAmount != null && this.currentAmount.compareTo(this.targetAmount) >= 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Goal goal = (Goal) o;
        return Objects.equals(id, goal.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}