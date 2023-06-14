package com.epam.esm.repository_impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.GiftCertificateRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of DAO Interface {@link GiftCertificateRepository}.
 *
 * @author Danylo Proshyn
 */

@Repository
@Transactional
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public GiftCertificate insertEntity(GiftCertificate gc) {

        em.persist(gc);

        return em.find(GiftCertificate.class, gc.getId());
    }

    @Override
    public Optional<GiftCertificate> getEntity(Long id) {

        return Optional.ofNullable(em.find(GiftCertificate.class, id));

    }

    @Override
    public GiftCertificate updateEntity(GiftCertificate gc) {

        em.merge(gc);

        return em.find(GiftCertificate.class, gc.getId());
    }

    @Override
    public void deleteEntity(Long id) {

        em.detach(em.find(GiftCertificateRepository.class, id));
    }

    @Override
    public List<GiftCertificate> getAll(
            int page, int total, Optional<List<String>> tagNames, Optional<String> namePart,
            Optional<String> descriptionPart, Optional<String> nameOrder, Optional<String> createDateOrder) {

        CriteriaBuilder                cb       = em.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> cq       = cb.createQuery(GiftCertificate.class);
        Root<GiftCertificate>          cqRootGc = cq.from(GiftCertificate.class);

        List<Predicate> predicates = new ArrayList<>();

        if (tagNames.isPresent()) {
            for (String tagName : tagNames.get()) {
                Subquery<Tag>         sq       = cq.subquery(Tag.class);
                Root<GiftCertificate> sqRootGc = sq.from(GiftCertificate.class);
                Join<GiftCertificate, Tag> sqJoinTag = sqRootGc.join("tags", JoinType.LEFT);

                predicates.add(cb.exists(sq.where(cb.and(cb.equal(cqRootGc.get("id"), sqRootGc.get("id")),
                        cb.equal(sqJoinTag.get("name"), tagName)))));
            }
        }

        namePart.ifPresent(s -> predicates.add(cb.like(cb.lower(cqRootGc.get("name")), '%' + s.toLowerCase() + '%')));
        descriptionPart.ifPresent(
                s -> predicates.add(cb.like(cb.lower(cqRootGc.get("description")), '%' + s.toLowerCase() + '%')));

        List<Order> orderList = new ArrayList<>();
        nameOrder.ifPresent(s -> orderList.add(s.equalsIgnoreCase("desc")
                ? cb.desc(cqRootGc.get("name"))
                : cb.asc(cqRootGc.get("name"))));
        createDateOrder.ifPresent(s -> orderList.add(s.equalsIgnoreCase("desc")
                ? cb.desc(cqRootGc.get("createdDate"))
                : cb.asc(cqRootGc.get("createdDate"))));
        orderList.add(cb.asc(cqRootGc.get("id")));

        cq.where(cb.and(predicates.toArray(Predicate[]::new))).orderBy(orderList);

        return em.createQuery(cq).setFirstResult((page - 1) * total).setMaxResults(total).getResultStream()
                .filter(gc -> gc.getCreatedDate().plusDays(gc.getDuration()).isAfter(LocalDateTime.now())).toList();
    }
}
