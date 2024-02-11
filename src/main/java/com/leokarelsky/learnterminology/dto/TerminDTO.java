package com.leokarelsky.learnterminology.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Objects;

@Data
public class TerminDTO {
    private long id;
    @NotBlank(message = "The 'termin' cannot be empty")
    private String name;
    @NotBlank(message = "The 'meaning' cannot be empty")
    private String meaning;
    private long collectionId;
    public TerminDTO() {
    }

    public TerminDTO(long id, String name, String meaning, long collectionId) {
        this.id = id;
        this.name = name;
        this.meaning = meaning;
        this.collectionId = collectionId;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TerminDTO terminDTO = (TerminDTO) o;
        return id == terminDTO.id && collectionId == terminDTO.collectionId && Objects.equals(name, terminDTO.name) && Objects.equals(meaning, terminDTO.meaning);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, meaning, collectionId);
    }

    @Override
    public String toString() {
        return "TerminDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", meaning='" + meaning + '\'' +
                ", collectionId=" + collectionId +
                '}';
    }
}
