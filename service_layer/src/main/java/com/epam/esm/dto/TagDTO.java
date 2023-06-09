package com.epam.esm.dto;

import com.epam.esm.entity.Tag;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

/**
 * DTO class for {@link Tag} entity.
 *
 * @author Danylo Proshyn
 */

@Relation(collectionRelation = "tags")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Builder
public class TagDTO extends RepresentationModel<TagDTO> {

    private long   id;
    private String name;
}
