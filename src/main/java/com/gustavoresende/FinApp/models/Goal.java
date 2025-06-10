package com.gustavoresende.FinApp.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "goals")
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal targetAmount; // Valor total a ser alcançado

    @Column(nullable = false)
    private BigDecimal currentAmount; // Valor já acumulado

    @Column(name = "end_date")
    private LocalDate endDate; // Data de término da meta (opcional)

    @Column(columnDefinition = "TEXT") // Pode ser um texto mais longo
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Construtor padrão
    public Goal() {
        this.currentAmount = BigDecimal.ZERO; // Inicializa o valor acumulado como zero
    }

    // Construtor para criação (sem ID)
    public Goal(String name, BigDecimal targetAmount, LocalDate endDate, String description, User user) {
        this.name = name;
        this.targetAmount = targetAmount;
        this.endDate = endDate;
        this.description = description;
        this.user = user;
        this.currentAmount = BigDecimal.ZERO;
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

    // Método utilitário para adicionar ao progresso da meta
    public void addToCurrentAmount(BigDecimal amount) {
        if (amount != null && amount.compareTo(BigDecimal.ZERO) > 0) {
            this.currentAmount = this.currentAmount.add(amount);
            if (this.currentAmount.compareTo(this.targetAmount) > 0) {
                this.currentAmount = this.targetAmount; // Não exceder o valor alvo
            }
        }
    }
}
