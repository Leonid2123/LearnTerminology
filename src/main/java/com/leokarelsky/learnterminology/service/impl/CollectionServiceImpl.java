package com.leokarelsky.learnterminology.service.impl;

import com.leokarelsky.learnterminology.exception.NullEntityReferenceException;
import com.leokarelsky.learnterminology.model.TerminCollection;
import com.leokarelsky.learnterminology.repository.CollectionRepository;
import com.leokarelsky.learnterminology.service.CollectionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectionServiceImpl implements CollectionService {

    private final CollectionRepository collectionRepository;

    public CollectionServiceImpl(CollectionRepository collectionRepository) {
        this.collectionRepository = collectionRepository;
    }

    @Override
    public TerminCollection create(TerminCollection collection) {
        if (collection != null) {
            return collectionRepository.save(collection);
        }
        throw new NullEntityReferenceException("Collection cannot be 'null'");
    }

    @Override
    public TerminCollection readById(long id) {
        EntityNotFoundException exception = new EntityNotFoundException("Collection with id " + id + " not found");
        return collectionRepository.findById(id).orElseThrow(() -> exception);
    }

    @Override
    public TerminCollection update(TerminCollection collection) {
        if (collection != null) {
            readById(collection.getId());
            return collectionRepository.save(collection);
        }
        throw new NullEntityReferenceException("Collection cannot be 'null'");
    }

    @Override
    public void delete(long id) {
        TerminCollection collection = readById(id);
        collectionRepository.delete(collection);
    }

    @Override
    public List<TerminCollection> getAll() {
        return collectionRepository.findAll();
    }

    @Override
    public List<TerminCollection> getByUserId(long userId) {
        return collectionRepository.getByUserId(userId);
    }
}
