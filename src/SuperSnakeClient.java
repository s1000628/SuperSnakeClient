
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
            
            System.out.println(host + ":" + port + " �ɐڑ�����");
            socket = new Socket(host, port);
            Sender sender = new Sender(socket.getOutputStream());
            Receiver receiver = new Receiver(socket.getInputStream());
            DataConverter data = new DataConverter();
            System.out.println("OK");
            
            System.out.println("�����̏��𑗐M����");
            data.setMyPlayerName("Java���Y");
            data.setMyPlayerColor(new Color(0, 255, 0));
            sender.beginSend(DataType.PLAYER_INFO, data.getPlayerInfoBytes());
            System.out.println("OK");
            
            System.out.println("�Q�[���̏�����M����");
            receiver.beginReceive();
            do {
                while (!receiver.isReceived()) {
                    Thread.sleep(10);
                }
            } while (receiver.getDataType() != DataType.GAME_INFO);
            data.setGameInfo(receiver.getData());
            System.out.println("OK");
            
            while (true) {
                System.out.println("�Q�[���̏�Ԃ���M����");
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
                        + (player.isDead() ? "(���S)" : "")
                        + " (" + player.getPosition().getX() + ", " + player.getPosition().getY() + ")"
                        + " " + player.getDirection().name()
                        );
                }
                
                System.out.println("[ Action ]");
                System.out.print("0:���i | 1:�� | 2:�E>");
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
