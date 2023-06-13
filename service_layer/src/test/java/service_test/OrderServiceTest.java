package service_test;

import com.epam.esm.dto.OrderDTO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.repository_impl.OrderRepositoryImpl;
import com.epam.esm.service.OrderService;
import com.epam.esm.service_impl.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {OrderServiceImpl.class})
public class OrderServiceTest extends Mockito {

    @Autowired
    private OrderService orderService;

    @MockBean
    private OrderRepository orderRepository;
    @MockBean
    private UserRepository            userRepository;
    @MockBean
    private GiftCertificateRepository gcRepository;

    User user1 = new User(1L, "1");

    public GiftCertificate gc1 =
            new GiftCertificate(1L, "1", "1", 1, 1, true, List.of());
    public GiftCertificate gc2 =
            new GiftCertificate(2L, "2", "2", 2, 2, true, List.of());

    LocalDateTime purchaseTimestamp = LocalDateTime.now();
    Order order1 = Order.builder().user(user1).giftCertificate(gc1).cost(4.99).createdDate(purchaseTimestamp).build();
    Order order2 = Order.builder().user(user1).giftCertificate(gc2).cost(4.99).createdDate(purchaseTimestamp).build();

    OrderDTO order1DTO = new OrderDTO(user1.getId(), gc1.getId(), 4.99, purchaseTimestamp);
    OrderDTO order2DTO = new OrderDTO(user1.getId(), gc2.getId(), 4.99, purchaseTimestamp);

    @Test
    public void testGetUserOrders() {

        when(orderRepository.getEntitiesByUser(user1.getId())).thenReturn(List.of(order1, order2));

        assertEquals(List.of(order1DTO, order2DTO), orderService.getUserOrders(user1.getId()));
        verify(orderRepository).getEntitiesByUser(user1.getId());
    }

    @Test
    public void testAddOrderSuccess() {

        when(userRepository.getEntity(user1.getId())).thenReturn(Optional.of(user1));
        when(gcRepository.getEntity(gc1.getId())).thenReturn(Optional.of(gc1));
        when(orderRepository.insertEntity(order1)).thenReturn(order1);
        when(gcRepository.updateEntity(gc1)).thenReturn(gc1);

        assertTrue(orderService.addOrder(user1.getId(), gc1.getId()));
        assertFalse(gc1.isActive());
        verify(gcRepository).updateEntity(gc1);
    }

    @Test
    public void testAddOrderFailOnEmptyUser() {

        when(userRepository.getEntity(user1.getId())).thenReturn(Optional.empty());
        when(gcRepository.getEntity(gc2.getId())).thenReturn(Optional.of(gc2));

        assertFalse(orderService.addOrder(user1.getId(), gc2.getId()));
    }

    @Test
    public void testAddOrderFailOnEmptyGiftCertificate() {

        when(userRepository.getEntity(user1.getId())).thenReturn(Optional.of(user1));
        when(gcRepository.getEntity(gc2.getId())).thenReturn(Optional.empty());

        assertFalse(orderService.addOrder(user1.getId(), gc2.getId()));
    }

    @Test
    public void testAddOrderFailOnNotActiveGiftCertificate() {

        when(userRepository.getEntity(user1.getId())).thenReturn(Optional.of(user1));
        gc2.setActive(false);
        when(gcRepository.getEntity(gc2.getId())).thenReturn(Optional.of(gc2));

        assertFalse(orderService.addOrder(user1.getId(), gc2.getId()));
    }
}
