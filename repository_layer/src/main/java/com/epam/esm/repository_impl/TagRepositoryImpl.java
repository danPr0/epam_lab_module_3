package com.epam.esm.repository_impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.repository.TagRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
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
    public Optional<Tag> getMostPopularEntity() {

        User user = em.createQuery("select o.user from Order o group by o.user order by sum(o.cost) desc limit 1",
                User.class).getSingleResult();

        TypedQuery<Tag> query = em.createQuery(
                "select tag from Order o left join o.giftCertificate gc left join gc.tags tag " +
                        "where o.user.id = :userId group by tag.id order by count(tag.id) desc limit 1", Tag.class);
        query.setParameter("userId", user.getId());

        return Optional.ofNullable(query.getSingleResult());
    }

    @Override
    public void deleteEntity(Long id) {

        em.remove(em.find(Tag.class, id));
    }
}
