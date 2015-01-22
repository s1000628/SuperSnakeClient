package snct_procon.supersnake.net;

/**
 * SuperSnake で通信するデータの種類を表す.
 */
public enum DataType {
    UNKNOWN(0),
    
    /**
     * サーバーからプレイヤーにゲームの状態を送信する.
     * 送信は毎ターン行われる.<br />
     * 参加中でかつ生きているプレイヤーは, これを受信したら思考を開始する.
     */
    GAME_STATE(1),
    
    /**
     * プレイヤーからサーバーに思考結果を送信する.
     */
    ANSWER(2),
    
    /**
     * サーバーからプレイヤーにゲームの情報を送信する.
     * ゲーム開始時に一度だけ送信される.
     */
    GAME_INFO(3),
    
    /**
     * プレイヤーからサーバーにプレイヤーの情報を送信する.
     * ゲーム開始前に一度だけ送信される.
     */
    PLAYER_INFO(4),
    
    /**
     * サーバーからプレイヤーにゲームの結果を送信する.
     * ゲーム終了後に一度だけ送信される.
     */
    GAME_RESULT(5),
    
    ;
    
    /**
     * 実際に送受信されるデータの種類識別番号を取得する.
     * @return データの種類識別番号
     */
    public byte getValue() {
        return v;
    }
    
    /**
     * データの種類識別番号から DataType を取得する.
     * @param value データの種類識別番号
     * @return DataType
     */
    public static DataType getDataType(byte value) {
        if (types == null) {
            types = DataType.values();
        }
        
        // 種類識別番号が一致する要素を探す
        for (DataType type : types) {
            if (type.v == value) {
                return DataType.valueOf(type.name());
            }
        }
        
        return DataType.UNKNOWN;
    }
    
    private DataType(int value) {
        this.v = (byte)value;
    }
    
    private byte v;
    private static DataType[] types = null;
}
