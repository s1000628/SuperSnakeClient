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
     * ゲームの状態を表示する.
     * プレイヤーの生死に関わらず、毎ターンの始めに呼び出される.
     * サブクラスでオーバーライドすれば、自由な形式で表示できる.
     * @param state
     */
    public void showGameState(GameState state) {
        // フィールドの状態
        System.out.println("[ Field ]");
        showField(state);
        
        // プレイヤーの状態
        System.out.println("[ Players ]");
        int playersCount = state.getPlayersCount();
        for (int i = 0; i < playersCount; ++i) {
            PlayerState player = state.getPlayerState(i);
            System.out.println(
                player.getName() + "(player" + i + "):"
                + (player.isDead() ? " (死亡)" : "")
                + " (" + player.getPosition().getX() + ", " + player.getPosition().getY() + ")"
                + " " + player.getDirection().name()
                );
        }
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
        showField(state);
        
        // 順位
        System.out.println("[ Ranking ]");
        int playersCount = state.getPlayersCount();
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

    private void showField(GameState state) {
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
