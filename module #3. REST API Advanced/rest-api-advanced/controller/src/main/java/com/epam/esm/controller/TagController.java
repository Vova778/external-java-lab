package com.epam.esm.controller;

import com.epam.esm.controller.assembler.TagModalAssembler;
import com.epam.esm.controller.modal.TagModal;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.service.TagService;
import com.epam.esm.utils.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;
    private final TagModalAssembler tagModalAssembler;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public TagModal save(@RequestBody TagDTO tagDTO) {
        return tagModalAssembler.toModel(tagService.save(tagDTO));
    }

    @GetMapping("/find/{id}")
    public TagModal findById(@PathVariable Long id) {
        return tagModalAssembler.toModel(tagService.findById(id));
    }

    @GetMapping("/find")
    public TagModal findByName(@RequestParam String name) {
        return tagModalAssembler.toModel(tagService.findByName(name));
    }

    @GetMapping("/find-all")
    public CollectionModel<TagModal> findAll(@RequestParam Integer page,
                                             @RequestParam Integer pageSize) {
        return tagModalAssembler
                .toCollectionModel(
                        tagService.findAll(new Pageable(page, pageSize)), page, pageSize);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        tagService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}