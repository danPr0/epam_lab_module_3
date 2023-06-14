package com.epam.esm.controller;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.rest.AddTagRequest;
import com.epam.esm.service.TagService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * Controller class responsible for operations with tags.
 *
 * @author Danylo Proshyn
 */

@RestController()
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;

    private final String resourceCode = "02";

    @Autowired
    public TagController(TagService tagService) {

        this.tagService = tagService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getTag(@PathVariable long id) {

        Optional<TagDTO> tag = tagService.getTag(id);

        if (tag.isPresent()) {
            tag.get().add(linkTo(methodOn(TagController.class).getTag(id)).withSelfRel());

            return ResponseEntity.ok(tag.get());
        } else {
            return ResponseEntity.status(404)
                    .body(Map.of("errorMessage", String.format("Requested resource not found (id = %s)", id),
                            "errorCode", "404" + resourceCode));
        }
    }

    @GetMapping("/most-popular-tag")
    public ResponseEntity<Object> getMostPopularUserTag() {

        Optional<TagDTO> tag = tagService.getMostPopularUserTag();

        if (tag.isPresent()) {
            tag.get().add(linkTo(methodOn(TagController.class).getMostPopularUserTag()).withSelfRel());

            return ResponseEntity.ok(tag.get());
        } else {
            return ResponseEntity.status(404)
                    .body(Map.of("errorMessage", "Resource not found",
                            "errorCode", "404" + resourceCode));
        }
    }

    @PostMapping()
    public ResponseEntity<Object> addTag(@Valid @RequestBody AddTagRequest req) {

        TagDTO tagDTO = TagDTO.builder().id(req.getId()).name(req.getName()).build();
        if (tagService.addTag(tagDTO)) {
            tagDTO.add(linkTo(methodOn(TagController.class).getTag(tagDTO.getId())).withSelfRel());

            return ResponseEntity.status(201).body(tagDTO);
        } else {
            return ResponseEntity.status(409)
                    .body(Map.of("errorMessage", String.format("Resource already exists (id = %s)", req.getId()),
                            "errorCode", "409" + resourceCode));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTag(@PathVariable long id) {

        if (tagService.deleteTag(id)) {
            return ResponseEntity.status(204).build();
        } else {
            return ResponseEntity.status(404)
                    .body(Map.of("errorMessage", String.format("Requested resource not found (id = %s)", id),
                            "errorCode", "404" + resourceCode));
        }
    }
}
