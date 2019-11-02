package WebService.Controller;


import WebService.Model.UserDetailsRequestModel;
import WebService.Model.UserRest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "users")// http"//localhost:8080/users/
public class UserController {


    @RequestMapping(method = RequestMethod.GET)
    public String getUser(){

    return "Get User Methode was called";
}


    @PostMapping
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails)
    {
        return new UserRest();
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
