package com.epam.esm.utils;

import lombok.Builder;
import lombok.Data;

/**
 * This class contains search and sort parameters to construct
 * database queries in {@link QueryProvider} class.
 * .
 */
@Data
@Builder
public class QueryParameters {
    private String tagName;
    private String name;
    private String description;
    private String sortByName;
    private String sortByDate;
}
