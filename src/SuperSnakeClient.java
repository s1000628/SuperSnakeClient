
import java.io.*;
import java.net.*;
import java.util.*;

import snct_procon.supersnake.*;
import snct_procon.supersnake.net.*;

public class SuperSnakeClient implements AutoCloseable {
    
    public SuperSnakeClient(Player player) {
        this.player = player;
    }
    
    /**
     * SuperSnake サーバーに接続する.
     * 接続の確立、プレイヤー情報の送信、ゲーム情報の受信を行う.
     * @param host 接続先ホスト名
     * @param port 接続先ポート番号
     * @throws IOException
     * @throws InterruptedException
     */
    public void connect(String host, int port) throws IOException, InterruptedException {
        // 接続
        socket = new Socket(host, port);
        sender = new Sender(socket.getOutputStream());
        receiver = new Receiver(socket.getInputStream());
        
        // 自分の情報を送信
        data.setMyPlayerName(player.getName());
        data.setMyPlayerColor(player.getColor());
        sender.beginSend(DataType.PLAYER_INFO, data.getPlayerInfoBytes());
        
        // ゲームの情報を受信
        do {
            receiver.beginReceive();
            while (!receiver.isReceived()) {
                Thread.sleep(10);
            }
        } while (receiver.getDataType() != DataType.GAME_INFO);
        data.setGameInfo(receiver.getData());
    }
    
    /**
     * 次のターンが開始するまで待機する.
     * ゲームの状態またはゲームの結果を受信するまで待機する.
     * @throws InterruptedException
     * @throws IOException
     */
    public void waitForNextTurn() throws InterruptedException, IOException {
        do {
            receiver.beginReceive();
            while (!receiver.isReceived()) {
                Thread.sleep(10);
            }
        } while (receiver.getDataType() != DataType.GAME_STATE && receiver.getDataType() != DataType.GAME_RESULT);
        if (receiver.getDataType() == DataType.GAME_STATE) {
            data.setGameState(receiver.getData());
        } else if (receiver.getDataType() == DataType.GAME_RESULT) {
            data.setGameResult(receiver.getData());
        }
    }
    
    /**
     * 次の行動を決定する.
     * @throws IOException
     */
    public void decideAction() throws IOException {
        Action action = player.think(data.getGameState());
        sender.beginSend(DataType.ANSWER, data.getActionBytes(action));
    }
    
    /**
     * ゲームの状態を表示する.
     */
    public void showGameState() {
        player.showGameState(data.getGameState());
    }
    
    /**
     * ゲームの結果を表示する.
     * isGameover() が true の状態で呼び出さなければならない.
     */
    public void showResult() {
        List<Integer> rank = new ArrayList<Integer>();
        for (int i = 0; i < data.getPlayersCount(); ++i) {
            rank.add(data.getRank(i));
        }
        player.showResult(data.getGameState(), rank);
    }

    /**
     * ゲームが終了しているか否かを取得する.
     * @return ゲームが終了していれば true 、そうでなければ false
     */
    public boolean isGameover() {
        return data.isGameover();
    }
    
    /**
     * プレイヤーが生きているか否かを取得する.
     * @return プレイヤーが生きていれば true 、そうでなければ false
     */
    public boolean isAlive() {
        return data.getPlayerState(data.getMyPlayerNumber()).isAlive();
    }

    @Override
    public void close() {
        if (socket != null) {
            try {
                socket.close();
                socket = null;
            }
            catch (IOException ex) {
            }
        }
    }
    
    Socket socket;
    Sender sender;
    Receiver receiver;
    DataConverter data = new DataConverter();
    Player player;
}
