package WebService.ServiceImplementation;

import WebService.Entity.UserEntity;
import WebService.RepositoryInterfaces.UserRepository;
import WebService.Service.UserService;
import WebService.Shared.dto.UserDto;
import WebService.Shared.dto.Utils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImplmentation implements UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    Utils utils;

    @Override
    public UserDto createUser(UserDto userDto) {
        UserEntity storedUserDetails=userRepository.findByEmail(userDto.getEmail());
        if(storedUserDetails!=null) throw new RuntimeException("RECORED ALREADY EXIST PLEASE TRYA NEW ONE ");

        UserEntity userEntity=new UserEntity();
        BeanUtils.copyProperties(userDto,userEntity);

        String publicuserId=utils.generatedUserId(30);

        userEntity.setEncryptedPassword("test");
        userEntity.setUserId(publicuserId);

        UserEntity storedDetails=userRepository.save(userEntity);

        UserDto returnedValue=new UserDto();
        BeanUtils.copyProperties(storedDetails,returnedValue);


        return returnedValue;
    }
}
