package WebService.ServiceImplementation;

import WebService.Entity.PasswordResetTokenEntity;
import WebService.Entity.UserEntity;
import WebService.Exceptions.UserServiceException;
import WebService.Model.ErrorMessages;
import WebService.RepositoryInterfaces.PasswordResetTokenRepository;
import WebService.RepositoryInterfaces.UserRepository;
import WebService.Service.UserService;
import WebService.Shared.dto.AddressDTO;

import WebService.Shared.dto.UserDto;
import WebService.Shared.dto.Utils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImplmentation implements UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    Utils utils;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    public UserDto createUser(UserDto userDto) {
        UserEntity storedUserDetails = userRepository.findByEmail(userDto.getEmail());
        if (storedUserDetails != null) throw new RuntimeException("RECORED ALREADY EXIST PLEASE TRYA NEW ONE ");

        for (int i = 0; i < userDto.getAddresses().size(); i++) {

            AddressDTO address = userDto.getAddresses().get(i);

            address.setUserDetails(userDto);
            address.setAddressId(utils.generatedAddressId(30));
            userDto.getAddresses().set(i, address);
        }

        //BeanUtils.copyProperties(userDto, userEntity);
        ModelMapper modelMapper = new ModelMapper();
        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);

        String publicuserId = utils.generatedUserId(30);
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        userEntity.setUserId(publicuserId);

        //we need to generate the user email verification token
        userEntity.setEmailVerificationToken(utils.generateEmailVerificationToken(publicuserId));
        userEntity.setEmailVerificationStatus(Boolean.FALSE);
        UserEntity storedDetails = userRepository.save(userEntity);

        // BeanUtils.copyProperties(storedDetails, returnedValue);
        UserDto returnedValue = modelMapper.map(storedDetails, UserDto.class);


        return returnedValue;
    }

    @Override
    public UserDto getUser(String email) {

        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) throw new UsernameNotFoundException(email);
        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(userEntity, returnValue);
        return returnValue;

    }

    @Override
    public UserDto getUserByUserId(String id) {

        UserDto returedValue = new UserDto();

        UserEntity userEntity = userRepository.findByUserId(id);
        if (userEntity == null) throw new UsernameNotFoundException(id);
        ModelMapper modelMapper = new ModelMapper();
        returedValue = modelMapper.map(userEntity, UserDto.class);

        return returedValue;
    }

    @Override
    public UserDto updateUser(String id, UserDto user) {
        UserDto returedValue = new UserDto();

        UserEntity userEntity = userRepository.findByUserId(id);
        if (userEntity == null)
            throw new UserServiceException(ErrorMessages.COULD_NOT_UPDATE_THE_RECORD.getErrorMessage());


        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        UserEntity updateduserDetail = userRepository.save(userEntity);
        BeanUtils.copyProperties(updateduserDetail, returedValue);
        return returedValue;
    }

    @Override
    public void deleteUser(String id) {
        UserEntity userEntity = userRepository.findByUserId(id);
        if (userEntity == null)
            throw new UserServiceException(ErrorMessages.COULD_NOT_UPDATE_THE_RECORD.getErrorMessage());
        userRepository.delete(userEntity);

    }

    @Override
    public List<UserDto> getUsers(int page, int limit) {
        List<UserDto> returnValue = new ArrayList<>();
        Pageable pageable = PageRequest.of(page, limit);
        Page<UserEntity> userPage = userRepository.findAll(pageable);
        List<UserEntity> userEntities = userPage.getContent();

        for (UserEntity userEntity : userEntities) {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(userEntity, userDto);
            returnValue.add(userDto);
        }

        return returnValue;
    }

    @Override
    public boolean verifyEmailToken(String token) {

        boolean returnValue = false;

        UserEntity userEntity = userRepository.findUserByEmailVerificationToken(token);

        if (userEntity != null) {

            boolean hasTokenExpired = Utils.hasTokenExpired(token);

            if (!hasTokenExpired) {

                //userEntity.setEmailVerificationToken(null);
                userEntity.setEmailVerificationStatus(true);
                userRepository.save(userEntity);
                returnValue = true;

            }
        }
        return returnValue;
    }

    @Override
    public boolean requestPasswordReset(String email) {
        boolean returnValue = true;
        UserEntity entity = userRepository.findByEmail(email);

        if (entity == null) {
            return false;
        }

        String token = Utils.generatePasswordRestToken(entity.getUserId());
        PasswordResetTokenEntity passwordResetTokenEntity = new PasswordResetTokenEntity();
        passwordResetTokenEntity.setToken(token);
        passwordResetTokenEntity.setUserDetails(entity);

        passwordResetTokenRepository.save(passwordResetTokenEntity);

        //send the email message from here

        return returnValue;
    }

    @Override
    public boolean resetPassword(String token, String password) {
        boolean returnValue = false;

        if (Utils.hasTokenExpired(token)) {
            return returnValue;
        }
        PasswordResetTokenEntity passwordResetTokenEntity = passwordResetTokenRepository.findByToken(token);

        if (passwordResetTokenEntity == null) {
            return returnValue;
        }

        String encodedPassword = bCryptPasswordEncoder.encode(password);
        UserEntity userEntity = passwordResetTokenEntity.getUserDetails();
        userEntity.setEncryptedPassword(encodedPassword);
        UserEntity savedEnitity = userRepository.save(userEntity);

        if (savedEnitity != null && savedEnitity.getEncryptedPassword().equalsIgnoreCase(encodedPassword)) {
            returnValue = true;

        }

        passwordResetTokenRepository.delete(passwordResetTokenEntity);
        return returnValue;

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserEntity entity = userRepository.findByEmail(email);
        if (entity == null) throw new UsernameNotFoundException(email);

        //User(String username, String password, boolean enabled,
        // boolean accountNonExpired, boolean credentialsNonExpired,
        // boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities)
        return new User(entity.getEmail(), entity.getEncryptedPassword()
                , entity.getEmailVerificationStatus()
                , true
                , true
                , true,
                new ArrayList<>());

    }
}
