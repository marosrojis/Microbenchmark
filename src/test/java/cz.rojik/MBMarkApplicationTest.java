package cz.rojik;

import cz.rojik.config.JpaConfig;
import cz.rojik.main.MBMarkApplication;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {MBMarkApplication.class,
        JpaConfig.class})
@WebAppConfiguration
//@TestPropertySource(locations = "classpath:application-test.properties")
public abstract class MBMarkApplicationTest {


}
