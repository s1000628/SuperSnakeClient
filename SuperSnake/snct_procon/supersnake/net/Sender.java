package snct_procon.supersnake.net;

import java.io.*;
import java.nio.*;

/**
 * SuperSnake �ł̃f�[�^���M�S�ʂ��s��.
 */
public class Sender {
    
    /**
     * Sender ������������.
     * @param stream Socket �� OutputStream
     */
    public Sender(OutputStream stream) {
        this.stream = stream;
    }
    
    /**
     * �o�C�g��𑗐M����.
     * @param type ���M����f�[�^�̎��
     * @param data ���M����o�C�g��
     */
    public void beginSend(DataType type, byte[] data) {
        // �w�b�_��t�^
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        
        try {
            // �f�[�^��
            Sender.writeInt32(bs, data.length + 1);
            
            // �f�[�^�̎��
            bs.write(new byte[] { type.getValue() });
            
            // ���C���f�[�^
            bs.write(data);
        }
        catch (IOException ex) {
        }
        
        // ���M�J�n
        SendingThread th = new SendingThread(stream);
        th.buf = bs.toByteArray();
        new Thread(th).start();
    }
    
    /**
     * �o�C�g��𑗐M����.
     * @param type ���M����f�[�^�̎��
     * @param data ���M����o�C�g����i�[���� ByteArrayOutputStream
     */
    public void beginSend(DataType type, ByteArrayOutputStream data) {
        beginSend(type, data.toByteArray());
    }
    
    /**
     * ���l�𑗐M�p�f�[�^�ɕϊ����� OutputStream �ɏ�������.
     * @param stream �������ݐ� OutputStream
     * @param data �������ސ��l
     * @throws IOException
     */
    public static void writeInt32(OutputStream stream, int data) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(4);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        buf.putInt(data);
        stream.write(buf.array());
    }
    
    /**
     * ������𑗐M�p�f�[�^�ɕϊ����� OutputStream �ɏ�������.
     * @param stream �������ݐ� OutputStream
     * @param data �������ޕ�����
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
