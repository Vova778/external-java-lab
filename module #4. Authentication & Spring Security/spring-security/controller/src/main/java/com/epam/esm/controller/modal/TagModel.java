package com.epam.esm.controller.modal;

import com.epam.esm.dto.TagDTO;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class TagModel extends RepresentationModel<TagModel> {

    @JsonUnwrapped
    private TagDTO tagDTO;
}