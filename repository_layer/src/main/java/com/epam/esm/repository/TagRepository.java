package com.epam.esm.repository;

import com.epam.esm.entity.Tag;

import java.util.Optional;

/**
 * DAO class for {@link Tag} entity.
 *
 * @author Danylo Proshyn
 */

public interface TagRepository {

    void insertEntity(Tag tag);

    Optional<Tag> getEntity(Long id);

    /**
     *
     * @return the most widely used tag of a user with the highest cost of all orders
     */
    Optional<Tag> getMostPopularEntity();

    void deleteEntity(Long id);
}
