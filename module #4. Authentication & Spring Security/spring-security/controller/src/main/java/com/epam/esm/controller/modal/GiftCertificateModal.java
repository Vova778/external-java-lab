package com.epam.esm.controller.modal;

import com.epam.esm.dto.GiftCertificateDTO;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class GiftCertificateModal extends RepresentationModel<GiftCertificateModal> {

    @JsonUnwrapped
    private GiftCertificateDTO giftCertificateDTO;
}
