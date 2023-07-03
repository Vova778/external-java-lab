package com.epam.esm.controller;


import com.epam.esm.assembler.GiftCertificateModelAssembler;
import com.epam.esm.assembler.ReceiptModelAssembler;
import com.epam.esm.assembler.UserModelAssembler;
import com.epam.esm.modal.GiftCertificateModel;
import com.epam.esm.modal.ReceiptModel;
import com.epam.esm.modal.UserModel;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.ReceiptDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.payload.request.ReceiptRequestBody;
import com.epam.esm.service.GiftCertificateService;
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
@RequestMapping(value = "/receipts")
@RequiredArgsConstructor
public class ReceiptController {

    private final ReceiptService receiptService;
    private final GiftCertificateService giftCertificateService;
    private final UserService userService;
    private final ReceiptModelAssembler receiptModelAssembler;
    private final GiftCertificateModelAssembler giftCertificateModelAssembler;
    private final UserModelAssembler userModelAssembler;
    private final PagedResourcesAssembler<ReceiptDTO> receiptPagedResourcesAssembler;
    private final PagedResourcesAssembler<GiftCertificateDTO> certificatePagedResourcesAssembler;

    @PostMapping("/create")
    public ResponseEntity<ReceiptModel> save(@RequestBody ReceiptRequestBody receiptRequestBody) {

        ReceiptDTO receiptDTO = receiptService.save(receiptRequestBody);
        ReceiptModel receiptModel = receiptModelAssembler.toModel(receiptDTO);
        return new ResponseEntity<>(receiptModel, HttpStatus.CREATED);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<ReceiptModel> findByID(@PathVariable Long id) {
        ReceiptDTO receiptDTO = receiptService.findById(id);
        ReceiptModel receiptModel = receiptModelAssembler.toModel(receiptDTO);
        return new ResponseEntity<>(receiptModel, HttpStatus.OK);
    }

    @GetMapping("/find-all")
    public ResponseEntity<PagedModel<ReceiptModel>> findAll(Pageable pageable) {
        Page<ReceiptDTO> receiptPage = receiptService.findAll(pageable);
        PagedModel<ReceiptModel> pagedModel = receiptPagedResourcesAssembler.toModel(receiptPage, receiptModelAssembler);
        return new ResponseEntity<>(pagedModel, HttpStatus.OK);
    }

    @GetMapping("/find/{receiptID}/gift-certificates")
    public ResponseEntity<PagedModel<GiftCertificateModel>> findGiftCertificatesByReceipt(@PathVariable Long receiptID,
                                                                                          Pageable pageable) {
        Page<GiftCertificateDTO> certificatePage = giftCertificateService.findAllByReceipt(receiptID, pageable);
        PagedModel<GiftCertificateModel> pagedModel = certificatePagedResourcesAssembler
                .toModel(certificatePage, giftCertificateModelAssembler);
        return new ResponseEntity<>(pagedModel, HttpStatus.OK);
    }

    @GetMapping("/find/{receiptID}/user")
    public ResponseEntity<UserModel> findUserByReceipt(@PathVariable Long receiptID) {
        UserDTO userDTO = userService.findByReceipt(receiptID);
        UserModel userModel = userModelAssembler.toModel(userDTO);
        return new ResponseEntity<>(userModel, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteByID(@PathVariable Long id) {
         receiptService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}