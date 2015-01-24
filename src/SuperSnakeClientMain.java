
import snct_procon.supersnake.*;

import java.io.*;
import java.net.*;

public class SuperSnakeClientMain {

    public static void main(String[] args) throws IOException, InterruptedException {
        // 接続先サーバー
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("接続先を入力(host:port) > ");
        String serverName = in.readLine();

        // プレイヤー
        Player player = new ConsolePlayer();
        
        try (SuperSnakeClient client = new SuperSnakeClient(player)) {
            // 接続
            try {
                client.connect(serverName);
            }
            catch (Exception ex) {
                System.err.println("サーバーへの接続に失敗しました。");
                return;
            }
            
            try {
                while (true) {
                    // 次のターンの開始待ち
                    client.waitForNextTurn();
                    
                    // ゲームが終了したら、ゲームの結果を表示して終了
                    if (client.isGameover()) {
                        client.showResult();
                        break;
                    }
                    
                    // ゲームの状態を表示
                    client.showGameState();
                    
                    // 行動を考える
                    if (client.isAlive()) {
                        client.decideAction();
                    }
                }
            }
            catch (SocketException ex) {
                System.err.println("サーバーとの通信が切断されました。");
            }
        }
    }

}
