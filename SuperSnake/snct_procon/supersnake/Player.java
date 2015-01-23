package snct_procon.supersnake;

import java.util.*;

public abstract class Player {
    
    /**
     * ゲームの状態からプレイヤーの行動を決定する.
     * サブクラスで実装する必要がある.
     * @param state ゲームの状態
     * @return プレイヤーの行動
     */
    public abstract Action think(GameState state);
    
    /**
     * プレイヤーを初期化する.
     * @param name プレイヤーの名前
     * @param color プレイヤーの色
     */
    public Player(String name, Color color) {
        this.name = new String(name);
        this.color = color.clone();
    }
    
    /**
     * ゲームの結果を表示する.
     * サブクラスでオーバーライドすれば、自由な形式で表示できる.
     * @param state ゲームの状態
     * @param rank 各プレイヤーの順位
     */
    public void showResult(GameState state, List<Integer> rank) {
        // フィールドの状態
        System.out.println("[ Field ]");

        FieldState field = state.getFieldState();
        int width = field.getSize().getWidth();
        int height = field.getSize().getHeight();
        int playersCount = state.getPlayersCount();
        
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                int p = -1;
                
                // 位置(x, y)にプレイヤーがいるか確認
                for (int i = 0; i < playersCount; ++i) {
                    Point pos = state.getPlayerState(i).getPosition();
                    if (pos.getX() == x && pos.getY() == y) {
                        p = i;
                        break;
                    }
                }
                
                if (p >= 0) {
                    // プレイヤーがいたらプレイヤー番号を出力
                    System.out.print(p);
                } else {
                    // プレイヤーがいなかったら通行可能か否かを出力
                    if (field.getCellState(x, y).isPassable()) {
                        System.out.print("_");
                    } else {
                        System.out.print("X");
                    }
                }
            }
            
            System.out.println();
        }
        
        // 順位
        System.out.println("[ Ranking ]");
        int maxVal = 0;
        for (int i = 0; i < playersCount; ++i) {
            if (rank.get(i) > maxVal) {
                maxVal = rank.get(i);
            }
        }
        List<List<Integer>> rankSorted = new ArrayList<List<Integer>>();
        for (int r = 0; r < maxVal; ++r) {
            rankSorted.add(new ArrayList<Integer>());
        }
        for (int i = 0; i < playersCount; ++i) {
            rankSorted.get(rank.get(i) - 1).add(i);
        }
        for (int r = 0; r < maxVal; ++r) {
            System.out.println((r + 1) + "位:");
            for (Integer i : rankSorted.get(r)) {
                String name = state.getPlayerState(i).getName();
                System.out.println("  " + name + "(player" + i + ")");
            }
        }
    }
    
    /**
     * プレイヤーの名前を取得する.
     * @return プレイヤーの名前
     */
    public String getName() {
        return new String(name);
    }
    
    /**
     * プレイヤーの色を取得する.
     * @return プレイヤーの色
     */
    public Color getColor() {
        return color;
    }
    
    private String name;
    private Color color;
}
