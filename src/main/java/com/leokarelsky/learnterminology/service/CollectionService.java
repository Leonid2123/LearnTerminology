package com.leokarelsky.learnterminology.service;

import com.leokarelsky.learnterminology.model.Termin;
import com.leokarelsky.learnterminology.model.TerminCollection;

import java.util.List;

public interface CollectionService {
    TerminCollection create(TerminCollection collection);
    TerminCollection readById(long id);
    TerminCollection update(TerminCollection collection);
    void delete(long id);
    List<TerminCollection> getAll();
    List<TerminCollection> getByUserId(long userId);
}
