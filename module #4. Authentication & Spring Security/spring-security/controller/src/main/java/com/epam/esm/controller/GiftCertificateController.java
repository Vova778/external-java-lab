package com.epam.esm.controller;

import com.epam.esm.controller.assembler.GiftCertificateModalAssembler;
import com.epam.esm.controller.modal.GiftCertificateModal;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.utils.Pageable;
import com.epam.esm.utils.QueryParameters;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/certificates")
@RequiredArgsConstructor
@Slf4j
public class GiftCertificateController {
    private final GiftCertificateService giftCertificateService;
    private final GiftCertificateModalAssembler giftCertificateModalAssembler;


    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public GiftCertificateModal save(@RequestBody GiftCertificateDTO giftCertificateDTO) {
        return giftCertificateModalAssembler
                .toModel(giftCertificateService.save(giftCertificateDTO));
    }

    @GetMapping("/find/{id}")
    public GiftCertificateModal findById(@PathVariable Long id) {
        return giftCertificateModalAssembler
                .toModel(giftCertificateService.findById(id));
    }

    @GetMapping("/find")
    public CollectionModel<GiftCertificateModal> findByName(@RequestParam String name,
                                                            @RequestParam Integer page,
                                                            @RequestParam Integer pageSize) {
        return giftCertificateModalAssembler.toCollectionModel(
                giftCertificateService.findAllByName(name, new Pageable(page, pageSize))
                , page, pageSize);
    }

    @GetMapping("/find-all")
    public CollectionModel<GiftCertificateModal> findAll(@RequestParam Integer page,
                                                         @RequestParam Integer pageSize) {
        return giftCertificateModalAssembler.toCollectionModel(
                giftCertificateService.findAll(new Pageable(page, pageSize))
                , page, pageSize);
    }

    @PostMapping("/find-by-tags")
    public CollectionModel<GiftCertificateModal> findByTags(@RequestBody Set<String> tags,
                                                            @RequestParam Integer page,
                                                            @RequestParam Integer pageSize) {
        log.debug("FIND_BY_TAGS [{}]", tags);
        return giftCertificateModalAssembler.toCollectionModel(
                giftCertificateService.findAllByTags(tags, new Pageable(page, pageSize))
                , page, pageSize);

    }

    @GetMapping("/find-all-with-params")
    public CollectionModel<GiftCertificateModal> findAllWithParams(@RequestParam(required = false) String tagName,
                                                                   @RequestParam(required = false) String name,
                                                                   @RequestParam(required = false) String description,
                                                                   @RequestParam(required = false) String sortByName,
                                                                   @RequestParam(required = false) String sortByDate,
                                                                   @RequestParam Integer page,
                                                                   @RequestParam Integer pageSize) {

        QueryParameters queryParams = QueryParameters.builder()
                .tagName(tagName)
                .name(name)
                .description(description)
                .sortByName(sortByName)
                .sortByDate(sortByDate)
                .build();
        return giftCertificateModalAssembler.toCollectionModel(
                giftCertificateService.findAllWithParams(queryParams, new Pageable(page, pageSize))
                , page, pageSize);

    }


    @PatchMapping("/update")
    public GiftCertificateModal update(@RequestBody GiftCertificateDTO giftCertificateDTO) {
        return giftCertificateModalAssembler
                .toModel(giftCertificateService.update(giftCertificateDTO));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        giftCertificateService.deleteById(id);
        return ResponseEntity.ok().build();
    }

}