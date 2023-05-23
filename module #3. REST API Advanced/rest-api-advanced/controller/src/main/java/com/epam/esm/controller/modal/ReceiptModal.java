package com.epam.esm.controller.modal;

import com.epam.esm.dto.ReceiptDTO;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class ReceiptModal extends RepresentationModel<TagModal> {

    @JsonUnwrapped
    private ReceiptDTO receiptDTO;
}
