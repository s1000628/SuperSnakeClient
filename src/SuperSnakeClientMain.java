
import snct_procon.supersnake.*;
import snct_procon.supersnake.net.*;

import java.net.*;
import java.io.*;

public class SuperSnakeClientMain {

    public static void main(String[] args) throws IOException, InterruptedException {
        // 接続先サーバー
        final String host = "localhost";
        final int port = 12345;

        // プレイヤー
        Player player = new ConsolePlayer();
        
        try (SuperSnakeClient client = new SuperSnakeClient(player)) {
            // 接続
            client.connect(host, port);
            
            while (true) {
                // 次のターンの開始待ち
                client.waitForNextTurn();
                
                // ゲームが終了したら、ゲームの結果を表示して終了
                if (client.isGameover()) {
                    client.showResult();
                    break;
                }
                
                // 行動を考える
                client.decideAction();
            }
        }
    }

}
