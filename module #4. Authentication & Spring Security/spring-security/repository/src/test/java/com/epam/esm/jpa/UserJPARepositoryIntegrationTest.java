package com.epam.esm.jpa;

import com.epam.esm.UserRepository;
import com.epam.esm.configuration.JPAConfig;
import com.epam.esm.model.entity.User;
import com.epam.esm.model.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;

@DataJpaTest
@ContextConfiguration(classes = {JPAConfig.class, UserRepository.class})
@ActiveProfiles("default")
class UserJPARepositoryIntegrationTest {

    @Autowired
    private UserRepository userJPARepository;

    @Sql(scripts = {"/schema-h2.sql", "/data-h2.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    void isExists() {
        //given
        String existedEmail = "mbilney4@ucoz.com";
        String existedFirstName = "Martelle";
        String existedLastName = "Bilney";
        User existedUser = User.builder()
                .id(4L)
                .email(existedEmail)
                .firstName(existedFirstName)
                .lastName(existedLastName)
                .build();
        User notExistedUser = User.builder().id(500L).email("user@mail.com").build();
        //when
        boolean isExistsExpectedTrue = userJPARepository.existsByEmail(existedUser.getEmail());
        boolean isExistsExpectedFalse = userJPARepository.existsByEmail(notExistedUser.getEmail());
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
        String email = "rtofftspj@intel.com";
        String firstName = "Rozetta";
        String lastName = "Stone";
        String encodedPassword = "$2a$10$CJSDqMFXSIpJ48bvn6h44.qnk/FsUl2IYBsuiwtdzdAvJCbJhERaW";
        User forSave = User.builder()
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .password(encodedPassword)
                .userRole(UserRole.CUSTOMER)
                .build();
        //when
        User savedUser = userJPARepository.save(forSave);

        //then
        then(savedUser).isNotNull();
        then(savedUser.getId()).isEqualTo(generatedID);
        then(savedUser.getEmail()).isEqualTo(email);
        then(savedUser.getFirstName()).isEqualTo(firstName);
        then(savedUser.getLastName()).isEqualTo(lastName);
        then(savedUser.getPassword()).isEqualTo(encodedPassword);
        then(savedUser.getUserRole()).isEqualTo(UserRole.CUSTOMER);
    }

    @Sql(scripts = {"/schema-h2.sql", "/data-h2.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    void findByID() {
        //given
        Long id = 3L;
        String expectedEmail = "lblatchford2@rambler.ru";
        //when
        Optional<User> userOptional = userJPARepository.findById(id);
        User user = null;
        if (userOptional.isPresent()) user = userOptional.get();
        //then
        then(user).isNotNull();
        then(user.getId()).isEqualTo(id);
        then(user.getEmail()).isEqualTo(expectedEmail);
    }


    @Sql(scripts = {"/schema-h2.sql", "/data-h2.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    void findByReceipt() {
        //given
        Long receiptID = 5L;
        Long expectedUserID = 4L;
        String expectedEmail = "btooher3@wikispaces.com";
        //when
        Optional<User> userOptional = userJPARepository.findByReceipt(receiptID);
        User user = null;
        if (userOptional.isPresent()) user = userOptional.get();
        //then
        then(user).isNotNull();
        then(user.getId()).isEqualTo(expectedUserID);
        then(user.getEmail()).isEqualTo(expectedEmail);
    }

    @Sql(scripts = {"/schema-h2.sql", "/data-h2.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    void findAllByName() {
        //given
        String partOfName = "Be";
        Pageable pageable = PageRequest.of(0, 5);
        //when
        List<User> actual = userJPARepository.findAllByFirstNameContainingIgnoreCase(partOfName, pageable);
        //then
        then(actual.size()).isEqualTo(4);
        then(actual).isEqualTo(getAllForNameLike());
    }

    @Sql(scripts = {"/schema-h2.sql", "/data-h2.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    void findAll() {
        //given
        int page = 1;
        int pageSize = 5;
        Pageable pageable = PageRequest.of(page, pageSize);
        //when
        List<User> defaultList = userJPARepository.findAll(pageable).toList();
        //then
        then(defaultList.size()).isEqualTo(pageSize);

    }

    @Sql(scripts = {"/schema-h2.sql", "/data-h2.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    void getTotalRecords() {
        //given
        Long expectedRecords = 50L;
        //when
        Long actual = userJPARepository.count();
        //then
        then(actual).isEqualTo(expectedRecords);
    }

    @Sql(scripts = {"/schema-h2.sql", "/data-h2.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    void getTotalRecordsForNameLike() {
        //given
        Long expectedRecords = 4L;
        String partOfName = "Be";
        //when
        Long actual = userJPARepository.countByFirstNameContainingIgnoreCase(partOfName);
        //then
        then(actual).isEqualTo(expectedRecords);
    }

    private List<User> getAllForNameLike() {
        return List.of(
                User.builder()
                        .id(4L)
                        .email("btooher3@wikispaces.com")
                        .firstName("Bent")
                        .lastName("Tooher")
                        .password("4SEgIXZKaXOZ")
                        .userRole(UserRole.CUSTOMER)
                        .build(),
                User.builder()
                        .id(10L)
                        .email("bmyrick9@wp.com")
                        .firstName("Beck")
                        .lastName("Myrick")
                        .password("di7uJL4Fhuwa")
                        .userRole(UserRole.CUSTOMER)
                        .build(),
                User.builder()
                        .id(11L)
                        .email("gvaggesa@upenn.edu")
                        .firstName("Giselbert")
                        .lastName("Vagges")
                        .password("xYNvdz7CFvE")
                        .userRole(UserRole.CUSTOMER)
                        .build(),
                User.builder()
                        .id(30L)
                        .email("mstaniont@cloudflare.com")
                        .firstName("Maybelle")
                        .lastName("Stanion")
                        .password("jI3oGR0vKvD")
                        .userRole(UserRole.CUSTOMER)
                        .build()
        );
    }
}