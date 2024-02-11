package com.leokarelsky.learnterminology.repository;

import com.leokarelsky.learnterminology.model.TerminCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CollectionRepository extends JpaRepository<TerminCollection, Long> {
    @Query(value = "select id, title, created_at, owner_id from collections where owner_id = ?1 union " +
            "select id, title, created_at, owner_id from collections inner join collection_collaborator on id = collection_id and " +
            "collaborator_id = ?1", nativeQuery = true)
    List<TerminCollection> getByUserId(long userId);
}
