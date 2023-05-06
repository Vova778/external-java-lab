package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.utils.QueryParameters;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/certificates")
@RequiredArgsConstructor
public class GiftCertificateController {
    private final GiftCertificateService giftCertificateService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    void save(@RequestBody GiftCertificateDTO giftCertificateDTO) {
        giftCertificateService.save(giftCertificateDTO);
    }

    @GetMapping("/find/{id}")
    GiftCertificateDTO findById(@PathVariable Long id) {
        return giftCertificateService.findById(id);
    }

    @GetMapping("/find")
    List<GiftCertificateDTO> findByName(@RequestParam String name) {
        return giftCertificateService.findAllByName(name);
    }

    @GetMapping("/find-all")
    List<GiftCertificateDTO> findAll() {
        return giftCertificateService.findAll();
    }

    @GetMapping("/find-all-with-params")
    List<GiftCertificateDTO> findAllWithParams(@RequestParam(required = false) String tagName,
                                               @RequestParam(required = false) String name,
                                               @RequestParam(required = false) String description,
                                               @RequestParam(required = false) String sortByName,
                                               @RequestParam(required = false) String sortByDate) {

        QueryParameters queryParams = QueryParameters.builder()
                .tagName(tagName)
                .name(name)
                .description(description)
                .sortByName(sortByName)
                .sortByDate(sortByDate)
                .build();
        return giftCertificateService.findAllWithParams(queryParams);
    }


    @PutMapping("/update")
    void update(@RequestBody GiftCertificateDTO giftCertificateDTO) {
        giftCertificateService.update(giftCertificateDTO);
    }

    @DeleteMapping("/delete/{id}")
    void deleteById(@PathVariable Long id) {
        giftCertificateService.deleteById(id);
    }

}