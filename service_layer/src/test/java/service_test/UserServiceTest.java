package service_test;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.entity.User;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.repository_impl.UserRepositoryImpl;
import com.epam.esm.service.UserService;
import com.epam.esm.service_impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {UserServiceImpl.class})
public class UserServiceTest extends Mockito {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    User user1 = new User(1L, "1");
    User user2 = new User(2L, "2");

    UserDTO user1DTO = new UserDTO(1L, "1");
    UserDTO user2DTO = new UserDTO(2L, "2");

    @Test
    public void testGetTag() {

        when(userRepository.getEntity(user1.getId())).thenReturn(Optional.of(user1));

        assertEquals(Optional.of(user1DTO), userService.getUser(user1DTO.getId()));
        verify(userRepository).getEntity(user1.getId());

        when(userRepository.getEntity(user2.getId())).thenReturn(Optional.empty());

        assertTrue(userService.getUser(user2DTO.getId()).isEmpty());
        verify(userRepository).getEntity(user2.getId());
    }
}
