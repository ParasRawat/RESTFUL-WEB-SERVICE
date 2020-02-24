package WebService;

import WebService.Entity.UserEntity;
import WebService.RepositoryInterfaces.UserRepository;
import WebService.ServiceImplementation.UserServiceImplmentation;
import WebService.Shared.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {

    //FOR STARTERS WE WILL CREATE MOCK OF THE CLASSES THAT WE WANT TO TEST
    //UNIT TESTING THE GET USER METHODE
    @InjectMocks
    UserServiceImplmentation userServiceImplmentation;
    //kind of a fake class that we can instantiate
    @Mock
    UserRepository userRepository;

    @BeforeEach
    void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
    }

    //MOKITO FRAMEWORK IS USED TO MOCK THOSE CLASSES THAT OUR TEST FUNCTION NEEDS

    @Test
    final void testGetUser(){
        //STUB IS A HARDCODED FAKE OBJECT CONTAINING HARDCODED VALUE TESTING PURPOSES
        UserEntity userEntity=new UserEntity();
        userEntity.setId(1L);
        userEntity.setFirstName("Sergey");
        userEntity.setUserId("uabsuabsains12131");
        userEntity.setEncryptedPassword("9u3923923u23");

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

}

