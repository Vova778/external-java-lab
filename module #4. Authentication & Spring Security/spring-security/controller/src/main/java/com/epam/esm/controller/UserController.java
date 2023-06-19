package com.epam.esm.controller;

import com.epam.esm.controller.assembler.UserModalAssembler;
import com.epam.esm.controller.modal.UserModal;
import com.epam.esm.service.UserService;
import com.epam.esm.utils.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserModalAssembler userModalAssembler;

    @GetMapping("/find/{id}")
    public UserModal findById(@PathVariable Long id) {
        return userModalAssembler.toModel(userService.findById(id));
    }

    @GetMapping("/find")
    public CollectionModel<UserModal> findByName(@RequestParam String name,
                                    @RequestParam Integer page,
                                    @RequestParam Integer pageSize) {
        return userModalAssembler
                .toCollectionModel(
                        userService.findAllByName(name, new Pageable(page, pageSize)));
    }

    @GetMapping("/find-all")
    public CollectionModel<UserModal> findAll(@RequestParam Integer page,
                                              @RequestParam Integer pageSize) {

        return userModalAssembler
                .toCollectionModel(
                        userService.findAll(new Pageable(page, pageSize)), page, pageSize);
    }

}