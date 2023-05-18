package ru.baron.cloudapp.entity;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class FileData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String path;
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="user_id")
    private User user;
    private Double size;

    public FileData(String name, String path, User user, double size) {
        this.name = name;
        this.path = path;
        this.user = user;
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileData fileData = (FileData) o;
        return Objects.equals(name, fileData.name) && Objects.equals(path, fileData.path)
                && Objects.equals(user, fileData.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, path, user);
    }
}
