package com.epam.esm.controller;

import com.epam.esm.assembler.GiftCertificateModelAssembler;
import com.epam.esm.assembler.TagModelAssembler;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.modal.GiftCertificateModel;
import com.epam.esm.modal.TagModel;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.TagService;
import com.epam.esm.utils.QueryParameters;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
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
    private final TagService tagService;
    private final GiftCertificateModelAssembler giftCertificateModelAssembler;
    private final TagModelAssembler tagModelAssembler;
    private final PagedResourcesAssembler<GiftCertificateDTO> certificatePagedResourcesAssembler;
    private final PagedResourcesAssembler<TagDTO> tagPagedResourcesAssembler;

    @PostMapping("/create")
    public ResponseEntity<GiftCertificateModel> save(@RequestBody GiftCertificateDTO giftCertificateDTO) {
        GiftCertificateDTO certificateDTO = giftCertificateService.save(giftCertificateDTO);
        GiftCertificateModel certificateModel = giftCertificateModelAssembler.toModel(certificateDTO);
        return new ResponseEntity<>(certificateModel, HttpStatus.CREATED);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<GiftCertificateModel> findByID(@PathVariable Long id) {
        GiftCertificateDTO certificateDTO = giftCertificateService.findById(id);
        GiftCertificateModel certificateModel = giftCertificateModelAssembler.toModel(certificateDTO);
        return new ResponseEntity<>(certificateModel, HttpStatus.OK);
    }

    @GetMapping("/find")
    public ResponseEntity<PagedModel<GiftCertificateModel>> findByName(@RequestParam String name,
                                                                       Pageable pageable) {
        Page<GiftCertificateDTO> certificatePage = giftCertificateService.findAllByName(name, pageable);
        PagedModel<GiftCertificateModel> pagedModel = certificatePagedResourcesAssembler
                .toModel(certificatePage, giftCertificateModelAssembler);
        return new ResponseEntity<>(pagedModel, HttpStatus.OK);
    }

    @GetMapping("/find-all")
    public ResponseEntity<PagedModel<GiftCertificateModel>> findAll(Pageable pageable) {
        Page<GiftCertificateDTO> certificatePage = giftCertificateService.findAll(pageable);
        PagedModel<GiftCertificateModel> pagedModel = certificatePagedResourcesAssembler
                .toModel(certificatePage, giftCertificateModelAssembler);
        return new ResponseEntity<>(pagedModel, HttpStatus.OK);
    }

    @PostMapping("/find-by-tags")
    public ResponseEntity<PagedModel<GiftCertificateModel>> findByTags(@RequestBody Set<String> tags,
                                                                       Pageable pageable) {
        log.debug("FIND_BY_TAGS [{}]", tags);
        Page<GiftCertificateDTO> certificatePage = giftCertificateService.findAllByTags(tags, pageable);
        PagedModel<GiftCertificateModel> pagedModel = certificatePagedResourcesAssembler
                .toModel(certificatePage, giftCertificateModelAssembler);
        return new ResponseEntity<>(pagedModel, HttpStatus.OK);
    }

    @GetMapping("/find-all-with-params")
    public ResponseEntity<PagedModel<GiftCertificateModel>> findAllWithParams(
            @RequestParam(required = false) String tagName,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String sortByName,
            @RequestParam(required = false) String sortByDate,
            Pageable pageable) {

        QueryParameters queryParams = QueryParameters.builder()
                .tagName(tagName)
                .name(name)
                .description(description)
                .sortByName(sortByName)
                .sortByDate(sortByDate)
                .build();
        Page<GiftCertificateDTO> certificatePage = giftCertificateService.findAllWithParams(pageable, queryParams);
        PagedModel<GiftCertificateModel> pagedModel = certificatePagedResourcesAssembler
                .toModel(certificatePage, giftCertificateModelAssembler);
        return new ResponseEntity<>(pagedModel, HttpStatus.OK);
    }

    @GetMapping("/find/{certificateID}/tags")
    public ResponseEntity<PagedModel<TagModel>> findTagsByCertificate(@PathVariable Long certificateID,
                                                                      Pageable pageable) {
        Page<TagDTO> tagPage = tagService.findAllByCertificate(certificateID, pageable);
        PagedModel<TagModel> pagedModel = tagPagedResourcesAssembler.toModel(tagPage, tagModelAssembler);
        return new ResponseEntity<>(pagedModel, HttpStatus.OK);
    }


    @PatchMapping("/update")
    public ResponseEntity<GiftCertificateModel> update(@RequestBody GiftCertificateDTO giftCertificateDTO) {
        GiftCertificateDTO certificateDTO = giftCertificateService.update(giftCertificateDTO);
        GiftCertificateModel certificateModel = giftCertificateModelAssembler.toModel(certificateDTO);
        return new ResponseEntity<>(certificateModel, HttpStatus.OK);
    }

    @PatchMapping("/update-price")
    public ResponseEntity<GiftCertificateModel> updatePrice(@RequestParam Long giftCertificateID,
                                                            @RequestParam Double price) {
        GiftCertificateDTO certificateDTO = giftCertificateService.updatePrice(giftCertificateID, price);
        GiftCertificateModel certificateModel = giftCertificateModelAssembler.toModel(certificateDTO);
        return new ResponseEntity<>(certificateModel, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteByID(@PathVariable Long id) {
        giftCertificateService.deleteById(id);
        return ResponseEntity.ok().build();
    }


}