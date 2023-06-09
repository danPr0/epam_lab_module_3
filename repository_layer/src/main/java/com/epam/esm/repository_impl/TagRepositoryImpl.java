package com.epam.esm.repository_impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.TagRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Implementation of DAO Interface {@link TagRepository}.
 *
 * @author Danylo Proshyn
 */

@Repository
@Transactional
public class TagRepositoryImpl implements TagRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void insertEntity(Tag tag) {

        em.persist(tag);
    }

    @Override
    public Optional<Tag> getEntity(Long id) {

        return Optional.ofNullable(em.find(Tag.class, id));
    }

    @Override
    public Optional<Tag> getMostPopularEntity(Long userId) {

        Query query = em.createNativeQuery(
                "select * from tags inner join gift_certificates_tags on tags.id = gift_certificates_tags.tag_id inner " +
                        "join orders on gift_certificates_tags.gc_id = orders.gc_id where user_id = :userId group by " +
                        "tag_id order by sum(cost) desc limit 1", Tag.class);
        query.setParameter("userId", userId);

        return Optional.ofNullable((Tag) query.getSingleResult());
    }

    @Override
    public void deleteEntity(Long id) {

        em.remove(em.find(Tag.class, id));
    }
}
