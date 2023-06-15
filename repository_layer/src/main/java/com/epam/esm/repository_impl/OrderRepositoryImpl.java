package com.epam.esm.repository_impl;

import com.epam.esm.entity.Order;
import com.epam.esm.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of DAO Interface {@link OrderRepository}.
 *
 * @author Danylo Proshyn
 */

@Repository
@Transactional
public class OrderRepositoryImpl implements OrderRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Order insertEntity(Order order) {

        em.persist(order);

        return em.find(Order.class, new Order.OrderIdClass(order.getUser(), order.getGiftCertificate()));
    }

    @Override
    public List<Order> getEntitiesByUser(Long userId) {

        TypedQuery<Order> query = em.createQuery("select o from Order o where o.user.id = :userId", Order.class);
        query.setParameter("userId", userId);

        return query.getResultList();
    }
}
