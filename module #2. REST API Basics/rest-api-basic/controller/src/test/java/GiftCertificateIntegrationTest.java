import com.epam.esm.configuration.datasource.impl.TestDataSourceConfiguration;
import com.epam.esm.controller.configuration.initializer.WebAppInitializer;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.TagDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {WebAppInitializer.class, TestDataSourceConfiguration.class})
@WebAppConfiguration()
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GiftCertificateIntegrationTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private List<GiftCertificateDTO> certificates;

    private static final String certificateJSON = "{\n" +
            "        \"name\": \"test\",\n" +
            "        \"description\": \"new\",\n" +
            "        \"price\": 9.0,\n" +
            "        \"duration\": 16\n" +
            "    }";

    private static final String updateJSON = "{\n" +
            "        \"id\": 1,\n" +
            "        \"name\": \"java\",\n" +
            "        \"description\": \"buy me\",\n" +
            "        \"price\": 14.0,\n" +
            "        \"duration\": 5\n" +
            "    }";

    private static final String certificateJSONExists = "{\n" +
            "        \"name\": \"jvm\",\n" +
            "        \"description\": \"jvm based languages\",\n" +
            "        \"price\": 525.0,\n" +
            "        \"duration\": 2\n" +
            "    }";

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        certificates = getCertificates().collect(Collectors.toList());
    }

    @Test
    @Order(1)
    void should_Save() throws Exception {
        this.mockMvc.perform(post("/certificates/create")
                .contentType(MediaType.APPLICATION_JSON).content(certificateJSON))
                .andDo(print()).andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    @Order(3)
    void should_Not_Save_ifExists_andThrow() throws Exception {
        this.mockMvc.perform(post("/certificates/create")
                .contentType(MediaType.APPLICATION_JSON).content(certificateJSONExists))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage")
                        .value("GiftCertificate with given name:[jvm] already exists."))
                .andExpect(jsonPath("$.errorCode").value("40007"))
                .andReturn();
    }

    @Test
    @Order(4)
    void should_Not_Save_andThrow() throws Exception {
        this.mockMvc.perform(post("/certificates/create")
                .contentType(MediaType.APPLICATION_JSON).content("null"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @Order(5)
    void should_findById() throws Exception {
        this.mockMvc.perform(get("/certificates/find/4"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.name").value("android"))
                .andExpect(jsonPath("$.description").value("not familiar"))
                .andExpect(content().contentType("application/json"))
                .andReturn();
    }

    @Test
    @Order(6)
    void should_Not_findById_andThrow() throws Exception {
        this.mockMvc.perform(get("/certificates/find/0"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value(
                        "GiftCertificate.id can't be less than zero or null"))
                .andExpect(jsonPath("$.errorCode").value("40002"))
                .andExpect(content().contentType("application/json"))
                .andReturn();

        this.mockMvc.perform(get("/certificates/find/229"))
                .andDo(print()).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("GiftCertificate not found (id:[229])"))
                .andExpect(jsonPath("$.errorCode").value("40406"))
                .andExpect(content().contentType("application/json"))
                .andReturn();
    }

    @Test
    @Order(7)
    void should_findByName() throws Exception {
        this.mockMvc.perform(get("/certificates/find?name=microsoft"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].name").value("microsoft"))
                .andExpect(jsonPath("$[0].description").value("monopoly"))
                .andExpect(content().contentType("application/json"))
                .andReturn();
    }

    @Test
    @Order(8)
    void should_Not_findByName_andThrow() throws Exception {
        this.mockMvc.perform(get("/certificates/find?name="))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value(
                        "The validated character sequence is blank"))
                .andExpect(jsonPath("$.errorCode").value("40002"))
                .andExpect(content().contentType("application/json"))
                .andReturn();

        this.mockMvc.perform(get("/certificates/find?name=null"))
                .andDo(print()).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value(
                        "GiftCertificate not found (name:[null])"))
                .andExpect(jsonPath("$.errorCode").value("40406"))
                .andExpect(content().contentType("application/json"))
                .andReturn();

        this.mockMvc.perform(get("/certificates/find?name=noNameCert"))
                .andDo(print()).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value(
                        "GiftCertificate not found (name:[noNameCert])"))
                .andExpect(jsonPath("$.errorCode").value("40406"))
                .andExpect(content().contentType("application/json"))
                .andReturn();

    }

    @Test
    @Order(9)
    void should_findAll() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/certificates/find-all"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        List<GiftCertificateDTO> actual = mapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<>() {
                });
        actual.sort(Comparator.comparing(GiftCertificateDTO::getId));
        assertEquals(certificates, actual);
    }

    @Test
    @Order(10)
    void should_findAll_With_Params() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/certificates/find-all-with-params?tagName=c&" +
                "name=m&description=n&sortByName=DESC&sortByDate="))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        List<GiftCertificateDTO> actual = mapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<>() {
                });
        certificates = getCertificatesForParametrizedQuery()
                .sorted(Comparator.comparing(GiftCertificateDTO::getName).reversed())
                .collect(Collectors.toList());
        assertEquals(certificates, actual);
    }

    @Test
    @Order(11)
    void should_Update() throws Exception {
        this.mockMvc.perform(put("/certificates/update")
                .contentType(MediaType.APPLICATION_JSON).content(updateJSON))
                .andDo(print()).andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @Order(2)
    void should_Delete() throws Exception {
        this.mockMvc.perform(delete("/certificates/delete/5"))
                .andDo(print()).andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @Order(11)
    void should_Not_Delete_andThrow() throws Exception {
        this.mockMvc.perform(delete("/certificates/delete/99"))
                .andDo(print()).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value(
                        "Certificate with given id:[99] not found for delete."))
                .andExpect(jsonPath("$.errorCode").value("40406"))
                .andReturn();

        this.mockMvc.perform(delete("/certificates/delete/0"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value(
                        "GiftCertificate.id can't be less than zero or null"))
                .andExpect(jsonPath("$.errorCode").value("40002"))
                .andReturn();
    }


    private static Stream<GiftCertificateDTO> getCertificates() {
        return Stream.of(GiftCertificateDTO.builder()
                        .id(1L)
                        .name("jvm")
                        .description("jvm based languages")
                        .price(55.0)
                        .duration(2)
                        .createDate(LocalDateTime.of(2023, 3, 23,
                                15, 58, 5, 284000000))
                        .tags(Stream.of(new TagDTO(1L, "java"),
                                new TagDTO(2L, "scala"),
                                new TagDTO(5L, "kotlin"))
                                .collect(Collectors.toCollection(HashSet::new)))
                        .build(),
                GiftCertificateDTO.builder()
                        .id(2L)
                        .name("microsoft")
                        .description("monopoly")
                        .price(55.0)
                        .duration(2)
                        .createDate(LocalDateTime.of(2023, 3, 23,
                                15, 59, 5, 284000000))
                        .tags(Stream.of(new TagDTO(3L, "c"),
                                new TagDTO(4L, "c-sharp"),
                                new TagDTO(6L, "visual basic"))
                                .collect(Collectors.toCollection(HashSet::new)))
                        .build(),
                GiftCertificateDTO.builder()
                        .id(3L)
                        .name("mixed")
                        .description("all-in-one")
                        .price(55.0)
                        .duration(2)
                        .createDate(LocalDateTime.of(2023, 3, 23,
                                16, 0, 5, 284000000))
                        .tags(Stream.of(new TagDTO(1L, "java"),
                                new TagDTO(4L, "c-sharp"),
                                new TagDTO(6L, "visual basic"))
                                .collect(Collectors.toCollection(HashSet::new)))
                        .build(),
                GiftCertificateDTO.builder()
                        .id(4L)
                        .name("android")
                        .description("not familiar")
                        .price(55.0)
                        .duration(2)
                        .createDate(LocalDateTime.of(2023, 3, 23,
                                17, 58, 5, 284000000))
                        .tags(Stream.of(new TagDTO(1L, "java"),
                                new TagDTO(5L, "kotlin"))
                                .collect(Collectors.toCollection(HashSet::new)))
                        .build());
    }

    private static Stream<GiftCertificateDTO> getCertificatesForParametrizedQuery() {
        return Stream.of(GiftCertificateDTO.builder()
                        .id(1L)
                        .name("jvm")
                        .description("jvm based languages")
                        .price(55.0)
                        .duration(2)
                        .createDate(LocalDateTime.of(2023, 3, 23,
                                15, 58, 5, 284000000))
                        .tags(Stream.of(new TagDTO(1L, "java"),
                                new TagDTO(2L, "scala"),
                                new TagDTO(5L, "kotlin"))
                                .collect(Collectors.toCollection(HashSet::new)))
                        .build(),
                GiftCertificateDTO.builder()
                        .id(2L)
                        .name("microsoft")
                        .description("monopoly")
                        .price(55.0)
                        .duration(2)
                        .createDate(LocalDateTime.of(2023, 3, 23,
                                15, 59, 5, 284000000))
                        .tags(Stream.of(new TagDTO(3L, "c"),
                                new TagDTO(4L, "c-sharp"),
                                new TagDTO(6L, "visual basic"))
                                .collect(Collectors.toCollection(HashSet::new)))
                        .build(),
                GiftCertificateDTO.builder()
                        .id(3L)
                        .name("mixed")
                        .description("all-in-one")
                        .price(55.0)
                        .duration(2)
                        .createDate(LocalDateTime.of(2023, 3, 23,
                                16, 0, 5, 284000000))
                        .tags(Stream.of(new TagDTO(1L, "java"),
                                new TagDTO(4L, "c-sharp"),
                                new TagDTO(6L, "visual basic"))
                                .collect(Collectors.toCollection(HashSet::new)))
                        .build());
    }
}