package com.epam.esm.controller.modal;

import com.epam.esm.dto.UserDTO;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class UserModal extends RepresentationModel<UserModal> {

    @JsonUnwrapped
    private UserDTO userDTO;
}
