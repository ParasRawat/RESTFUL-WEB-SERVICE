package WebService.Controller;


import WebService.Model.UserDetailsRequestModel;
import WebService.Model.UserRest;
import WebService.Service.UserService;
import WebService.Shared.dto.UserDto;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "users")// http"//localhost:8080/users/
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping(path = "/{id}")
    public UserRest getUser(@PathVariable  String id){
        UserRest userRest=new UserRest();
        UserDto userDto=userService.getUserByUserId(id);
        BeanUtils.copyProperties(userDto,userRest);

    return userRest;
}


    @PostMapping
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails)
    {
        UserRest userRest=new UserRest();
        UserDto userDto=new UserDto();
        BeanUtils.copyProperties(userDetails,userDto);

        UserDto createdUser =userService.createUser(userDto);
        BeanUtils.copyProperties(createdUser,userRest);
        return  userRest;
    }

    @PutMapping
    public String updateUser(){
        return "UPDATE USER CALLED";
    }

    @DeleteMapping
    public String DeleteUser(){
        return "DELETE USER WAS CALLED";
    }

}
