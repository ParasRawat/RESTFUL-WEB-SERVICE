package WebService;

import WebService.Entity.UserEntity;
import WebService.RepositoryInterfaces.UserRepository;
import WebService.ServiceImplementation.UserServiceImplmentation;
import WebService.Shared.dto.AddressDTO;
import WebService.Shared.dto.UserDto;
import WebService.Shared.dto.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {

    //FOR STARTERS WE WILL CREATE MOCK OF THE CLASSES THAT WE WANT TO TEST
    //UNIT TESTING THE GET USER METHODE
    @InjectMocks
    UserServiceImplmentation userServiceImplmentation;
    //kind of a fake class that we can instantiate
    @Mock
    UserRepository userRepository;

    String userId="uabsuabsains12131";
    @Mock
    Utils utils;
    UserEntity userEntity;
    String enrcyptedpass="9u3923923u23";
    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @BeforeEach
    void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
        //this methode is called first
        userEntity=new UserEntity();
        userEntity.setId(1L);
        userEntity.setFirstName("Sergey");
        userEntity.setUserId(userId);
        userEntity.setEncryptedPassword(enrcyptedpass);

    }

    //MOKITO FRAMEWORK IS USED TO MOCK THOSE CLASSES THAT OUR TEST FUNCTION NEEDS

    @Test
    final void testGetUser(){
        //STUB IS A HARDCODED FAKE OBJECT CONTAINING HARDCODED VALUE TESTING PURPOSES


        //created a mock and stubbed it with a stub object
        //part of
        when(userRepository.findByEmail( anyString() )).thenReturn(userEntity);
        UserDto userDto=userServiceImplmentation.getUser("test@Email.com");
        //part of the mockito framework
        assertNotNull(userDto);
        assertEquals("Sergey",userDto.getFirstName());

    }

    @Test
    final void testGetUser_UsernameNotFoundException(){
        //creating a mock of an object behaviour
        when(userRepository.findByEmail( anyString() )).thenReturn(null);

        //if this methode does not throw an exception of the expected type, then this methode will fail

        //handling such exceptions in JUNIT 5
        assertThrows(UsernameNotFoundException.class,
                //peice of code that needs to be executed
                () ->{
                    userServiceImplmentation.getUser("deo");
                }
                );

    }

    // we write unit test for a method.
    // for writing junit test cases for a method find out all the objects that you might need to mock


    @Test
    final void testCreateUser(){
    //mocking the first dependent object


        when(userRepository.findByEmail( anyString() )).thenReturn(null);

        when(utils.generatedAddressId(anyInt())).thenReturn("dasadeasasa");

        when(utils.generatedUserId(anyInt())).thenReturn(userId);

        when(bCryptPasswordEncoder.encode(anyString())).thenReturn(enrcyptedpass);

        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        AddressDTO addressDTO=new AddressDTO();
        addressDTO.setType("Shipping");
        List<AddressDTO> addressDTOList=new ArrayList<>();
        addressDTOList.add(addressDTO);

        UserDto userDto=new UserDto();
        userDto.setAddresses(addressDTOList);

        UserDto stored=userServiceImplmentation.createUser(userDto);
        assertNotNull(stored);
        assertEquals(userEntity.getFirstName(),stored.getFirstName());


    }
}

