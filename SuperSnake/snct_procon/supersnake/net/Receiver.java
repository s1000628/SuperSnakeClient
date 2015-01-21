package snct_procon.supersnake.net;

import java.io.*;
import java.nio.*;

/**
 * SuperSnake �ł̃f�[�^��M�S�ʂ��s��.
 */
public class Receiver {
    
    /**
     * Receiver ������������.
     * @param stream
     */
    public Receiver(InputStream stream) {
        this.stream = stream;
    }
    
    /**
     * �f�[�^�̎�M���J�n����.
     */
    public void beginReceive() {
        // ���Ɏ�M�������Ȃ牽�����Ȃ�
        if (th != null) {
            return;
        }
        
        th = new ReceivingThread(this);
        received = false;
        new Thread(th).start();
    }
    
    /**
     * �f�[�^�̎�M�������������ۂ����擾����.
     * @return �f�[�^�̎�M���������Ă���� true �A�����łȂ���� false
     */
    public boolean isReceived() {
        return received;
    }
    
    /**
     * ��M�����f�[�^�̎�ނ��擾����.
     * isReceived() �� true �̏�ԂŎ擾���Ȃ���΂Ȃ�Ȃ�.
     * @return ��M�����f�[�^�̎��
     */
    public DataType getDataType() {
        return dataType;
    }
    
    /**
     * ��M�����f�[�^�̓��e���擾����.
     * isReceived() �� true �̏�ԂŎ擾���Ȃ���΂Ȃ�Ȃ�.
     * @return ��M�����f�[�^�̓��e
     */
    public byte[] getData() {
        return data;
    }
    
    /**
     * ��M�����f�[�^���琔�l�����o��.
     * @param steram ��M�����f�[�^�� InputStream
     * @return ��M�������l
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
     * ��M�����f�[�^���當��������o��.
     * @param stream ��M�����f�[�^�� InputStream
     * @return ��M����������
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
                byte[] buf = new byte[1024]; // �f�[�^�ꎞ��M�p�o�b�t�@
                boolean receivedLen = false; // �f�[�^������M�������ۂ�
                int dataLen = 0; // �f�[�^��
                ByteArrayOutputStream bs = new ByteArrayOutputStream(); // ��M�o�b�t�@
                
                while (!receivedLen) {
                    // ��M�҂�
                    int len = receiver.stream.read(buf);
                    
                    // ��M�o�b�t�@�ɒǋL
                    bs.write(buf, 0, len);
                    
                    // �f�[�^����M
                    if (!receivedLen && bs.size() >= 4) {
                        dataLen = Receiver.readInt32(new ByteArrayInputStream(bs.toByteArray()));
                        receivedLen = true;
                    }
                    
                    // ���C���f�[�^��M
                    if (receivedLen && bs.size() >= 4 + dataLen) {
                        ByteArrayInputStream bis = new ByteArrayInputStream(bs.toByteArray());
                        bis.skip(4);
                        
                        // �f�[�^�̎��
                        receiver.dataType = DataType.getDataType((byte)bis.read());
                        
                        // �f�[�^�̓��e
                        receiver.data = new byte[dataLen - 1];
                        bis.read(receiver.data);
                        
                        // ��M����
                        receiver.received = true;
                    }
                } 
            }
            catch (IOException ex) {
            }
            finally {
                // ��M�����I��
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
