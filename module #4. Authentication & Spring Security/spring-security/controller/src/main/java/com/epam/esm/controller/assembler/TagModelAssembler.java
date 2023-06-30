package com.epam.esm.controller.assembler;

import com.epam.esm.controller.TagController;
import com.epam.esm.controller.modal.TagModel;
import com.epam.esm.dto.TagDTO;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TagModelAssembler extends RepresentationModelAssemblerSupport<TagDTO, TagModel> {
    public static final String DELETE_REL = "delete";

    public TagModelAssembler() {
        super(TagController.class, TagModel.class);
    }

    /**
     * Converts the given entity into a {@code TagModel}, which extends {@link RepresentationModel}.
     *
     * @param entity of TagDTO class  which will be converted to representation model
     * @return TagModel object
     */
    @Override
    public TagModel toModel(TagDTO entity) {
        TagModel tagModel = new TagModel(entity);

        tagModel.add(linkTo(
                        methodOn(TagController.class)
                                .findByID(entity.getId()))
                        .withSelfRel(),
                linkTo(
                        methodOn(TagController.class)
                                .deleteByID(entity.getId()))
                        .withRel(DELETE_REL));

        return tagModel;
    }

}