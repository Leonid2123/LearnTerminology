package com.leokarelsky.learnterminology.dto;

import com.leokarelsky.learnterminology.model.Termin;
import com.leokarelsky.learnterminology.model.TerminCollection;

public class TerminTransformer {
    public static TerminDTO convertToDto(Termin termin) {
        return new TerminDTO(
                termin.getId(),
                termin.getName(),
                termin.getMeaning(),
                termin.getCollection().getId()
        );
    }

    public static Termin convertToEntity(TerminDTO terminDto, TerminCollection collection) {
        Termin termin = new Termin();
        termin.setId(terminDto.getId());
        termin.setName(terminDto.getName());
        termin.setMeaning(terminDto.getMeaning());
        termin.setCollection(collection);
        return termin;
    }
}
