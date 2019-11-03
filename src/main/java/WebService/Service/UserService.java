package WebService.Service;

import WebService.Shared.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService  extends UserDetailsService {
    UserDto createUser (UserDto userDto);

}
