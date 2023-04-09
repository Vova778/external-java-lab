package com.epam.esm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Tag {
    private Long id;

    @NotBlank
    @NonNull
    private String name;
}
