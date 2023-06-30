package com.epam.esm.controller.assembler;

import com.epam.esm.controller.UserController;
import com.epam.esm.controller.modal.UserModel;
import com.epam.esm.dto.UserDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserModelAssembler extends RepresentationModelAssemblerSupport<UserDTO, UserModel> {
    public static final String RECEIPTS_REL = "receipts";
    private static final int PAGE = 0;
    private static final int SIZE = 10;

    public UserModelAssembler() {
        super(UserController.class, UserModel.class);
    }

    /**
     * Converts the given entity into a {@code UserModel}, which extends {@link RepresentationModel}.
     *
     * @param entity of UserDTO class which will be converted to representation model
     * @return UserModel object
     */
    @Override
    public UserModel toModel(UserDTO entity) {
        UserModel userModel = new UserModel(entity);

        userModel.add(linkTo(
                        methodOn(UserController.class)
                                .findByID(entity.getId()))
                        .withSelfRel(),
                linkTo(
                        methodOn(UserController.class)
                                .findReceiptsByUserID(entity.getId(), PageRequest.of(PAGE, SIZE)))
                        .withRel(RECEIPTS_REL));

        return userModel;
    }

}
