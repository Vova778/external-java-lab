package com.epam.esm.controller.assembler;

import com.epam.esm.controller.TagController;
import com.epam.esm.controller.modal.TagModal;
import com.epam.esm.dto.TagDTO;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TagModalAssembler extends RepresentationModelAssemblerSupport<TagDTO, TagModal> {

    public static final String GET_REL = "get";
    public static final String CREATE_REL = "create";
    public static final String DELETE_REL = "delete";
    public static final String UPDATE_REL = "update";
    public static final String NEXT_REL = "next";
    public static final String PREVIOUS_REL = "previous";

    public TagModalAssembler() {
        super(TagController.class, TagModal.class);
    }


    @Override
    public TagModal toModel(TagDTO entity) {
        TagModal tagModal = new TagModal(entity);

        Link getLink = linkTo(methodOn(TagController.class).findById(entity.getId()))
                .withRel(GET_REL);
        Link createLink = linkTo(methodOn(TagController.class).save(null))
                .withRel(CREATE_REL);
        Link deleteLink = linkTo(methodOn(TagController.class).deleteById(entity.getId()))
                .withRel(DELETE_REL);

        tagModal.add(getLink,createLink,deleteLink);
        return tagModal;
    }

    public CollectionModel<TagModal> toCollectionModel(
            Iterable<? extends TagDTO> entities, Integer page,
            Integer pageSize) {

        CollectionModel<TagModal> collectionModel = super.toCollectionModel(entities);

        if (page != 0) {
            Link prevLink = linkTo(methodOn(TagController.class)
                    .findAll(page - 1, pageSize))
                    .withRel(PREVIOUS_REL);
            collectionModel.add(prevLink);
        }
        if (collectionModel.getContent().size() >= pageSize) {
            Link nextLink = linkTo(methodOn(TagController.class)
                    .findAll(page + 1, pageSize))
                    .withRel(NEXT_REL);
            collectionModel.add(nextLink);
        }
        return collectionModel;
    }


}