package snct_procon.supersnake.net;

import snct_procon.supersnake.*;
import java.util.*;
import java.io.*;

/**
 * SuperSnake で用いる各種通信用データの変換を行う.
 */
public class DataConverter {
    
    public DataConverter() {
    }
    
    /**
     * 自分のプレイヤーの名前を設定する.
     * @param value プレイヤーの名前
     */
    public void setMyPlayerName(String value) {
        this.myName = new String(value);
    }
    
    /**
     * 自分のプレイヤーの色を設定する.
     * @param color プレイヤーの色
     */
    public void setMyPlayerColor(Color color) {
        this.myColor = color.clone();
    }
    
    /**
     * 自分のプレイヤー番号を取得する.
     * @return プレイヤー番号
     */
    public int getMyPlayerNumber() {
        return myPlayerNum;
    }
    
    /**
     * フィールドの状態を取得する.
     * @return フィールドの状態
     */
    public FieldState getFieldState() {
        return field;
    }
    
    /**
     * プレイヤー数を取得する.
     * @return プレイヤー数
     */
    public int getPlayersCount() {
        return players.length;
    }
    
    /**
     * プレイヤーの状態を取得する.
     * @param playerNumber 取得するプレイヤーのプレイヤー番号
     * @return プレイヤーの状態
     */
    public PlayerState getPlayerState(int playerNumber) {
        return players[playerNumber];
    }
    
    /**
     * プレイヤーの順位を取得する.
     * @param playerNumber 取得するプレイヤーのプレイヤー番号
     * @return プレイヤーの位置
     */
    public int getRank(int playerNumber) {
        return rank[playerNumber];
    }
    
    /**
     * ゲームが終了しているか否かを取得する.
     * @return ゲームが終了していれば true 、そうでなければ false
     */
    public boolean isGameover() {
        return gameover;
    }
    
    /**
     * ゲームの状態を取得する.
     * @return ゲームの状態
     */
    public GameState getGameState() {
        List<PlayerState> playerStates = new ArrayList<PlayerState>();
        for (PlayerState player : players) {
            playerStates.add(player);
        }
        return new GameState(field, playerStates);
    }
    
    /**
     * プレイヤーの情報送信用のバイト列を生成する.
     * @return 送信用バイト列
     */
    public byte[] getPlayerInfoBytes() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            Sender.writeString(bos, myName);
        }
        catch (IOException ex) {
            // ByteArrayOutputStream.write() の呼び出しなので IO エラーは発生しない
        }
        bos.write(myColor.getR());
        bos.write(myColor.getG());
        bos.write(myColor.getB());
        return bos.toByteArray();
    }
    
    /**
     * プレイヤーの行動送信用のバイト列を生成する.
     * @param action プレイヤーの行動
     * @return 送信用バイト列
     */
    public byte[] getActionBytes(Action action) {
        byte res = 0;
        switch (action) {
        case STRAIGHT: res = 0; break; 
        case LEFT: res = 1; break;
        case RIGHT: res = 2; break;
        }
        return new byte[] { res };
    }
    
    /**
     * 受信データからゲームの情報を設定する.
     * @param data 受信データのバイト列
     */
    public void setGameInfo(byte[] data) {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        
        try {
            // フィールド名
            String fieldName = Receiver.readString(bis);
            
            // フィールドサイズ
            int w = Receiver.readInt32(bis);
            int h = Receiver.readInt32(bis);
            Size fieldSize = new Size(w, h);
            
            List<List<CellState>> cells = new ArrayList<List<CellState>>();
            for (int x = 0; x < w; ++x) {
                List<CellState> row = new ArrayList<CellState>();
                for (int y = 0; y < h; ++y) {
                    row.add(new CellState(true, new Color(0, 0, 0)));
                }
                cells.add(row);
            }
            field = new FieldState(fieldName, fieldSize, cells);
            
            // プレイヤー人数
            int n = Receiver.readInt32(bis);
            players = new PlayerState[n];
            
            // プレイヤー
            for (int i = 0; i < n; ++i) {
                // プレイヤーの名前
                String name = Receiver.readString(bis);
                
                // プレイヤーの色
                int r = bis.read();
                int g = bis.read();
                int b = bis.read();
                Color color = new Color(r, g, b);
                
                players[i] = new PlayerState(name, color, new Point(0, 0), Direction.RIGHT, true);
            }
            
            // 自分のプレイヤー番号
            myPlayerNum = Receiver.readInt32(bis);
        }
        catch (IOException ex) {
            // ByteArrayInputStream.read() の呼び出しなので IO エラーは発生しない
        }
    }
  
    /**
     * 受信データからゲームの状態を設定する.
     * @param data 受信データのバイト列
     */
    public void setGameState(byte[] data) {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        
        try {
            // フィールドサイズ
            int w = Receiver.readInt32(bis);
            int h = Receiver.readInt32(bis);
            Size size = new Size(w, h);
            
            // セル
            CellState[][] cellsArr = new CellState[w][h];
            for (int y = 0; y < h; ++y) {
                for (int x = 0; x < w; ++x) {
                    // 通行可能フラグ
                    boolean passable = (bis.read() != 0);
                    
                    // 色
                    int r = bis.read();
                    int g = bis.read();
                    int b = bis.read();
                    Color color = new Color(r, g, b);
                    
                    cellsArr[x][y] = new CellState(passable, color);
                }
            }
            List<List<CellState>> cells = new ArrayList<List<CellState>>();
            for (int x = 0; x < w; ++x) {
                List<CellState> row = new ArrayList<CellState>();
                for (int y = 0; y < h; ++y) {
                    row.add(cellsArr[x][y]);
                }
                cells.add(row);
            }
            
            field = new FieldState(field.getName(), size, cells);
            
            // プレイヤー人数
            int n = Receiver.readInt32(bis);
            
            // プレイヤー
            for (int i = 0; i < n; ++i) {
                // 位置
                int x = Receiver.readInt32(bis);
                int y = Receiver.readInt32(bis);
                Point pos = new Point(x, y);
                
                // 向き
                Direction dir;
                switch (bis.read()) {
                case 0: dir = Direction.RIGHT; break;
                case 1: dir = Direction.RIGHT_UP; break;
                case 2: dir = Direction.UP; break;
                case 3: dir = Direction.LEFT_UP; break;
                case 4: dir = Direction.LEFT; break;
                case 5: dir = Direction.LEFT_DOWN; break;
                case 6: dir = Direction.DOWN; break;
                case 7: dir = Direction.RIGHT_DOWN; break;
                default: dir = Direction.RIGHT; break;
                }
                
                // 生死
                boolean alive = (bis.read() == 0);
                
                players[i] = new PlayerState(players[i].getName(), players[i].getColor(), pos, dir, alive);
            }
        }
        catch (IOException ex) {
            // ByteArrayInputStream.read() の呼び出しなので IO エラーは発生しない
        }
    }
    
    /**
     * 受信データからゲームの結果を設定する。
     * @param data 受信データのバイト列
     */
    public void setGameResult(byte[] data) {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        
        try {
            // フィールドサイズ
            int w = Receiver.readInt32(bis);
            int h = Receiver.readInt32(bis);
            Size size = new Size(w, h);
            
            // セル
            CellState[][] cellsArr = new CellState[w][h];
            for (int y = 0; y < h; ++y) {
                for (int x = 0; x < w; ++x) {
                    // 通行可能フラグ
                    boolean passable = (bis.read() != 0);
                    
                    // 色
                    int r = bis.read();
                    int g = bis.read();
                    int b = bis.read();
                    Color color = new Color(r, g, b);
                    
                    cellsArr[x][y] = new CellState(passable, color);
                }
            }
            List<List<CellState>> cells = new ArrayList<List<CellState>>();
            for (int x = 0; x < w; ++x) {
                List<CellState> row = new ArrayList<CellState>();
                for (int y = 0; y < h; ++y) {
                    row.add(cellsArr[x][y]);
                }
                cells.add(row);
            }
            
            field = new FieldState(field.getName(), size, cells);
            
            // プレイヤー人数
            int n = Receiver.readInt32(bis);
            
            // プレイヤー
            for (int i = 0; i < n; ++i) {
                // 位置
                int x = Receiver.readInt32(bis);
                int y = Receiver.readInt32(bis);
                Point pos = new Point(x, y);
                
                // 向き
                Direction dir;
                switch (bis.read()) {
                case 0: dir = Direction.RIGHT; break;
                case 1: dir = Direction.RIGHT_UP; break;
                case 2: dir = Direction.UP; break;
                case 3: dir = Direction.LEFT_UP; break;
                case 4: dir = Direction.LEFT; break;
                case 5: dir = Direction.LEFT_DOWN; break;
                case 6: dir = Direction.DOWN; break;
                case 7: dir = Direction.RIGHT_DOWN; break;
                default: dir = Direction.RIGHT; break;
                }
                
                // 生死
                boolean alive = (bis.read() == 0);
                
                players[i] = new PlayerState(players[i].getName(), players[i].getColor(), pos, dir, alive);
            }
            
            // 順位
            rank = new int[n];
            for (int i = 0; i < n; ++i) {
                rank[i] = Receiver.readInt32(bis);
            }
            
            // ゲームオーバー
            gameover = true;
        }
        catch (IOException ex) {
            // ByteArrayInputStream.read() の呼び出しなので IO エラーは発生しない
        }
    }
    
    private String myName;
    private Color myColor;
    private int myPlayerNum;
    private FieldState field;
    private PlayerState[] players;
    private int[] rank;
    private boolean gameover = false;
}
