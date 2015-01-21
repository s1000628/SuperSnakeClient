package snct_procon.supersnake.net;

import java.io.*;
import java.nio.*;

/**
 * SuperSnake でのデータ送信全般を行う.
 */
public class Sender {
    
    /**
     * Sender を初期化する.
     * @param stream Socket の OutputStream
     */
    public Sender(OutputStream stream) {
        this.stream = stream;
    }
    
    /**
     * バイト列を送信する.
     * @param type 送信するデータの種類
     * @param data 送信するバイト列
     */
    public void beginSend(DataType type, byte[] data) {
        // ヘッダを付与
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        
        try {
            // データ長
            Sender.writeInt32(bs, data.length + 1);
            
            // データの種類
            bs.write(new byte[] { type.getValue() });
            
            // メインデータ
            bs.write(data);
        }
        catch (IOException ex) {
        }
        
        // 送信開始
        SendingThread th = new SendingThread(stream);
        th.buf = bs.toByteArray();
        new Thread(th).start();
    }
    
    /**
     * バイト列を送信する.
     * @param type 送信するデータの種類
     * @param data 送信するバイト列を格納した ByteArrayOutputStream
     */
    public void beginSend(DataType type, ByteArrayOutputStream data) {
        beginSend(type, data.toByteArray());
    }
    
    /**
     * 数値を送信用データに変換して OutputStream に書き込む.
     * @param stream 書き込み先 OutputStream
     * @param data 書き込む数値
     * @throws IOException
     */
    public static void writeInt32(OutputStream stream, int data) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(4);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        buf.putInt(data);
        stream.write(buf.array());
    }
    
    /**
     * 文字列を送信用データに変換して OutputStream に書き込む.
     * @param stream 書き込み先 OutputStream
     * @param data 書き込む文字列
     * @throws IOException
     */
    public static void writeString(OutputStream stream, String data) throws IOException {
        byte[] buf = data.getBytes("UTF-8");
        writeInt32(stream, buf.length);
        stream.write(buf);
    }
    
    private class SendingThread implements Runnable {
        
        public SendingThread(OutputStream stream) {
            this.stream = stream;
        }
        
        public byte[] buf;
        
        @Override
        public void run() {
            try {
                synchronized (stream) {
                    stream.write(buf);
                }
            }
            catch (IOException ex) {
            }
        }

        private OutputStream stream;
    }
    
    private OutputStream stream;
}
