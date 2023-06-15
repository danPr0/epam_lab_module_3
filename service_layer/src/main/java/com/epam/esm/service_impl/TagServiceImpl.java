package com.epam.esm.service_impl;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.TagService;
import com.epam.esm.util_service.DTOUtil;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementation of service interface {@link TagService}.
 *
 * @author Danylo Proshyn
 */

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    private final Logger logger = LogManager.getLogger(TagServiceImpl.class);

    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {

        this.tagRepository = tagRepository;
    }

    @Override
    public boolean addTag(@Valid TagDTO tag) {

        try {
            tagRepository.insertEntity(DTOUtil.convertToEntity(tag));
        } catch (DataAccessException e) {
            logger.error(e);

            return false;
        }

        return true;
    }

    @Override
    public Optional<TagDTO> getTag(long id) {

        return tagRepository.getEntity(id).map(DTOUtil::convertToDTO);
    }

    @Override
    public Optional<TagDTO> getMostPopularUserTag() {

        return tagRepository.getMostPopularEntity().map(DTOUtil::convertToDTO);
    }

    @Override
    public boolean deleteTag(long id) {

        try {
            tagRepository.deleteEntity(id);
        } catch (Exception e) {
            logger.error(e);

            return false;
        }

        return true;
    }
}
