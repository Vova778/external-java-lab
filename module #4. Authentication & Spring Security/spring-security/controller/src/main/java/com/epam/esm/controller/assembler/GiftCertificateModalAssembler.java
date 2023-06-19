package com.epam.esm.controller.assembler;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.controller.modal.GiftCertificateModal;
import com.epam.esm.dto.GiftCertificateDTO;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GiftCertificateModalAssembler
        extends RepresentationModelAssemblerSupport<GiftCertificateDTO, GiftCertificateModal> {

    public static final String GET_REL = "get";
    public static final String CREATE_REL = "create";
    public static final String DELETE_REL = "delete";
    public static final String UPDATE_REL = "update";
    public static final String NEXT_REL = "next";
    public static final String PREVIOUS_REL = "previous";

    public GiftCertificateModalAssembler() {
        super(GiftCertificateController.class, GiftCertificateModal.class);
    }


    @Override
    public GiftCertificateModal toModel(GiftCertificateDTO entity) {
        GiftCertificateModal tagModal = new GiftCertificateModal(entity);

        Link getLink = linkTo(methodOn(GiftCertificateController.class).findById(entity.getId()))
                .withRel(GET_REL);
        Link updateLink = linkTo(methodOn(GiftCertificateController.class).update(null))
                .withRel(UPDATE_REL);
        Link createLink = linkTo(methodOn(GiftCertificateController.class).save(null))
                .withRel(CREATE_REL);
        Link deleteLink = linkTo(methodOn(GiftCertificateController.class).deleteById(entity.getId()))
                .withRel(DELETE_REL);

        tagModal.add(getLink,updateLink, createLink,deleteLink);
        return tagModal;
    }

    public CollectionModel<GiftCertificateModal> toCollectionModel(
            Iterable<? extends GiftCertificateDTO> entities, Integer page,
            Integer pageSize) {

        CollectionModel<GiftCertificateModal> collectionModel = super.toCollectionModel(entities);

        if (page != 0) {
            Link prevLink = linkTo(methodOn(GiftCertificateController.class)
                    .findAll(page - 1, pageSize))
                    .withRel(PREVIOUS_REL);
            collectionModel.add(prevLink);
        }
        if (collectionModel.getContent().size() >= pageSize) {
            Link nextLink = linkTo(methodOn(GiftCertificateController.class)
                    .findAll(page + 1, pageSize))
                    .withRel(NEXT_REL);
            collectionModel.add(nextLink);
        }
        return collectionModel;
    }


}