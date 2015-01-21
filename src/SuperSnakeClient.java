
import snct_procon.supersnake.*;
import snct_procon.supersnake.net.*;

import java.net.*;
import java.io.*;

public class SuperSnakeClient {

    public static void main(String[] args) throws IOException, InterruptedException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        Socket socket = null;
        try {
            final String host = "localhost";
            final int port = 12345;
            
            System.out.println(host + ":" + port + " に接続する");
            socket = new Socket(host, port);
            Sender sender = new Sender(socket.getOutputStream());
            Receiver receiver = new Receiver(socket.getInputStream());
            DataConverter data = new DataConverter();
            System.out.println("OK");
            
            System.out.println("自分の情報を送信する");
            data.setMyPlayerName("Java太郎");
            data.setMyPlayerColor(new Color(0, 255, 0));
            sender.beginSend(DataType.PLAYER_INFO, data.getPlayerInfoBytes());
            System.out.println("OK");
            
            System.out.println("ゲームの情報を受信する");
            receiver.beginReceive();
            do {
                while (!receiver.isReceived()) {
                    Thread.sleep(10);
                }
            } while (receiver.getDataType() != DataType.GAME_INFO);
            data.setGameInfo(receiver.getData());
            System.out.println("OK");
            
            while (true) {
                System.out.println("ゲームの状態を受信する");
                do {
                    receiver.beginReceive();
                    while (!receiver.isReceived()) {
                        Thread.sleep(10);
                    }
                } while (receiver.getDataType() != DataType.GAME_STATE);
                data.setGameState(receiver.getData());
                System.out.println("OK");
                
                System.out.println("[ Field State ]");
                FieldState field = data.getFieldState();
                Size size = field.getSize();
                int width = size.getWidth();
                int height = size.getHeight();
                int playersCount = data.getPlayersCount();
                for (int y = 0; y < height; ++y) {
                    for (int x = 0; x < width; ++x) {
                        int p = -1;
                        for (int i = 0; i < playersCount; ++i) {
                            Point pos = data.getPlayerState(i).getPosition();
                            if (pos.getX() == x && pos.getY() == y) {
                                p = i;
                                break;
                            }
                        }
                        if (p >= 0) {
                            System.out.print(p);
                        } else {
                            if (field.getCellState(x, y).isPassable()) {
                                System.out.print("_");
                            } else {
                                System.out.print("X");
                            }
                        }
                    }
                    System.out.println();
                }
                
                System.out.println("[ Players State ]");
                for (int i = 0; i < playersCount; ++i) {
                    PlayerState player = data.getPlayerState(i);
                    System.out.println(
                        player.getName() + "(player" + i + "):"
                        + (player.isDead() ? "(死亡)" : "")
                        + " (" + player.getPosition().getX() + ", " + player.getPosition().getY() + ")"
                        + " " + player.getDirection().name()
                        );
                }
                
                System.out.println("[ Action ]");
                System.out.print("0:直進 | 1:左 | 2:右>");
                byte action = Byte.parseByte(in.readLine());
                sender.beginSend(DataType.ANSWER, new byte[] { action });
            }
        }
        finally {
            if (socket != null) {
                socket.close();
            }
        }
    }

}
