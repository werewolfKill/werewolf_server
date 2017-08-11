package werewolf;

import com.sun.javafx.tk.Toolkit;
import javafx.concurrent.Task;
import org.junit.Test;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

/**
 * @author wangtonghe
 * @date 2017/8/10 09:33
 */
public class TimerTest {




    @Test
    public  void timeTest(){
        Timer timer = new Timer();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        timer.schedule(new TimerTask(){

            @Override
            public void run() {
                System.out.println("hahh");
                countDownLatch.countDown();
            }
        },  1000);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
