package com.epam.esm.assembler;

import com.epam.esm.controller.ReceiptController;
import com.epam.esm.modal.ReceiptModel;
import com.epam.esm.dto.ReceiptDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class ReceiptModelAssembler extends RepresentationModelAssemblerSupport<ReceiptDTO, ReceiptModel> {

    public static final String DELETE_REL = "delete";
    public static final String USER_REL = "user";
    public static final String CERTIFICATES_REL = "gift certificates";
    private static final int PAGE = 0;
    private static final int SIZE = 10;

    public ReceiptModelAssembler() {
        super(ReceiptController.class, ReceiptModel.class);
    }

    /**
     * Converts the given entity into a {@code ReceiptModel}, which extends {@link RepresentationModel}.
     *
     * @param entity of ReceiptDTO class  which will be converted to representation model
     * @return ReceiptModel object
     */
    @Override
    public ReceiptModel toModel(ReceiptDTO entity) {
        ReceiptModel receiptModel = new ReceiptModel(entity);

        receiptModel.add(
                linkTo(
                        methodOn(ReceiptController.class)
                                .findByID(entity.getId()))
                        .withSelfRel(),
                linkTo(
                        methodOn(ReceiptController.class)
                                .findUserByReceipt(entity.getId()))
                        .withRel(USER_REL),
                linkTo(
                        methodOn(ReceiptController.class)
                                .findGiftCertificatesByReceipt(entity.getId(), PageRequest.of(PAGE,SIZE)))
                        .withRel(CERTIFICATES_REL),
                linkTo(
                        methodOn(ReceiptController.class)
                                .deleteByID(entity.getId()))
                        .withRel(DELETE_REL));

        return receiptModel;
    }

}
