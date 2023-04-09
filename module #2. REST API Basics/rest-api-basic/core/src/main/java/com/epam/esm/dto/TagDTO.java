package com.epam.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagDTO {
    private Long id;

    @NotBlank
    @NonNull
    private String name;
}
