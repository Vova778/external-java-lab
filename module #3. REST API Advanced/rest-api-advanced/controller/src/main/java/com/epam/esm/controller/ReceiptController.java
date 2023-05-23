package com.epam.esm.controller;


import com.epam.esm.controller.assembler.ReceiptModalAssembler;
import com.epam.esm.controller.modal.ReceiptModal;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.ReceiptDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.service.ReceiptService;
import com.epam.esm.utils.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(value = "/receipts")
@RequiredArgsConstructor
public class ReceiptController {
    private final ReceiptService receiptService;
    private final ReceiptModalAssembler receiptModalAssembler;

    @PostMapping("/create/{userID}")
    @ResponseStatus(HttpStatus.CREATED)
    public ReceiptModal save(@PathVariable Long userID , @RequestBody Set<Long> giftCertificatesID) {
        ReceiptDTO receiptDTO = new ReceiptDTO();
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userID);
        receiptDTO.setUserDTO(userDTO);
        if (!giftCertificatesID.isEmpty()) {
            giftCertificatesID.forEach(id ->  {
                GiftCertificateDTO giftCertificateDTO = new GiftCertificateDTO();
                giftCertificateDTO.setId(id);
                receiptDTO.getGiftCertificates().add(giftCertificateDTO);
            });
        }
        return receiptModalAssembler.toModel(receiptService.save(receiptDTO));
    }

    @GetMapping("/find/{id}")
    public ReceiptModal findById(@PathVariable Long id) {
        return receiptModalAssembler.toModel(receiptService.findById(id));
    }

    @GetMapping("/find-all")
    public CollectionModel<ReceiptModal> findAll(@RequestParam Integer page,
                                                 @RequestParam Integer pageSize) {
        return receiptModalAssembler
                .toCollectionModel(receiptService.findAll(new Pageable(page, pageSize)),page,pageSize);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        receiptService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}