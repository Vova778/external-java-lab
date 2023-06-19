package com.epam.esm.utils;

import com.epam.esm.model.entity.GiftCertificate;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * This class implements functionality of creating
 * database queries. Also query can be created
 * in according to {@link QueryParameters} parameters received from
 * GiftCertificateController class
 */
@Slf4j
@NoArgsConstructor
@Component
@Setter
public class QueryProvider {
    private QueryParameters queryParams;
    private static final String QUERY_PROVIDER = "[QueryProvider]";
    private static final String SELECT = "SELECT ";
    private static final String DISTINCT = " DISTINCT ";
    private static final String DESC = " DESC";
    private static final String WHERE = " WHERE ";
    private static final String AND = " AND ";
    private static final String ORDER_BY = " ORDER BY ";
    private static final String LIKE = " LIKE ";
    private static final String FRONT_PERCENT_SIGN = "'%";
    private static final String BACK_PERCENT_SIGN = "%'";
    private static final String CERTIFICATE_FIELDS = "gift_certificate.id, gift_certificate.name," +
            " gift_certificate.description, gift_certificate.price, gift_certificate.duration," +
            " gift_certificate.create_date, gift_certificate.last_update_date ";
    private static final String FROM_GIFT_CERTIFICATE = "FROM external_lab.gift_certificate";
    private static final String CERTIFICATE_NAME = "gift_certificate.name";
    private static final String DESCRIPTION = "gift_certificate.description";
    private static final String TAG_NAME = "tag.name";
    private static final String DATE = "gift_certificate.create_date";

    private static final String JOIN_TAG_HAS_CERTIFICATE = " LEFT JOIN external_lab.gift_certificate_has_tag on" +
            " gift_certificate.id = external_lab.gift_certificate_has_tag.gift_certificate_id";
    private static final String JOIN_TAG = " LEFT JOIN external_lab.tag on tag.id = external_lab.gift_certificate_has_tag.tag_id";

    /**
     * This method implements functionality of constructing
     * database query based on {@link QueryParameters} fields values.
     * Result query will use to search and sort {@link GiftCertificate} objects
     * with search by received parameters option.
     *
     * @return ready to use database query
     */
    public String findAllWithParams() {
        StringBuilder sb = new StringBuilder().append(SELECT).append(DISTINCT).append(CERTIFICATE_FIELDS)
                .append(FROM_GIFT_CERTIFICATE);
        if (queryParams.getTagName() != null && !queryParams.getTagName().isEmpty()) {
            sb.append(JOIN_TAG_HAS_CERTIFICATE).append(JOIN_TAG).append(WHERE).append(TAG_NAME)
                    .append(LIKE).append(FRONT_PERCENT_SIGN).append(queryParams.getTagName()).append(BACK_PERCENT_SIGN);
            getQueryDependsOnParams(sb, AND);
        } else {
            getQueryDependsOnParams(sb, WHERE);
        }
        log.warn("QUERY: {}", sb);
        return sb.toString();
    }

    /**
     * This method implements functionality of adding necessary
     * search operations to database query. Depends on values of {@link QueryParameters#getTagName()}
     * , {@link QueryParameters#getName()} ()} and {@link QueryParameters#getDescription()}
     * parameters received from GiftCertificateController
     *
     * @param sb constructed query from {@link #findAllWithParams()}  method.
     */
    private void getQueryDependsOnParams(StringBuilder sb, String statement) {
        if ((queryParams.getName() != null && !queryParams.getName().isEmpty())
                && (queryParams.getDescription() != null && !queryParams.getDescription().isEmpty())) {
            sb.append(statement).append(CERTIFICATE_NAME).append(LIKE).append(FRONT_PERCENT_SIGN)
                    .append(queryParams.getName()).append(BACK_PERCENT_SIGN)
                    .append(AND).append(DESCRIPTION).append(LIKE).append(FRONT_PERCENT_SIGN)
                    .append(queryParams.getDescription()).append(BACK_PERCENT_SIGN);
            getSortedByParam(sb);
        } else if ((queryParams.getName() != null && !queryParams.getName().isEmpty())
                && (queryParams.getDescription() == null || queryParams.getDescription().isEmpty())) {
            sb.append(statement).append(CERTIFICATE_NAME).append(LIKE).append(FRONT_PERCENT_SIGN)
                    .append(queryParams.getName())
                    .append(BACK_PERCENT_SIGN);
            getSortedByParam(sb);
        } else if ((queryParams.getDescription() != null && !queryParams.getDescription().isEmpty())
                && (queryParams.getName() == null || queryParams.getName().isEmpty())) {
            sb.append(statement).append(DESCRIPTION).append(LIKE).append(FRONT_PERCENT_SIGN)
                    .append(queryParams.getDescription()).append(BACK_PERCENT_SIGN);
            getSortedByParam(sb);
        } else {
            getSortedByParam(sb);
        }
    }

    /**
     * This method implements functionality of adding necessary
     * sorting operations to database query. Depends on values of {@link QueryParameters#getSortByDate()}
     * and {@link QueryParameters#getSortByName()} parameters received from GiftCertificateController
     *
     * @param sb constructed query from {@link #getQueryDependsOnParams(StringBuilder sb, String statement)} method.
     */
    private void getSortedByParam(StringBuilder sb) {
        if ((queryParams.getSortByName() != null && !queryParams.getSortByName().isEmpty())
                && (queryParams.getSortByDate() == null || queryParams.getSortByDate().isEmpty())) {
            sb.append(ORDER_BY).append(CERTIFICATE_NAME);
            if (queryParams.getSortByName().equalsIgnoreCase(DESC.trim()))
                sb.append(DESC);
        } else if ((queryParams.getSortByDate() != null && !queryParams.getSortByDate().isEmpty())
                && queryParams.getSortByName() == null || queryParams.getSortByName().isEmpty()) {
            sb.append(ORDER_BY).append(DATE);
            if (queryParams.getSortByDate().equalsIgnoreCase(DESC.trim()))
                sb.append(DESC);
        }
    }
}