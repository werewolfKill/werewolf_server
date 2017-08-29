package werewolf;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author wangtonghe
 * @date 2017/8/29 23:26
 */
public class ListTest {

    @Test
    public void test(){

        List<String> src = new ArrayList<>();
        src.add("asd");
        List<String> dest = new ArrayList<>(Arrays.asList(new String[src.size()]));
        Collections.copy(dest,src);
        System.out.println(dest);



    }
}
