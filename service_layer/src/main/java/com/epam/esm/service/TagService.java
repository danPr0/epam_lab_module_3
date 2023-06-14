package com.epam.esm.service;

import com.epam.esm.dto.TagDTO;

import java.util.Optional;

/**
 * Service class for {@link TagDTO}.
 *
 * @author Danylo Proshyn
 */

public interface TagService {

    boolean addTag(TagDTO tag);

    Optional<TagDTO> getTag(long id);

    Optional<TagDTO> getMostPopularUserTag();

    boolean deleteTag(long id);
}
