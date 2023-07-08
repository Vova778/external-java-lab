package com.epam.esm.jpa;

import com.epam.esm.ReceiptRepository;
import com.epam.esm.configuration.JPAConfig;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Receipt;
import com.epam.esm.model.entity.User;
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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;

@DataJpaTest
@ContextConfiguration(classes = {JPAConfig.class, ReceiptRepository.class})
@ActiveProfiles("default")
class ReceiptJPARepositoryIntegrationTest {

    @Autowired
    private ReceiptRepository receiptRepository;

    @Sql(scripts = {"/schema-h2.sql", "/data-h2.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    void isExists() {
        //given
        String date = "2023-04-23T15:58:05.284";
        LocalDateTime createDate = LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME);

        User user = new User();
        user.setId(1L);

        Receipt existedReceipt = new Receipt(2L, 255.0, createDate, user, new HashSet<>());
        Receipt notExistedReceipt = new Receipt(100L, 255.0, createDate, user, new HashSet<>());
        //when
        boolean isExistsExpectedTrue = receiptRepository.existsById(existedReceipt.getId());
        boolean isExistsExpectedFalse = receiptRepository.existsById(notExistedReceipt.getId());
        //then
        then(isExistsExpectedTrue).isTrue();
        then(isExistsExpectedFalse).isFalse();
    }

    @Sql(scripts = {"/schema-h2.sql", "/data-h2.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    void save() {
        //given
        Double price = 1255.0;

        String date = "2025-04-23T15:58:05.284";
        LocalDateTime createDate = LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME);

        User user = new User();
        user.setId(3L);

        Receipt notExistedReceipt = new Receipt();
        notExistedReceipt.setPrice(price);
        notExistedReceipt.setCreateDate(createDate);
        notExistedReceipt.setUser(user);
        notExistedReceipt.setGiftCertificates(new HashSet<>(getNewCertificates()));

        Long generatedID = 7L;
        //when
        Receipt savedReceipt = receiptRepository.save(notExistedReceipt);
        //then
        then(savedReceipt).isNotNull();
        then(savedReceipt.getId()).isEqualTo(generatedID);
        then(savedReceipt.getCreateDate()).isEqualTo(createDate);
        then(savedReceipt.getPrice()).isEqualTo(price);
        then(savedReceipt.getUser().getId()).isEqualTo(user.getId());
    }

    @Sql(scripts = {"/schema-h2.sql", "/data-h2.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    void findById() {
        Long id = 4L;
        Double expectedPrice = 128.0;
        Long expectedUserID = 3L;
        //when
        Optional<Receipt> receiptOptional = receiptRepository.findById(id);
        Receipt receipt = null;
        if (receiptOptional.isPresent()) receipt = receiptOptional.get();
        //then
        then(receipt).isNotNull();
        then(receipt.getId()).isEqualTo(4L);
        then(receipt.getPrice()).isEqualTo(expectedPrice);
        then(receipt.getUser().getId()).isEqualTo(expectedUserID);
    }

    @Sql(scripts = {"/schema-h2.sql", "/data-h2.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    void findAll() {
        //given
        Pageable pageableDefault = PageRequest.of(0, 10);
        Pageable pageableCustom = PageRequest.of(0, 3);
        //when
        List<Receipt> defaultList = receiptRepository.findAll(pageableDefault).toList();
        List<Receipt> customList = receiptRepository.findAll(pageableCustom).toList();
        //then
        then(defaultList.size()).isEqualTo(6);
        then(customList.size()).isEqualTo(3);
    }

    @Sql(scripts = {"/schema-h2.sql", "/data-h2.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    void findAllByUser() {
        Long userID = 3L;
        Pageable pageable = PageRequest.of(0, 10);
        //when
        List<Receipt> receiptsByUser = receiptRepository.findAllByUser(userID, pageable);
        //then
        then(receiptsByUser.size()).isEqualTo(2);
    }

    @Sql(scripts = {"/schema-h2.sql", "/data-h2.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    void getTotalRecords() {
        //given
        Long expectedRecords = 6L;
        //when
        Long actual = receiptRepository.count();
        //then
        then(actual).isEqualTo(expectedRecords);
    }

    @Sql(scripts = {"/schema-h2.sql", "/data-h2.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    void getTotalRecordsForUserID() {
        //given
        Long userID = 4L;
        Long expectedRecords = 1L;
        //when
        Long actualRecords = receiptRepository.countReceiptByUser(userID);
        //then
        then(actualRecords).isEqualTo(expectedRecords);
    }

    private List<GiftCertificate> getNewCertificates() {
        return List.of(
                GiftCertificate.builder()
                        .name("name1")
                        .description("desc1")
                        .createDate(LocalDateTime.now())
                        .price(1000.0)
                        .duration(5).build(),
                GiftCertificate.builder()
                        .name("name2")
                        .description("desc2")
                        .createDate(LocalDateTime.now())
                        .price(255.0)
                        .duration(5).build()
        );
    }

    private List<Receipt> getAll() {
        String date = "2023-03-23T15:58:05.284";
        String date1 = "2023-04-23T15:58:05.284";
        String date2 = "2023-04-29T15:58:05.284";
        String date3 = "2023-05-01T15:58:05.284";
        String date4 = "2023-03-13T15:58:05.284";
        String date5 = "2023-03-22T15:58:05.284";

        return List.of(
                Receipt.builder()
                        .id(1L)
                        .price(210.0)
                        .createDate(LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME))
                        .build(),
                Receipt.builder()
                        .id(2L)
                        .price(255.0)
                        .createDate(LocalDateTime.parse(date1, DateTimeFormatter.ISO_DATE_TIME))
                        .build(),
                Receipt.builder()
                        .id(3L)
                        .price(256.0)
                        .createDate(LocalDateTime.parse(date2, DateTimeFormatter.ISO_DATE_TIME))
                        .build(),
                Receipt.builder()
                        .id(4L)
                        .price(128.0)
                        .createDate(LocalDateTime.parse(date3, DateTimeFormatter.ISO_DATE_TIME))
                        .build(),
                Receipt.builder()
                        .id(5L)
                        .price(50.0)
                        .createDate(LocalDateTime.parse(date4, DateTimeFormatter.ISO_DATE_TIME))
                        .build(),
                Receipt.builder()
                        .id(6L)
                        .price(111.0)
                        .createDate(LocalDateTime.parse(date5, DateTimeFormatter.ISO_DATE_TIME))
                        .build()
        );
    }

    private List<Receipt> getAllWithPageParams() {
        String date = "2023-03-23T15:58:05.284";
        String date1 = "2023-04-23T15:58:05.284";
        String date2 = "2023-04-29T15:58:05.284";

        return List.of(
                Receipt.builder()
                        .id(1L)
                        .price(210.0)
                        .createDate(LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME))
                        .build(),
                Receipt.builder()
                        .id(2L)
                        .price(255.0)
                        .createDate(LocalDateTime.parse(date1, DateTimeFormatter.ISO_DATE_TIME))
                        .build(),
                Receipt.builder()
                        .id(3L)
                        .price(256.0)
                        .createDate(LocalDateTime.parse(date2, DateTimeFormatter.ISO_DATE_TIME))
                        .build()
        );
    }

    private List<Receipt> getAllByUserIDEqualTo3() {
        String date3 = "2023-05-01T15:58:05.284";
        String date5 = "2023-03-22T15:58:05.284";

        return List.of(
                Receipt.builder()
                        .id(4L)
                        .price(128.0)
                        .createDate(LocalDateTime.parse(date3, DateTimeFormatter.ISO_DATE_TIME))
                        .build(),
                Receipt.builder()
                        .id(6L)
                        .price(111.0)
                        .createDate(LocalDateTime.parse(date5, DateTimeFormatter.ISO_DATE_TIME))
                        .build()
        );
    }
}