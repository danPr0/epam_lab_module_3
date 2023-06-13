package service_test;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.TransactionFailException;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository_impl.GiftCertificateRepositoryImpl;
import com.epam.esm.repository_impl.TagRepositoryImpl;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service_impl.GiftCertificateServiceImpl;
import com.epam.esm.util_service.DTOUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
        classes = {GiftCertificateServiceImpl.class})
public class GiftCertificateServiceTest extends Mockito {

    @Autowired
    private GiftCertificateService gcService;

    @MockBean
    private GiftCertificateRepository gcRepository;
    @MockBean
    private TagRepository             tagRepository;

    public GiftCertificate gc1;
    public GiftCertificate gc2;

    public GiftCertificateDTO gc1DTO;
    public GiftCertificateDTO gc2DTO;

    private final Tag tag1;
    private final Tag tag2;

    private final TagDTO tag1DTO;
    private final TagDTO tag2DTO;

    {
        tag1 = new Tag(1L, "1");
        tag2 = new Tag(2L, "2");

        tag1DTO = new TagDTO(1L, "1");
        tag2DTO = new TagDTO(2L, "2");

        LocalDateTime createdDate = LocalDateTime.now();
        LocalDateTime lastModifiedDate = LocalDateTime.now();

        gc1 = new GiftCertificate(1L, "1", "1", 1, 1, true, List.of(tag1));
        gc1.setCreatedDate(createdDate);
        gc1.setLastModifiedDate(lastModifiedDate);

        gc2 = new GiftCertificate(2L, "2", "2", 2, 2, true, List.of(tag2));
        gc2.setCreatedDate(createdDate);
        gc2.setLastModifiedDate(lastModifiedDate);

        gc1DTO = new GiftCertificateDTO(1L, "1", "1", 1.0, 1, createdDate, lastModifiedDate, List.of(tag1DTO));
        gc2DTO = new GiftCertificateDTO(2L, "2", "2", 2.0, 2, createdDate, lastModifiedDate, List.of(tag2DTO));
    }

    @Test
    public void testAddGiftCertificate() {

        when(gcRepository.insertEntity(any())).thenReturn(gc1);

        assertDoesNotThrow(() -> gcService.addGiftCertificate(gc1DTO));
        verify(gcRepository).insertEntity(any());

        doThrow(IllegalArgumentException.class).when(gcRepository).insertEntity(any());

        assertThrows(TransactionFailException.class, () -> gcService.addGiftCertificate(gc2DTO));
        verify(gcRepository, times(2)).insertEntity(any());
    }

    @Test
    public void testGetGiftCertificate() {

        when(gcRepository.getEntity(gc1.getId())).thenReturn(Optional.of(gc1));

        assertEquals(Optional.of(gc1DTO), gcService.getGiftCertificate(gc1DTO.getId()));
        verify(gcRepository).getEntity(gc1.getId());

        when(gcRepository.getEntity(gc2.getId())).thenReturn(Optional.empty());

        assertTrue(gcService.getGiftCertificate(gc2DTO.getId()).isEmpty());
        verify(gcRepository).getEntity(gc2.getId());
    }

    @Test
    public void testUpdateGiftCertificate() {

        when(gcRepository.updateEntity(any())).thenReturn(gc1);
        when(gcRepository.getEntity(gc1DTO.getId())).thenReturn(Optional.of(gc1));

        assertDoesNotThrow(() -> gcService.updateGiftCertificate(gc1DTO));
        verify(gcRepository).updateEntity(any(GiftCertificate.class));

        when(gcRepository.getEntity(gc2DTO.getId())).thenReturn(Optional.of(gc2));
        doThrow(IllegalArgumentException.class).when(gcRepository).updateEntity(any(GiftCertificate.class));

        assertThrows(TransactionFailException.class, () -> gcService.updateGiftCertificate(gc2DTO));
        verify(gcRepository, times(2)).updateEntity(any(GiftCertificate.class));
    }

    @Test
    public void deleteGiftCertificate() {

        doNothing().when(gcRepository).deleteEntity(gc1.getId());

        assertTrue(gcService.deleteGiftCertificate(gc1DTO.getId()));
        verify(gcRepository).deleteEntity(gc1.getId());

        doThrow(IllegalArgumentException.class).when(gcRepository).deleteEntity(gc2.getId());

        assertFalse(gcService.deleteGiftCertificate(gc2DTO.getId()));
        verify(gcRepository).deleteEntity(gc2.getId());
    }

    @Test
    public void testGetAll() {

        when(gcRepository.getAll(1, 500, Optional.of(List.of(tag1.getName())), Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty())).thenReturn(List.of(gc1));

        assertEquals(List.of(gc1DTO),
                gcService.getAll(1, 500, Optional.of(List.of(tag1.getName())), Optional.empty(), Optional.empty(),
                        Optional.empty(), Optional.empty()));
        verify(gcRepository).getAll(1, 500, Optional.of(List.of(tag1.getName())), Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty());
    }
}
