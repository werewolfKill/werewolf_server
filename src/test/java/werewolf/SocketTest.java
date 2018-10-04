package werewolf;

import java.io.PrintStream;
import java.net.Socket;

/**
 * @author wangtonghe
 * @date 2017/9/7 21:45
 */
public class SocketTest {

    public static void main(String[] args) throws Exception{
        Socket client = new Socket("103.44.145.245", 31260);
        client.setSoTimeout(5000);
        PrintStream out = new PrintStream(client.getOutputStream());
        out.print("hello");
    }
}
