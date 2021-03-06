package WebService.Controller;

import WebService.Entity.UserEntity;
import WebService.Exceptions.UserServiceException;
import WebService.Model.*;
import WebService.Service.AddressesService;
import WebService.Service.UserService;
import WebService.Shared.dto.AddressDTO;
import WebService.Shared.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;


@RestController
@RequestMapping(value = "/users")// http"//localhost:8080/users/
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    AddressesService addressesService;


    @GetMapping(path = "/{id}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserRest getUser(@PathVariable String id) {
        UserRest userRest = new UserRest();
        UserDto userDto = userService.getUserByUserId(id);
        ModelMapper modelMapper = new ModelMapper();
        userRest = modelMapper.map(userDto, UserRest.class);
        return userRest;
    }

    //CONSUMING INFORMATION IN BOTH XML FORMAT AND THE JSON FORMAT
//PRODUCES INFORMATION IN BOTH XML AND THE JSON FORMAT
    @PostMapping(
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
        //IF THE METHODE CONTAINS THROWS KEYWORD THEN YOU CAN THROW ANY EXCEPTION AT ANY LINE WITHOUT THE TRY CATCH BLOCK
        UserRest userRest;
        //WE CAN NOW DO ANYTHING THAT WE WANT IN THE USERSERVICEEXCREPTIONCLASS LIKE CALLING A METHODE TO DISPLAY A WARNING
        if (userDetails.getFirstName().isEmpty())
            throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());

        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);

        UserDto createdUser = userService.createUser(userDto);
        ModelMapper modelMapper1 = new ModelMapper();
        userRest = modelMapper1.map(createdUser, UserRest.class);
        return userRest;
    }

    @PutMapping(
            path = {"/{id}"},
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserRest updateUser(@RequestBody UserDetailsRequestModel userDetailsRequestModel, @PathVariable String id) {
        UserRest userRest = new UserRest();


        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetailsRequestModel, userDto);

        UserDto updatedUser = userService.updateUser(id, userDto);
        BeanUtils.copyProperties(updatedUser, userRest);
        return userRest;
    }

    @DeleteMapping(
            path = {"/{id}"},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public OperationStatusModel DeleteUser(@PathVariable String id) {
        OperationStatusModel operationStatusModel = new OperationStatusModel();
        operationStatusModel.setOperationName(RequestOperationName.DELETE.name());

        userService.deleteUser(id);

        operationStatusModel.setOperationResult(RequestOperationStatus.SUCCESS.name());


        return operationStatusModel;
    }

    //PASSING PARAMETER AS A QUERY STRING NOT AS A PATH PARAMETER
    @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public List<UserRest> getUers(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "limit", defaultValue = "25") int limit) {

        List<UserRest> returnValue = new ArrayList<>();

        List<UserDto> userDtos = userService.getUsers(page, limit);

        for (UserDto user : userDtos) {
            UserRest userRest = new UserRest();
            BeanUtils.copyProperties(user, userRest);
            returnValue.add(userRest);
        }

        return returnValue;
    }

    //http:localhost:8080/WebService/users/id/addresses
    @GetMapping(path = "/{id}/addresses", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public List<AddressRest> getUserAddresses(@PathVariable String id) {

        List<AddressRest> addressRests;

        List<AddressDTO> addressDTOS = addressesService.getAddresses(id);
        ModelMapper modelMapper = new ModelMapper();
        Type listType = new TypeToken<List<AddressRest>>() {
        }.getType();

        addressRests = modelMapper.map(addressDTOS, listType);

        for (AddressRest addressRest : addressRests) {

            Link addressLink = linkTo(methodOn(UserController.class).getUserAddress(id, addressRest.getAddressId())).withSelfRel();
            addressRest.add(addressLink);

            Link userLink = linkTo(UserController.class).slash(id).withRel("user");

            addressRest.add(userLink);
        }

        return addressRests;
    }


    @GetMapping(path = "/{id}/addresses/{addressId}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public AddressRest getUserAddress(@PathVariable String addressId, @PathVariable String id) {

        AddressDTO addressDTO = addressesService.getAddress(addressId);

        ModelMapper modelMapper = new ModelMapper();

        Link addressLink = linkTo(methodOn(UserController.class).getUserAddress(id, addressId)).withSelfRel();

        Link userLink = linkTo(UserController.class).slash(id).withRel("user");

        Link addressesLink = linkTo(methodOn(UserController.class).getUserAddresses(id)).withRel("addresses");


        AddressRest addressRest = modelMapper.map(addressDTO, AddressRest.class);
        //because we are extending the resouce support
        addressRest.add(addressLink);
        addressRest.add(userLink);
        addressRest.add(addressesLink);

        return addressRest;

    }

    @GetMapping(path = "/email", produces = {MediaType.APPLICATION_JSON_VALUE})
    public OperationStatusModel verifyEmailToken(@RequestParam(value = "token") String token) {

        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.VERIFY_EMAIL.name());


        boolean isVerified = userService.verifyEmailToken(token);

        if (isVerified) {

            returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());

            return returnValue;
        } else {
            returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
            return returnValue;
        }


    }

    // /mywebservice/users/password-reset-request
    //proper format for returning a response in case of a proper request
    @PostMapping(path = "/password-reset-request",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public OperationStatusModel requestResest(@RequestBody PasswordResetRequestModel passwordResetRequestModel) {

        OperationStatusModel operationStatusModel = new OperationStatusModel();

        boolean operationResult = userService.requestPasswordReset(passwordResetRequestModel.getEmail());

        operationStatusModel.setOperationName(RequestOperationName.REQUEST_PASSWORD_REST.name());
        operationStatusModel.setOperationResult(RequestOperationStatus.ERROR.name());

        if (operationResult) {

            operationStatusModel.setOperationResult(RequestOperationStatus.SUCCESS.name());
        }
        return operationStatusModel;


    }

    @PostMapping(path = "/password-reset",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})

    public OperationStatusModel resetPassword(@RequestBody PasswordResetModel passwordResetModel) {
        OperationStatusModel returnValue = new OperationStatusModel();
        boolean operationResult = userService.resetPassword(passwordResetModel.getToken(), passwordResetModel.getPassword());
        returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
        returnValue.setOperationName(RequestOperationStatus.PASSWORD_RESET.name());

        if (operationResult) {
            returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        }
        return returnValue;
    }


}
