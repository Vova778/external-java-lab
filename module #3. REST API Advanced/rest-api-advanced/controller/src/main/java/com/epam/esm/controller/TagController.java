package com.epam.esm.controller;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.service.TagService;
import com.epam.esm.utils.Pageable;
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
    TagDTO save(@RequestBody TagDTO tagDTO) {
        return tagService.save(tagDTO);
    }

    @GetMapping("/find/{id}")
    TagDTO findById(@PathVariable Long id) {
        return tagService.findById(id);
    }

    @GetMapping("/find")
    TagDTO findByName(@RequestParam String name) {
        return tagService.findByName(name);
    }

    @GetMapping("/find-all")
    List<TagDTO> findAll(@RequestParam Integer page,
                         @RequestParam Integer pageSize) {
        return tagService.findAll(new Pageable(page, pageSize));
    }

    @DeleteMapping("/delete/{id}")
    TagDTO deleteById(@PathVariable Long id) {
        return tagService.deleteById(id);
    }
}