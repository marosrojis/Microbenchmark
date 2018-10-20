package cz.rojik;

import cz.rojik.main.MBMarkApplication;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {MBMarkApplication.class})
@WebAppConfiguration
//@TestPropertySource(locations = "classpath:application-test.properties")
public abstract class MBMarkApplicationTest {


}
