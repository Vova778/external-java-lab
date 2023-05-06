package com.epam.esm.impl;

import com.epam.esm.GiftCertificateRepository;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.utils.QueryParameters;
import com.epam.esm.utils.QueryProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GiftCertificateJDBCTemplate implements GiftCertificateRepository {

    private final JdbcTemplate jdbcTemplate;
    private final QueryProvider queryProvider;

    public void attachTagToCertificate(Long tagId, Long certificateId) {
        if (tagId != null && certificateId != null) {
            jdbcTemplate.update(queryProvider.attachCertificateToTag(), tagId, certificateId);
        }
    }

    @Override
    public boolean isExists(GiftCertificate giftCertificate) {
        Optional<GiftCertificate> certificate;
        try {
            certificate = Optional.ofNullable(jdbcTemplate.queryForObject(queryProvider.isExists(),
                    new BeanPropertyRowMapper<>(GiftCertificate.class), giftCertificate.getName()));
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
        return certificate.isPresent();
    }

    @Override
    public Long save(GiftCertificate giftCertificate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(queryProvider.insert(), new String[]{"id"});
            ps.setString(1, giftCertificate.getName());
            ps.setString(2, giftCertificate.getDescription());
            ps.setBigDecimal(3, BigDecimal.valueOf(giftCertificate.getPrice()));
            ps.setInt(4, giftCertificate.getDuration());
            ps.setTimestamp(5, Timestamp.valueOf(giftCertificate.getCreateDate()));
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public Optional<GiftCertificate> findById(Long id) {
        Optional<GiftCertificate> certificate;
        try {
            certificate = Optional.ofNullable(jdbcTemplate.queryForObject(queryProvider.findById(),
                    new BeanPropertyRowMapper<>(GiftCertificate.class), id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
        return certificate;
    }

    public Optional<List<GiftCertificate>> findAllByName(String name) {
        return getGiftCertificates(name, queryProvider.findAllByName());
    }

    @Override
    public Optional<List<GiftCertificate>> findAll() {
        return Optional.of(jdbcTemplate.query(queryProvider.findAll(),
                new BeanPropertyRowMapper<>(GiftCertificate.class)));
    }

    @Override
    public Optional<List<GiftCertificate>> findAllWithParams(QueryParameters queryParameters) {
        queryProvider.setQueryParameters(queryParameters);
        return Optional.of(jdbcTemplate.query(queryProvider.findAllWithParams()
                , new BeanPropertyRowMapper<>(GiftCertificate.class)));
    }

    @Override
    public void update(GiftCertificate giftCertificate) {
        jdbcTemplate.update(queryProvider.update(giftCertificate));
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(queryProvider.delete(), id);
    }

    private Optional<List<GiftCertificate>> getGiftCertificates(String option, String query) {
        Optional<List<GiftCertificate>> certificates;
        try {
            certificates = Optional.of(jdbcTemplate.query(query,
                    new BeanPropertyRowMapper<>(GiftCertificate.class), "%" + option + "%"));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
        return certificates;
    }
}

