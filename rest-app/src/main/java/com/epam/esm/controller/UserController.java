package com.epam.esm.controller;

import com.epam.esm.assembler.ReceiptModelAssembler;
import com.epam.esm.assembler.UserModelAssembler;
import com.epam.esm.modal.ReceiptModel;
import com.epam.esm.modal.UserModel;
import com.epam.esm.dto.ReceiptDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.service.ReceiptService;
import com.epam.esm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ReceiptService receiptService;
    private final UserModelAssembler userModelAssembler;
    private final ReceiptModelAssembler receiptModelAssembler;
    private final PagedResourcesAssembler<UserDTO> userPagedResourcesAssembler;
    private final PagedResourcesAssembler<ReceiptDTO> receiptPagedResourcesAssembler;

    @GetMapping("/findById/{id}")
    public ResponseEntity<UserModel> findByID(@PathVariable Long id) {
        UserDTO userDTO = userService.findById(id);
        UserModel userModel = userModelAssembler.toModel(userDTO);
        return new ResponseEntity<>(userModel, HttpStatus.OK);
    }

    @GetMapping("/findByEmail/{email}")
    public ResponseEntity<UserModel> findByEmail(@PathVariable String email) {
        UserDTO userDTO = userService.findByEmail(email);
        UserModel userModel = userModelAssembler.toModel(userDTO);
        return new ResponseEntity<>(userModel, HttpStatus.OK);
    }

    @GetMapping("/find")
    public ResponseEntity<PagedModel<UserModel>> findAllByName(@RequestParam String name,
                                                               Pageable pageable) {
        Page<UserDTO> pageUsers =  userService.findAllByName(name, pageable);
        PagedModel<UserModel> pagedModel = userPagedResourcesAssembler.toModel(pageUsers, userModelAssembler);
        return new ResponseEntity<>(pagedModel, HttpStatus.OK);
    }

    @GetMapping("/find-all")
    public ResponseEntity<PagedModel<UserModel>> findAll(Pageable pageable) {
        Page<UserDTO> pageUsers = userService.findAll(pageable);
        PagedModel<UserModel> pagedModel = userPagedResourcesAssembler.toModel(pageUsers, userModelAssembler);
        return new ResponseEntity<>(pagedModel, HttpStatus.OK);
    }
    @GetMapping("/find/{userID}/receipts")
    public ResponseEntity<PagedModel<ReceiptModel>> findReceiptsByUserID(@PathVariable Long userID,
                                                                         Pageable pageable) {
        Page<ReceiptDTO> receiptModelPage = receiptService.findAllByUser(userID, pageable);
        PagedModel<ReceiptModel> pagedModel = receiptPagedResourcesAssembler
                .toModel(receiptModelPage, receiptModelAssembler);
        return new ResponseEntity<>(pagedModel, HttpStatus.OK);
    }
}