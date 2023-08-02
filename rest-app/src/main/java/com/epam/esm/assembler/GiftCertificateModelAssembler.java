package com.epam.esm.assembler;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.modal.GiftCertificateModel;
import com.epam.esm.dto.GiftCertificateDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GiftCertificateModelAssembler
        extends RepresentationModelAssemblerSupport<GiftCertificateDTO, GiftCertificateModel> {

    public static final String UPDATE_REL = "update";
    public static final String DELETE_REL = "delete";
    public static final String TAGS_REL = "tags";
    private static final int PAGE = 0;
    private static final int SIZE = 2;

    public GiftCertificateModelAssembler() {
        super(GiftCertificateController.class, GiftCertificateModel.class);
    }

    /**
     * Converts the given entity into a {@code GiftCertificateModel}, which extends {@link RepresentationModel}.
     *
     * @param entity of GiftCertificateDTO class  which will be converted to representation model
     * @return GiftCertificateModel object
     */
    @Override
    public GiftCertificateModel toModel(GiftCertificateDTO entity) {
        GiftCertificateModel certificateModel = new GiftCertificateModel(entity);

        certificateModel.add(
                linkTo(
                        methodOn(GiftCertificateController.class)
                                .findByID(entity.getId()))
                        .withSelfRel(),
                linkTo(
                        methodOn(GiftCertificateController.class)
                                .findTagsByCertificate(entity.getId(), PageRequest.of(PAGE, SIZE)))
                        .withRel(TAGS_REL),
                linkTo(
                        methodOn(GiftCertificateController.class)
                                .update(entity)).withRel(UPDATE_REL),
                linkTo(
                        methodOn(GiftCertificateController.class)
                                .deleteByID(entity.getId()))
                        .withRel(DELETE_REL));

        return certificateModel;
    }


}