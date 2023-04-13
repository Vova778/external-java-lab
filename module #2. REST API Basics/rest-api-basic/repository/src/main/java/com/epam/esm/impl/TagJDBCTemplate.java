package com.epam.esm.impl;


import com.epam.esm.TagRepository;
import com.epam.esm.model.Tag;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class TagJDBCTemplate implements TagRepository {
    private static final String INSERT = "INSERT INTO external_lab_module_2.tag (name) VALUES (?)";
    private static final String FIND_BY_ID = "SELECT * FROM external_lab_module_2.tag WHERE id = ?";
    private static final String FIND_BY_NAME = "SELECT * FROM external_lab_module_2.tag WHERE name = ?";
    private static final String FIND_ALL = "SELECT * FROM external_lab_module_2.tag";
    private static final String DELETE = "DELETE FROM external_lab_module_2.tag WHERE id = ?";
    private static final String FIND_ALL_BY_NAME = "SELECT * FROM external_lab_module_2.tag WHERE name LIKE ?";
    private static final String FIND_ALL_BY_CERTIFICATE = "SELECT external_lab_module_2.tag.id, external_lab_module_2.tag.name FROM external_lab_module_2.tag LEFT JOIN" +
            " tag_has_gift_certificate on tag.id = tag_has_gift_certificate.tag_id WHERE gift_certificate_id = ?";

    private final JdbcTemplate jdbcTemplate;

    public TagJDBCTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean isExists(Tag tag) {
        return false;
    }

    public Long save(Tag tag) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps =
                    connection.prepareStatement(INSERT, new String[]{"id"});
            ps.setString(1, tag.getName());
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public Optional<Tag> findById(Long id) {
        Optional<Tag> optionalTag;
        try {
            optionalTag = Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_ID,
                    new BeanPropertyRowMapper<>(Tag.class), id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
        return optionalTag;
    }


    public Optional<Tag> findByName(String name) {
        Optional<Tag> optionalTag;
        try {
            optionalTag = Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_NAME,
                    new BeanPropertyRowMapper<>(Tag.class), name));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
        return optionalTag;
    }

    @Override
    public Optional<List<Tag>> findAllByName(String name) {
        Optional<List<Tag>> optionalTags;
        try {
            optionalTags = Optional.of(jdbcTemplate.query(FIND_ALL_BY_NAME,
                    new BeanPropertyRowMapper<>(Tag.class), "%" + name + "%"));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
        return optionalTags;
    }

    public Optional<List<Tag>> findAllByCertificate(Long certificateId) {
        Optional<List<Tag>> optionalTags;
        try {
            optionalTags = Optional.of(jdbcTemplate.query(FIND_ALL_BY_CERTIFICATE,
                    new BeanPropertyRowMapper<>(Tag.class), certificateId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
        return optionalTags;
    }


    public Optional<List<Tag>> findAll() {
        return Optional.of(jdbcTemplate.query(FIND_ALL, new BeanPropertyRowMapper<>(Tag.class)));
    }

    public void deleteById(Long id) {
        jdbcTemplate.update(DELETE, id);
    }
}
