package com.leokarelsky.learnterminology.service.impl;

import com.leokarelsky.learnterminology.exception.NullEntityReferenceException;
import com.leokarelsky.learnterminology.model.Termin;
import com.leokarelsky.learnterminology.repository.TerminRepository;
import com.leokarelsky.learnterminology.service.TerminService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TerminServiceImpl implements TerminService {

    private static final Logger logger = LoggerFactory.getLogger(CollectionServiceImpl.class);

    private final TerminRepository terminRepository;

    public TerminServiceImpl(TerminRepository terminRepository) {
        this.terminRepository = terminRepository;
    }

    @Override
    public Termin create(Termin termin) {
        if (termin != null) {
            return terminRepository.save(termin);
        }
        throw new NullEntityReferenceException("Termin cannot be 'null'");
    }

    @Override
    public Termin readById(long id) {
        EntityNotFoundException exception = new EntityNotFoundException("Termin with id " + id + " not found");
        logger.error(exception.getMessage(), exception);

        return terminRepository.findById(id).orElseThrow(() -> exception);
    }

    @Override
    public Termin update(Termin termin) {
        if (termin != null) {
            readById(termin.getId());
            return terminRepository.save(termin);
        }
        throw new NullEntityReferenceException("Termin cannot be 'null'");
    }

    @Override
    public void delete(long id) {
        Termin termin = readById(id);
        terminRepository.delete(termin);
    }

    @Override
    public List<Termin> getAll() {
        return terminRepository.findAll();
    }

    @Override
    public List<Termin> getByCollectionId(long collectionId) {
        return terminRepository.getByCollectionId(collectionId);
    }
}
