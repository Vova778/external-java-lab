package com.epam.esm.controller.assembler;

import com.epam.esm.controller.ReceiptController;
import com.epam.esm.controller.modal.ReceiptModal;
import com.epam.esm.dto.ReceiptDTO;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.HashSet;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ReceiptModalAssembler extends RepresentationModelAssemblerSupport<ReceiptDTO, ReceiptModal> {

    public static final String GET_REL = "get";
    public static final String CREATE_REL = "create";
    public static final String DELETE_REL = "delete";
    public static final String UPDATE_REL = "update";
    public static final String NEXT_REL = "next";
    public static final String PREVIOUS_REL = "previous";

    public ReceiptModalAssembler() {
        super(ReceiptController.class, ReceiptModal.class);
    }


    @Override
    public ReceiptModal toModel(ReceiptDTO entity) {
        ReceiptModal receiptModal = new ReceiptModal(entity);

        Link getLink = linkTo(methodOn(ReceiptController.class).findById(entity.getId()))
                .withRel(GET_REL);
        Link createLink = linkTo(methodOn(ReceiptController.class).save(null, new HashSet<>()))
                .withRel(CREATE_REL);

        receiptModal.add(getLink,createLink);
        return receiptModal;
    }

    public CollectionModel<ReceiptModal> toCollectionModel(
            Iterable<? extends ReceiptDTO> entities, Integer page,
            Integer pageSize) {

        CollectionModel<ReceiptModal> collectionModel = super.toCollectionModel(entities);

        if (page != 0) {
            Link prevLink = linkTo(methodOn(ReceiptController.class)
                    .findAll(page - 1, pageSize))
                    .withRel(PREVIOUS_REL);
            collectionModel.add(prevLink);
        }
        if (collectionModel.getContent().size() >= pageSize) {
            Link nextLink = linkTo(methodOn(ReceiptController.class)
                    .findAll(page + 1, pageSize))
                    .withRel(NEXT_REL);
            collectionModel.add(nextLink);
        }
        return collectionModel;
    }


}
