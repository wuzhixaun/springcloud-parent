import com.wuzx.StreamProducerApplication;
import com.wuzx.service.IMessageProducer;
import com.wuzx.service.LogMessageProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest(classes = {StreamProducerApplication.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class StreamProducerApplicationTest {
    @Autowired
    private IMessageProducer iMessageProducer;

    @Autowired
    private LogMessageProducer logMessageProducer;
    @Test
    public void testSendMessage() {
        iMessageProducer.sendMessage("hello world-吴志旋");
    }


    @Test
    public void testSendMessage2() {
        logMessageProducer.sendMessage("hello world-吴志旋");
    }
}
