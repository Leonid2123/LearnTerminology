package com.leokarelsky.learnterminology.service;

import com.leokarelsky.learnterminology.model.Termin;
import com.leokarelsky.learnterminology.model.TerminCollection;

import java.util.List;

public interface TerminService {
    Termin create(Termin termin);
    Termin readById(long id);
    Termin update(Termin termin);
    void delete(long id);
    List<Termin> getAll();

    List<Termin> getByCollectionId(long collectionId);
}
