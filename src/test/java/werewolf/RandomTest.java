package werewolf;

import org.junit.Test;

import java.util.Random;

/**
 * @author wangtonghe
 * @date 2017/8/27 18:48
 */
public class RandomTest {

    @Test
    public void test(){
        for(int i=0;i<100;i++){
            System.out.println(new Random().nextInt(10));

        }
    }
}
