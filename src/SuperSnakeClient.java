
import snct_procon.supersnake.*;
import snct_procon.supersnake.net.*;

import java.net.*;
import java.io.*;
import java.util.*;

public class SuperSnakeClient {

    public static void main(String[] args) throws IOException, InterruptedException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        Socket socket = null;
        try {
            final String host = "localhost";
            final int port = 12345;
            
            System.out.println(host + ":" + port + " に接続する");
            socket = new Socket(host, port);
            System.out.println("OK");
            
            System.out.println("自分の情報を送信する");
            Sender sender = new Sender(socket.getOutputStream());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            Sender.writeString(bos, "Java");
            bos.write(new byte[] { 0, (byte)255, 0 });
            sender.beginSend(DataType.PLAYER_INFO, bos);
            System.out.println("OK");
            
            System.out.println("ゲームの情報を受信する");
            Receiver receiver = new Receiver(socket.getInputStream());
            receiver.beginReceive();
            do {
                while (!receiver.isReceived()) {
                    Thread.sleep(10);
                }
            } while (receiver.getDataType() != DataType.GAME_INFO);
            ByteArrayInputStream bis = new ByteArrayInputStream(receiver.getData());
            String fieldName = Receiver.readString(bis);
            int width = Receiver.readInt32(bis);
            int height = Receiver.readInt32(bis);
            int playersCount = Receiver.readInt32(bis);
            List<Color> colors = new ArrayList<Color>();
            List<String> names = new ArrayList<String>();
            for (int i = 0; i < playersCount; ++i) {
                names.add(Receiver.readString(bis));
                System.out.println(names.get(names.size() - 1));
                int r = bis.read();
                int g = bis.read();
                int b = bis.read();
                colors.add(new Color(r, g, b));
            }
            int playerNum = Receiver.readInt32(bis);
            System.out.println("OK");
            
            while (true) {
                System.out.println("ゲームの状態を受信する");
                do {
                    receiver.beginReceive();
                    while (!receiver.isReceived()) {
                        Thread.sleep(10);
                    }
                } while (receiver.getDataType() != DataType.GAME_STATE);
                bis = new ByteArrayInputStream(receiver.getData());
                int w = Receiver.readInt32(bis);
                int h = Receiver.readInt32(bis);
                CellState[][] cells = new CellState[w][h];
                for (int y = 0; y < h; ++y) {
                    for (int x = 0; x < w; ++x) {
                        boolean passable = bis.read() != 0;
                        int r = bis.read();
                        int g = bis.read();
                        int b = bis.read();
                        cells[x][y] = new CellState(passable, new Color(r, g, b));
                    }
                }
                int n = Receiver.readInt32(bis);
                List<PlayerState> players = new ArrayList<PlayerState>();
                for (int i = 0; i < n; ++i) {
                    int x = Receiver.readInt32(bis);
                    int y = Receiver.readInt32(bis);
                    Point pos = new Point(x, y);
                    Direction d;
                    switch (bis.read()) {
                    case 0: d = Direction.RIGHT; break;
                    case 1: d = Direction.RIGHT_UP; break;
                    case 2: d = Direction.UP; break;
                    case 3: d = Direction.LEFT_UP; break;
                    case 4: d = Direction.LEFT; break;
                    case 5: d = Direction.LEFT_DOWN; break;
                    case 6: d = Direction.DOWN; break;
                    case 7: d = Direction.RIGHT_DOWN; break;
                    default: d = Direction.RIGHT; break;
                    }
                    boolean alive = bis.read() == 0;
                    players.add(new PlayerState(names.get(i), pos, colors.get(i), d));
                }
                System.out.println("OK");
                
                System.out.println("oooooooooooooooooooo");
                for (int y = 0; y < h; ++y) {
                    for (int x = 0; x < w; ++x) {
                        int p = -1;
                        for (int i = 0; i < players.size(); ++i) {
                            Point pos = players.get(i).getPosition();
                            if (pos.getX() == x && pos.getY() == y) {
                                p = i;
                                break;
                            }
                        }
                        if (p >= 0) {
                            System.out.print(p);
                        } else {
                            if (cells[x][y].isPassable()) {
                                System.out.print("_");
                            } else {
                                System.out.print("X");
                            }
                        }
                    }
                    System.out.println();
                }
                System.out.println("oooooooooooooooooooo");
                for (int i = 0; i < players.size(); ++i) {
                    PlayerState player = players.get(i);
                    System.out.println(player.getName() + "(player" + i + "): " + player.getDirection().name());
                }
                System.out.println("oooooooooooooooooooo");
                
                System.out.print("0:直進|1:左|2:右>");
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
