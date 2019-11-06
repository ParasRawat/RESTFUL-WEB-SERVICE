package WebService.ServiceImplementation;

import WebService.Entity.UserEntity;
import WebService.RepositoryInterfaces.UserRepository;
import WebService.Service.UserService;
import WebService.Shared.dto.UserDto;
import WebService.Shared.dto.Utils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.ArrayList;

@Service
public class UserServiceImplmentation implements UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    Utils utils;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDto createUser(UserDto userDto) {
        UserEntity storedUserDetails=userRepository.findByEmail(userDto.getEmail());
        if(storedUserDetails!=null) throw new RuntimeException("RECORED ALREADY EXIST PLEASE TRYA NEW ONE ");

        UserEntity userEntity=new UserEntity();
        BeanUtils.copyProperties(userDto,userEntity);

        String publicuserId=utils.generatedUserId(30);

        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        userEntity.setUserId(publicuserId);

        UserEntity storedDetails=userRepository.save(userEntity);

        UserDto returnedValue=new UserDto();
        BeanUtils.copyProperties(storedDetails,returnedValue);


        return returnedValue;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
      UserEntity entity=userRepository.findByEmail(email);
      if(entity==null) throw new UsernameNotFoundException(email);

      return new User(entity.getEmail(),entity.getEncryptedPassword(),new ArrayList<>());

    }
}
