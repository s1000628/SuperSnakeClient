package snct_procon.supersnake.net;

import java.io.*;
import java.nio.*;

/**
 * SuperSnake でのデータ受信全般を行う.
 */
public class Receiver {
    
    /**
     * Receiver を初期化する.
     * @param stream
     */
    public Receiver(InputStream stream) {
        this.stream = stream;
    }
    
    /**
     * データの受信を開始する.
     */
    public void beginReceive() {
        // 既に受信処理中なら何もしない
        if (th != null) {
            return;
        }
        
        th = new ReceivingThread(this);
        received = false;
        new Thread(th).start();
    }
    
    /**
     * データの受信が完了したか否かを取得する.
     * @return データの受信が完了していれば true 、そうでなければ false
     */
    public boolean isReceived() {
        return received;
    }
    
    /**
     * 受信したデータの種類を取得する.
     * isReceived() が true の状態で取得しなければならない.
     * @return 受信したデータの種類
     */
    public DataType getDataType() {
        return dataType;
    }
    
    /**
     * 受信したデータの内容を取得する.
     * isReceived() が true の状態で取得しなければならない.
     * @return 受信したデータの内容
     */
    public byte[] getData() {
        return data;
    }
    
    /**
     * 受信したデータから数値を取り出す.
     * @param steram 受信したデータの InputStream
     * @return 受信した数値
     * @throws IOException
     */
    public static int readInt32(InputStream stream) throws IOException {
        byte[] buf = new byte[4];
        stream.read(buf);
        ByteBuffer bbuf = ByteBuffer.wrap(buf);
        bbuf.order(ByteOrder.LITTLE_ENDIAN);
        return bbuf.getInt();
    }
    
    /**
     * 受信したデータから文字列を取り出す.
     * @param stream 受信したデータの InputStream
     * @return 受信した文字列
     * @throws IOException
     */
    public static String readString(InputStream stream) throws IOException {
        int len = readInt32(stream);
        byte[] buf = new byte[len];
        stream.read(buf);
        return new String(buf, "UTF-8");
    }
    
    private class ReceivingThread implements Runnable {
        
        public ReceivingThread(Receiver receiver) {
            this.receiver = receiver;
        }
        
        @Override
        public void run() {
            try {
                byte[] buf = new byte[1024]; // データ一時受信用バッファ
                boolean receivedLen = false; // データ長を受信したか否か
                int dataLen = 0; // データ長
                ByteArrayOutputStream bs = new ByteArrayOutputStream(); // 受信バッファ
                
                while (!receivedLen) {
                    // 受信待ち
                    int len = receiver.stream.read(buf);
                    
                    // 受信バッファに追記
                    bs.write(buf, 0, len);
                    
                    // データ長受信
                    if (!receivedLen && bs.size() >= 4) {
                        dataLen = Receiver.readInt32(new ByteArrayInputStream(bs.toByteArray()));
                        receivedLen = true;
                    }
                    
                    // メインデータ受信
                    if (receivedLen && bs.size() >= 4 + dataLen) {
                        ByteArrayInputStream bis = new ByteArrayInputStream(bs.toByteArray());
                        bis.skip(4);
                        
                        // データの種類
                        receiver.dataType = DataType.getDataType((byte)bis.read());
                        
                        // データの内容
                        receiver.data = new byte[dataLen - 1];
                        bis.read(receiver.data);
                        
                        // 受信完了
                        receiver.received = true;
                    }
                } 
            }
            catch (IOException ex) {
            }
            finally {
                // 受信処理終了
                receiver.th = null;
            }
        }
        
        private volatile Receiver receiver;
    }
    
    private volatile InputStream stream;
    private volatile ReceivingThread th = null;
    private volatile DataType dataType = DataType.UNKNOWN;
    private volatile byte[] data = new byte[0];
    private volatile boolean received = false;
}
