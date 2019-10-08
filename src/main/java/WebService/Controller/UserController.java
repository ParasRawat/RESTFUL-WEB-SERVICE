package WebService.Controller;


import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "users")// http"//localhost:8080/users/
public class UserController {


    @RequestMapping(method = RequestMethod.GET)
    public String getUser(){

    return "Get User Methode was called";
}


    @PostMapping
    public String createUser(){
        return "CREATE USER CALLED";
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
