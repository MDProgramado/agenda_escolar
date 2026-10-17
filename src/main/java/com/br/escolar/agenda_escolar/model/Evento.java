package com.br.escolar.agenda_escolar.model;

import com.br.escolar.agenda_escolar.enums.SituacaoEvento;
import jakarta.persistence.*;

import java.time.LocalDateTime;

import java.util.Date;

@Entity
@Table(name = "eventos")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String titulo;

    @Column(length = 500)
    private String descricao;

    @Column(nullable = false, length = 100)
    private String responsavel;

    @Column(nullable = false)
    private String email_responsavel;

    @Column(nullable = false)
    private LocalDateTime inicio;

    @Column(nullable = false)
    private LocalDateTime fim;

    @Column(nullable = false)
    private Integer publico_previsto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private SituacaoEvento situacao = SituacaoEvento.AGENDADO;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "espaco_id", nullable = false)
    private Espaco espaco;

    public Evento(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public String getEmail_responsavel() {
        return email_responsavel;
    }

    public void setEmail_responsavel(String email_responsavel) {
        this.email_responsavel = email_responsavel;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }

    public LocalDateTime getFim() {
        return fim;
    }

    public void setFim(LocalDateTime fim) {
        this.fim = fim;
    }

    public Integer getPublico_previsto() {
        return publico_previsto;
    }

    public void setPublico_previsto(Integer publico_previsto) {
        this.publico_previsto = publico_previsto;
    }

    public SituacaoEvento getSituacao() {
        return situacao;
    }

    public void setSituacao(SituacaoEvento situacao) {
        this.situacao = situacao;
    }

    public Espaco getEspaco() {
        return espaco;
    }

    public void setEspaco(Espaco espaco) {
        this.espaco = espaco;
    }
}
