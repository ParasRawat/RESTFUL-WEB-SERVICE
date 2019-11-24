package WebService.Controller;


import WebService.Exceptions.UserServiceException;
import WebService.Model.*;
import WebService.Service.UserService;
import WebService.Shared.dto.UserDto;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "users")// http"//localhost:8080/users/
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping(path = "/{id}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserRest getUser(@PathVariable  String id){
        UserRest userRest=new UserRest();
        UserDto userDto=userService.getUserByUserId(id);
        BeanUtils.copyProperties(userDto,userRest);

    return userRest;
}

//CONSUMING INFORMATION IN BOTH XML FORMAT AND THE JSON FORMAT
//PRODUCES INFORMATION IN BOTH XML AND THE JSON FORMAT
    @PostMapping(
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception
    {
        //IF THE METHODE CONTAINS THROWS KEYWORD THEN YOU CAN THROW ANY EXCEPTION AT ANY LINE WITHOUT THE TRY CATCH BLOCK
        UserRest userRest=new UserRest();

        //WE CAN NOW DO ANYTHING THAT WE WANT IN THE USERSERVICEEXCREPTIONCLASS LIKE CALLING A METHODE TO DISPLAY A WARNING
        if(userDetails.getFirstName().isEmpty()) throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        UserDto userDto=new UserDto();
        BeanUtils.copyProperties(userDetails,userDto);

        UserDto createdUser =userService.createUser(userDto);
        BeanUtils.copyProperties(createdUser,userRest);
        return  userRest;
    }

    @PutMapping(
            path = {"/{id}"},
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserRest updateUser(@RequestBody UserDetailsRequestModel userDetailsRequestModel,@PathVariable String id){
        UserRest userRest=new UserRest();


        UserDto userDto=new UserDto();
        BeanUtils.copyProperties(userDetailsRequestModel,userDto);

        UserDto updatedUser =userService.updateUser(id,userDto);
        BeanUtils.copyProperties(updatedUser,userRest);
        return  userRest;
    }

    @DeleteMapping(
            path = {"/{id}"},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public OperationStatusModel DeleteUser(@PathVariable String id){
        OperationStatusModel operationStatusModel=new OperationStatusModel();
        operationStatusModel.setOperationName(RequestOperationName.DELETE.name());

        userService.deleteUser(id);

        operationStatusModel.setOperationResult(RequestOperationStatus.SUCCESS.name());


        return operationStatusModel;
    }

    //PASSING PARAMETER AS A QUERY STRING NOT AS A PATH PARAMETER
    @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public List<UserRest> getUers(@RequestParam(value = "page",defaultValue ="0") int page,@RequestParam(value = "limit",defaultValue = "25") int limit){

         List<UserRest> returnValue=new ArrayList<>();

         List<UserDto> userDtos=userService.getUsers(page,limit);

         for(UserDto user:userDtos){
             UserRest userRest=new UserRest();
             BeanUtils.copyProperties(user,userRest);
             returnValue.add(userRest);
         }

         return returnValue;
    }

}
