package com.epam.esm.impl;

import com.epam.esm.GiftCertificateRepository;
import com.epam.esm.model.GiftCertificate;
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
public class GiftCertificateJDBCTemplate implements GiftCertificateRepository {
    private static final String INSERT = " INSERT INTO external_lab_module_2.gift_certificate " +
            "(name, description, price, duration, create_date) VALUES (?, ?, ?, ?, ?)";
    private static final String FIND_BY_ID = "SELECT * FROM external_lab_module_2.gift_certificate WHERE id = ?";
    private static final String FIND_BY_NAME = "SELECT * FROM external_lab_module_2.gift_certificate WHERE name = ?";
    private static final String FIND_ALL = "SELECT * FROM external_lab_module_2.gift_certificate";
    private static final String UPDATE = "UPDATE external_lab_module_2.gift_certificate SET name = ?, description = ?,"
            + "price = ?, duration = ?, last_update_date = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM external_lab_module_2.gift_certificate WHERE id = ?";

    private final JdbcTemplate jdbcTemplate;

    public GiftCertificateJDBCTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public boolean isExists(GiftCertificate giftCertificate) {
        return false;
    }


    public Long save(GiftCertificate giftCertificate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT, new String[]{"id"});
            ps.setString(1, giftCertificate.getName());
            ps.setString(2, giftCertificate.getDescription());
            ps.setBigDecimal(3, BigDecimal.valueOf(giftCertificate.getPrice()));
            ps.setInt(4, giftCertificate.getDuration());
            ps.setTimestamp(5, Timestamp.valueOf(giftCertificate.getCreateDate()));
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public Optional<GiftCertificate> findById(Long id) {
        Optional<GiftCertificate> giftCertificate;
        try {
            giftCertificate = Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_ID,
                    new BeanPropertyRowMapper<>(GiftCertificate.class), id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
        return giftCertificate;
    }

    @Override
    public Optional<List<GiftCertificate>> findAllByName(String name) {
        return Optional.empty();
    }

    public Optional<GiftCertificate> findByName(String name) {
        Optional<GiftCertificate> giftCertificate;
        try {
            giftCertificate = Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_NAME,
                    new BeanPropertyRowMapper<>(GiftCertificate.class), name));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
        return giftCertificate;
    }

    public Optional<List<GiftCertificate>> findAll() {
        return Optional.of(jdbcTemplate.query(FIND_ALL, new BeanPropertyRowMapper<>(GiftCertificate.class)));
    }

    public void update( GiftCertificate giftCertificate) {
        jdbcTemplate.update(UPDATE, giftCertificate.getName(), giftCertificate.getDescription(),
                giftCertificate.getPrice(), giftCertificate.getDuration(), giftCertificate.getLastUpdateDate(),
                giftCertificate.getId());
    }

    public void deleteById(Long id) {
        jdbcTemplate.update(DELETE, id);
    }

    @Override
    public void attachTagToCertificate(Long tagId, Long certificateId) {

    }
}
