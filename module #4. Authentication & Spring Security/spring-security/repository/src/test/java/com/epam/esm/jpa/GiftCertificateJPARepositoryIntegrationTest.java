package com.epam.esm.jpa;

import com.epam.esm.GiftCertificateRepository;
import com.epam.esm.configuration.JPAConfig;
import com.epam.esm.model.entity.GiftCertificate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;

@DataJpaTest
@ContextConfiguration(classes = {JPAConfig.class, GiftCertificateRepository.class})
@ActiveProfiles("default")
class GiftCertificateJPARepositoryIntegrationTest {

    @Autowired
    private GiftCertificateRepository giftCertificateRepository;

    @Sql(scripts = {"/schema-h2.sql", "/data-h2.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    void isExists() {
        //given
        String existedTitle = "microsoft";
        String existedDescription = "monopoly";
        String existedDate = "2023-03-23T15:59:05.284";
        Double existedPrice = 155.0;
        Integer existedDuration = 2;
        GiftCertificate existed = GiftCertificate.builder()
                .id(2L)
                .name(existedTitle)
                .description(existedDescription)
                .createDate(LocalDateTime.parse(existedDate, DateTimeFormatter.ISO_DATE_TIME))
                .duration(existedDuration)
                .price(existedPrice)
                .build();
        GiftCertificate notExisted = GiftCertificate.builder().id(25L).name("some name for test").build();
        //when
        boolean isExistsExpectedTrue = giftCertificateRepository.existsByName(existed.getName());
        boolean isExistsExpectedFalse = giftCertificateRepository.existsByName(notExisted.getName());
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
        String title = "microsoft_1";
        String description = "monopolist";
        String createDate = "2023-03-23T15:59:05.284";
        Double price = 256.0;
        Integer duration = 2;
        GiftCertificate forSave = GiftCertificate.builder()
                .name(title)
                .description(description)
                .createDate(LocalDateTime.parse(createDate, DateTimeFormatter.ISO_DATE_TIME))
                .duration(duration)
                .price(price)
                .build();
        //when
        GiftCertificate saved = giftCertificateRepository.save(forSave);
        //then
        then(saved).isNotNull();
        then(saved.getId()).isEqualTo(generatedID);
        then(saved.getName()).isEqualTo(title);
        then(saved.getDescription()).isEqualTo(description);
        then(saved.getCreateDate()).isEqualTo(createDate);
        then(saved.getPrice()).isEqualTo(price);
        then(saved.getDuration()).isEqualTo(duration);
    }


    @Sql(scripts = {"/schema-h2.sql", "/data-h2.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    void findAllByName() {
        //given
        String partOfName = "mi";
        Pageable pageable = PageRequest.of(0, 5);
        //when
        List<GiftCertificate> actual = giftCertificateRepository
                .findAllByNameContainingIgnoreCase(partOfName, pageable);
        //then
        then(actual.size()).isEqualTo(2);
        then(actual).isEqualTo(getAllForNameLike());
    }

    @Sql(scripts = {"/schema-h2.sql", "/data-h2.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    void findById() {
        //given
        Long id = 4L;
        String expectedTitle = "android";
        //when
        Optional<GiftCertificate> certificateOptional = giftCertificateRepository.findById(id);
        GiftCertificate giftCertificate = null;
        if (certificateOptional.isPresent()) giftCertificate = certificateOptional.get();
        //then
        then(giftCertificate).isNotNull();
        then(giftCertificate.getId()).isEqualTo(id);
        then(giftCertificate.getName()).isEqualTo(expectedTitle);
    }

    @Sql(scripts = {"/schema-h2.sql", "/data-h2.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    void findAllByReceipt() {
        Long receiptID = 2L;
        Pageable pageable = PageRequest.of(0, 10);
        //when
        List<GiftCertificate> certificates = giftCertificateRepository.findAllByReceipt(receiptID, pageable);
        //then
        then(certificates.size()).isEqualTo(3);
        then(certificates).isEqualTo(getAllByReceiptIDEqualTo2());
    }

    @Sql(scripts = {"/schema-h2.sql", "/data-h2.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    void findAll() {
        //given
        Pageable pageableDefault = PageRequest.of(0, 10);
        //when
        List<GiftCertificate> defaultList = giftCertificateRepository.findAll(pageableDefault).toList();
        //then
        then(defaultList.size()).isEqualTo(4);
    }



    @Sql(scripts = {"/schema-h2.sql", "/data-h2.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    void getTotalRecords() {
        //given
        Long expectedRecords = 4L;
        //when
        Long actual = giftCertificateRepository.count();
        //then
        then(actual).isEqualTo(expectedRecords);
    }

    @Sql(scripts = {"/schema-h2.sql", "/data-h2.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    void getTotalRecordsForReceiptID() {
        //given
        Long receiptID = 6L;
        Long expectedRecords = 4L;
        //when
        Long actualRecords = giftCertificateRepository.getTotalRecordsForReceiptID(receiptID);
        //then
        then(actualRecords).isEqualTo(expectedRecords);
    }

    @Sql(scripts = {"/schema-h2.sql", "/data-h2.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    void getTotalRecordsForNameLike() {
        //given
        Long expectedRecords = 2L;
        String partOfName = "mi";
        //when
        Long actual = giftCertificateRepository.countByNameContainingIgnoreCase(partOfName);
        //then
        then(actual).isEqualTo(expectedRecords);
    }

    @Sql(scripts = {"/schema-h2.sql", "/data-h2.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)

    private List<GiftCertificate> getAllForNameLike() {
        String date1 = "2023-03-23T15:59:05.284";
        String date2 = "2023-03-23T16:00:05.284";
        return List.of(
                GiftCertificate.builder()
                        .id(2L)
                        .name("microsoft")
                        .description("monopoly")
                        .createDate(LocalDateTime.parse(date1, DateTimeFormatter.ISO_DATE_TIME))
                        .duration(2)
                        .price(255.0)
                        .build(),
                GiftCertificate.builder()
                        .id(3L)
                        .name("mixed")
                        .description("all-in-one")
                        .createDate(LocalDateTime.parse(date2, DateTimeFormatter.ISO_DATE_TIME))
                        .duration(2)
                        .price(55.0)
                        .build()
        );
    }

    private List<GiftCertificate> getAllByReceiptIDEqualTo2() {
        String date = "2023-03-23T15:58:05.284";
        String date1 = "2023-03-23T15:59:05.284";
        String date2 = "2023-03-23T16:00:05.284";
        return List.of(
                GiftCertificate.builder()
                        .id(1L)
                        .name("jvm")
                        .description("jvm based languages")
                        .createDate(LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME))
                        .duration(2)
                        .price(255.0)
                        .build(),
                GiftCertificate.builder()
                        .id(2L)
                        .name("microsoft")
                        .description("monopoly")
                        .createDate(LocalDateTime.parse(date1, DateTimeFormatter.ISO_DATE_TIME))
                        .duration(2)
                        .price(255.0)
                        .build(),
                GiftCertificate.builder()
                        .id(3L)
                        .name("mixed")
                        .description("all-in-one")
                        .createDate(LocalDateTime.parse(date2, DateTimeFormatter.ISO_DATE_TIME))
                        .duration(2)
                        .price(55.0)
                        .build()
        );
    }

}