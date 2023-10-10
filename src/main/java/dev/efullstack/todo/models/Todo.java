package dev.efullstack.todo.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;

public record Todo(@Id Long id, String name, String description, @Column("date_time") LocalDateTime dateTime, boolean status) {}
