package com.leokarelsky.learnterminology.repository;

import com.leokarelsky.learnterminology.model.Termin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TerminRepository extends JpaRepository<Termin, Long> {
    @Query("from Termin where collection.id = :collectionId")
    List<Termin> getByCollectionId(long collectionId);
}
