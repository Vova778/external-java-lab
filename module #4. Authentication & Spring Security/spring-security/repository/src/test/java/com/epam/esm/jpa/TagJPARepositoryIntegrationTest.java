package com.epam.esm.jpa;


import com.epam.esm.TagRepository;
import com.epam.esm.configuration.JPAConfig;
import com.epam.esm.model.entity.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;

@DataJpaTest
@ContextConfiguration(classes = {JPAConfig.class, TagRepository.class})
@ActiveProfiles("default")
class TagJPARepositoryIntegrationTest {
    @Autowired
    private TagRepository tagRepository;

    @Sql(scripts = {"/schema-h2.sql", "/data-h2.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    void isExists() {
        //given
        Tag existedTag = new Tag("c");
        Tag notExistedTag = new Tag("blah-blah");
        //when
        boolean isExistsExpectedTrue = tagRepository.existsByName(existedTag.getName());
        boolean isExistsExpectedFalse = tagRepository.existsByName(notExistedTag.getName());
        //then
        then(isExistsExpectedTrue).isTrue();
        then(isExistsExpectedFalse).isFalse();
    }

    @Sql(scripts = "/schema-h2.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    void save() {
        //given
        Long generatedID = 1L;
        String name = "test1";
        //when
        Tag savedTag = tagRepository.save(new Tag(name));
        //then
        then(savedTag).isNotNull();
        then(savedTag.getId()).isEqualTo(generatedID);
        then(savedTag.getName()).isEqualTo(name);
    }

    @Sql(scripts = {"/schema-h2.sql", "/data-h2.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    void findByID() {
        //given
        Long id = 4L;
        String expectedName = "c-sharp";
        //when
        Optional<Tag> tagOptional = tagRepository.findById(id);
        Tag tag = null;
        if (tagOptional.isPresent()) tag = tagOptional.get();
        //then
        then(tag).isNotNull();
        then(tag.getId()).isEqualTo(id);
        then(tag.getName()).isEqualTo(expectedName);
    }

    @Sql(scripts = {"/schema-h2.sql", "/data-h2.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    void findByName() {
        //given
        Long expectedID = 1L;
        String expectedName = "java";
        //when
        Optional<Tag> tagOptional = tagRepository.findByName(expectedName);
        Tag tag = null;
        if (tagOptional.isPresent()) tag = tagOptional.get();
        //then
        then(tag).isNotNull();
        then(tag.getId()).isEqualTo(expectedID);
        then(tag.getName()).isEqualTo(expectedName);
    }

    @Sql(scripts = {"/schema-h2.sql", "/data-h2.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    void findAll() {
        //given
        Pageable pageableDefault = PageRequest.of(0, 10);
        Pageable pageableCustom = PageRequest.of(0, 4);
        //when
        List<Tag> defaultList = tagRepository.findAll(pageableDefault).toList();
        List<Tag> customList = tagRepository.findAll(pageableCustom).toList();
        //then
        then(defaultList.size()).isEqualTo(6);
        then(customList.size()).isEqualTo(4);
    }

    @Sql(scripts = {"/schema-h2.sql", "/data-h2.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    void findAllByCertificate() {
        //given
        Long certificateID = 4L;
        Pageable pageable = PageRequest.of(0, 10);
        //when
        List<Tag> tagsByCertificateID = tagRepository.findAllByCertificate(certificateID, pageable);
        //then
        then(tagsByCertificateID.size()).isEqualTo(1);
        then(tagsByCertificateID).isEqualTo(getAllForCertificateIDEqualTo4());
    }


    @Sql(scripts = {"/schema-h2.sql", "/data-h2.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    void getTotalRecords() {
        //given
        Long expectedRecords = 6L;
        //when
        Long actual = tagRepository.count();
        //then
        then(actual).isEqualTo(expectedRecords);
    }

    @Sql(scripts = {"/schema-h2.sql", "/data-h2.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    void findMostWidelyUsedTagOfUserWithHighestCostOfAllReceipts() {
        //given
        Tag expected = new Tag(4L, "c-sharp", new HashSet<>());
        Tag tag = null;
        //when
        Optional<Tag> result = tagRepository.findMostWidelyUsedTagOfUserWithHighestCostOfAllOrders();
        if (result.isPresent()) tag = result.get();
        //then
        then(tag).isNotNull();
        then(tag).isEqualTo(expected);
    }

    @Sql(scripts = {"/schema-h2.sql", "/data-h2.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    void getTotalRecordsForGiftCertificateID() {
        //given
        Long certificateID = 3L;
        Long expectedRecords = 2L;
        //when
        Long actualRecords = tagRepository.getTotalRecordsForGiftCertificateID(certificateID);
        //then
        then(actualRecords).isEqualTo(expectedRecords);
    }

    private List<Tag> getAll() {
        return List.of(
                new Tag(1L, "java", new HashSet<>()),
                new Tag(2L, "scala", new HashSet<>()),
                new Tag(3L, "c", new HashSet<>()),
                new Tag(4L, "c-sharp", new HashSet<>()),
                new Tag(5L, "kotlin", new HashSet<>()),
                new Tag(6L, "visual basic", new HashSet<>()));
    }

    private List<Tag> getAllWithPageParams() {
        return List.of(
                new Tag(1L, "java", new HashSet<>()),
                new Tag(2L, "scala", new HashSet<>()),
                new Tag(3L, "c", new HashSet<>()),
                new Tag(4L, "c-sharp", new HashSet<>()));
    }

    private List<Tag> getAllForCertificateIDEqualTo4() {
        return List.of(
                new Tag(5L, "kotlin", new HashSet<>()));
    }
}
