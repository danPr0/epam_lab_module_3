package repository_test;

//import repository_test.util.DbTestUtil;
//
//import javax.sql.DataSource;
//import java.util.Comparator;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static repository_test.util.DbTestUtil.*;
//
///**
// * This class is used for testing DAO repository {@link com.epam.esm.repository_impl.GiftCertificateRepositoryImpl}.
// *
// * @author Danylo Proshyn
// */
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest(classes = {GiftCertificateRepositoryImpl.class, TagRepositoryImpl.class, DataSourceConfig.class})
//@EnableAutoConfiguration
//@TestPropertySource("classpath:datasource-dev.properties")
//@ActiveProfiles("dev")
//public class GiftRepositoryTest {
//
//    private final GiftCertificateRepository gcRepository;
//    private final TagRepository             tagRepository;
//
//    private final JdbcTemplate jdbcTemplate;
//
//    @Autowired
//    public GiftRepositoryTest(
//            GiftCertificateRepository gcRepository, TagRepository tagRepository, JdbcTemplate jdbcTemplate) {
//
//        this.gcRepository  = gcRepository;
//        this.tagRepository = tagRepository;
//
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    @BeforeEach
//    public void createAndFillTables() {
//
//        DbTestUtil.createTables(jdbcTemplate);
//        DbTestUtil.fillTables(jdbcTemplate);
//    }
//
//    @AfterEach
//    public void dropTable() {
//
//        DbTestUtil.dropTables(jdbcTemplate);
//    }
//
//    public int countRows() {
//
//        //noinspection DataFlowIssue
//        return jdbcTemplate.queryForObject("select count(*) from gift_certificates", Integer.class);
//    }
//
//    @Test
//    public void testGet() {
//
//        Assertions.assertEquals(gc1, gcRepository.getEntity(gc1.getId()).get());
//
//        assertTrue(gcRepository.getEntity(gc3Spare.getId()).isEmpty());
//    }
//
//    @Test
//    public void testInsert() {
//
//        gcRepository.insertEntity(gc3Spare);
//
//        assertEquals(5, countRows());
//        Assertions.assertEquals(gc3Spare, gcRepository.getEntity(gc3Spare.getId()).get());
//
//        assertThrows(DuplicateKeyException.class, () -> gcRepository.insertEntity(gc3Spare));
//        assertEquals(5, countRows());
//    }
//
//    @Test
//    public void testUpdate() {
//
//        long            spareId    = gc3Spare.getId();
//        GiftCertificate gcToUpdate = gc3Spare;
//        gcToUpdate.setId(gc2.getId());
//
//        assertEquals(1, gcRepository.updateEntity(gcToUpdate));
//        assertEquals(gcToUpdate, gcRepository.getEntity(gcToUpdate.getId()).get());
//
//        gcToUpdate.setId(spareId);
//        assertEquals(0, gcRepository.updateEntity(gcToUpdate));
//    }
//
//    @Test
//    public void testDelete() {
//
//        assertEquals(1, gcRepository.deleteEntity(gc2.getId()));
//        assertEquals(3, countRows());
//
//        assertTrue(gcRepository.getEntity(gc2.getId()).isEmpty());
//        assertEquals(0, gcRepository.deleteEntity(gc2.getId()));
//    }
//
//    @Test
//    public void testGetAllByTag() {
//
//        List<GiftCertificate> giftCertificates =
//                gcRepository.getAll(Optional.of(tag1.getName()), Optional.empty(), Optional.empty(), Optional.empty(),
//                        Optional.empty());
//
//        assertArrayEquals(new GiftCertificate[]{gc1, gc11},
//                giftCertificates.stream().sorted(Comparator.comparing(GiftCertificate::getId)).toArray());
//
//        assertEquals(0, gcRepository.getAll(Optional.of(tag3Spare.getName()), Optional.empty(), Optional.empty(),
//                Optional.empty(), Optional.empty()).size());
//    }
//
//    @Test
//    public void testGetAllSortedByName() {
//
//        assertArrayEquals(new GiftCertificate[]{gc1, gc11, gc2, gc22},
//                gcRepository.getAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.of("asc"),
//                        Optional.empty()).toArray());
//        assertArrayEquals(new GiftCertificate[]{gc2, gc22, gc1, gc11},
//                gcRepository.getAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.of("desc"),
//                        Optional.empty()).toArray());
//    }
//
//    @Test
//    public void testGetAllSortedByCreateDate() {
//
//        assertArrayEquals(new GiftCertificate[]{gc1, gc11, gc2, gc22},
//                gcRepository.getAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
//                        Optional.of("asc")).toArray());
//        assertArrayEquals(new GiftCertificate[]{gc22, gc2, gc11, gc1},
//                gcRepository.getAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
//                        Optional.of("desc")).toArray());
//    }
//
//    @Test
//    public void testGetAllSortedByTwoParams() {
//
//        assertArrayEquals(new GiftCertificate[]{gc1, gc11, gc2, gc22},
//                gcRepository.getAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.of("asc"),
//                        Optional.of("asc")).toArray());
//        assertArrayEquals(new GiftCertificate[]{gc11, gc1, gc22, gc2},
//                gcRepository.getAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.of("asc"),
//                        Optional.of("desc")).toArray());
//        assertArrayEquals(new GiftCertificate[]{gc2, gc22, gc1, gc11},
//                gcRepository.getAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.of("desc"),
//                        Optional.of("asc")).toArray());
//        assertArrayEquals(new GiftCertificate[]{gc22, gc2, gc11, gc1},
//                gcRepository.getAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.of("desc"),
//                        Optional.of("desc")).toArray());
//    }
//
//    @Test
//    public void testGetByNamePart() {
//
//        assertArrayEquals(new GiftCertificate[]{gc2, gc22},
//                gcRepository.getAll(Optional.empty(), Optional.of("2abc2"), Optional.empty(), Optional.empty(),
//                        Optional.empty()).toArray());
//        assertArrayEquals(new GiftCertificate[]{},
//                gcRepository.getAll(Optional.empty(), Optional.of("sgsdf6df786sd"), Optional.empty(), Optional.empty(),
//                        Optional.empty()).toArray());
//    }
//
//    @Test
//    public void testGetByDescriptionPart() {
//
//        assertArrayEquals(new GiftCertificate[]{gc1, gc11},
//                gcRepository.getAll(Optional.empty(), Optional.empty(), Optional.of("1abc1"), Optional.empty(),
//                        Optional.empty()).toArray());
//        assertArrayEquals(new GiftCertificate[]{},
//                gcRepository.getAll(Optional.empty(), Optional.empty(), Optional.of("sgsdf6df786sd"), Optional.empty(),
//                        Optional.empty()).toArray());
//    }
//
//    @Test
//    public void testAddTagToEntity() {
//
//        gcRepository.addTagToEntity(gc1.getId(), tag2.getId());
//        List<GiftCertificate> giftCertificates =
//                gcRepository.getAll(Optional.of(tag2.getName()), Optional.empty(), Optional.empty(), Optional.empty(),
//                        Optional.empty());
//
//        assertArrayEquals(new GiftCertificate[]{gc1, gc2, gc22},
//                giftCertificates.stream().sorted(Comparator.comparing(GiftCertificate::getId)).toArray());
//
//        assertThrows(DataIntegrityViolationException.class,
//                () -> gcRepository.addTagToEntity(gc3Spare.getId(), tag2.getId()));
//        assertThrows(DataIntegrityViolationException.class,
//                () -> gcRepository.addTagToEntity(gc1.getId(), tag3Spare.getId()));
//    }
//
//    @Test
//    public void testDeleteAllTagsForEntity() {
//
//        assertEquals(tagRepository.getAllByGiftCertificate(gc1.getId()).size(),
//                gcRepository.deleteAllTagsForEntity(gc1.getId()));
//        assertEquals(0, tagRepository.getAllByGiftCertificate(gc1.getId()).size());
//    }
//}
