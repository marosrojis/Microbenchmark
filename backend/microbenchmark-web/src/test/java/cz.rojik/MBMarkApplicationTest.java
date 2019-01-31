package cz.rojik;

import cz.rojik.backend.util.SecurityHelper;
import cz.rojik.mock.MockDataFactory;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EmptyDbConfig.class)
@DataJpaTest
public abstract class MBMarkApplicationTest {

    @Autowired
    private MockDataFactory mockDataFactory;

    @Before
    public void setUp() {
        mockDataFactory.createMockData();
    }
}
