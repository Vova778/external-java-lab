package com.epam.esm.controller.assembler;

import com.epam.esm.controller.UserController;
import com.epam.esm.controller.modal.UserModal;
import com.epam.esm.dto.UserDTO;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserModalAssembler extends RepresentationModelAssemblerSupport<UserDTO, UserModal> {
    public static final String NEXT_REL = "next";
    public static final String PREVIOUS_REL = "previous";

    public UserModalAssembler() {
        super(UserController.class, UserModal.class);
    }


    @Override
    public UserModal toModel(UserDTO entity) {
        return new UserModal(entity);
    }

    public CollectionModel<UserModal> toCollectionModel(
            Iterable<? extends UserDTO> entities, Integer page,
            Integer pageSize) {
        CollectionModel<UserModal> collectionModel = super.toCollectionModel(entities);

        if (page != 0) {
            Link prevLink = linkTo(methodOn(UserController.class)
                    .findAll(page - 1, pageSize))
                    .withRel(PREVIOUS_REL);
            collectionModel.add(prevLink);
        }
        if (collectionModel.getContent().size() >= pageSize) {
            Link nextLink = linkTo(methodOn(UserController.class)
                    .findAll(page + 1, pageSize))
                    .withRel(NEXT_REL);
            collectionModel.add(nextLink);
        }
        return collectionModel;
    }

}
