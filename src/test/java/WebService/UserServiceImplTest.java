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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {

    //FOR STARTERS WE WILL CREATE MOCK OF THE CLASSES THAT WE WANT TO TEST
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
        //STUB IS A HARDCODED OBJECT FAKE OBJECT CONTAINING HARDCODED VALUE
        UserEntity userEntity=new UserEntity();
        userEntity.setId(1L);
        userEntity.setFirstName("Sergey");
        userEntity.setUserId("uabsuabsains12131");
        userEntity.setEncryptedPassword("9u3923923u23");

        when(userRepository.findByEmail( anyString() )).thenReturn(userEntity);
        UserDto userDto=userServiceImplmentation.getUser("test@Email.com");

        assertNotNull(userDto);
        assertEquals("Sergey",userDto.getFirstName());


    }

}

