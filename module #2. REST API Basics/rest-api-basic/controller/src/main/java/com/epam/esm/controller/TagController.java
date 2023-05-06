package com.epam.esm.controller;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    void save(@RequestBody TagDTO tagDTO) {
        tagService.save(tagDTO);
    }

    @GetMapping("/find/{id}")
    TagDTO findById(@PathVariable Long id) {
        return tagService.findById(id);
    }

    @GetMapping("/find")
    List<TagDTO> findByName(@RequestParam String name) {
        return tagService.findAllByName(name);
    }

    @GetMapping("/find-all")
    List<TagDTO> findAll() {
        return tagService.findAll();
    }

    @DeleteMapping("/delete/{id}")
    void deleteById(@PathVariable Long id) {
        tagService.deleteById(id);
    }
}