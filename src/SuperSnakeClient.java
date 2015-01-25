
import java.io.*;
import java.net.*;
import java.util.*;

import snct_procon.supersnake.*;
import snct_procon.supersnake.net.*;

/**
 * SuperSnake �̃N���C�A���g.
 */
public class SuperSnakeClient implements AutoCloseable {
    
    /**
     * �|�[�g�ԍ����w�肵�Ȃ������ꍇ�Ɏg�p����|�[�g�ԍ�
     */
    public final int defaultPort = 12345;
    
    /**
     * SuperSnakeClient ������������.
     * @param player �g�p����v���C���[
     */
    public SuperSnakeClient(Player player) {
        this.player = player;
    }
    
    /**
     * SuperSnake �T�[�o�[�ɐڑ�����.
     * �ڑ��̊m���A�v���C���[���̑��M�A�Q�[�����̎�M���s��.
     * @param host �ڑ���z�X�g��
     * @param port �ڑ���|�[�g�ԍ�
     * @throws IOException �\�P�b�g���ł̓��o�̓G���[�������ɔ�������.
     * @throws InterruptedException ���荞�ݔ������ɔ�������.
     */
    public void connect(String host, int port) throws IOException, InterruptedException {
        // �ڑ�
        socket = new Socket(host, port);
        sender = new Sender(socket.getOutputStream());
        receiver = new Receiver(socket.getInputStream());
        
        // �����̏��𑗐M
        data.setMyPlayerName(player.getName());
        data.setMyPlayerColor(player.getColor());
        sender.beginSend(DataType.PLAYER_INFO, data.getPlayerInfoBytes());
        
        // �Q�[���̏�����M
        do {
            receiver.beginReceive();
            while (!receiver.isReceived()) {
                Thread.sleep(10);
            }
        } while (receiver.getDataType() != DataType.GAME_INFO);
        data.setGameInfo(receiver.getData());
    }

    /**
     * SuperSnake �T�[�o�[�ɐڑ�����.
     * �ڑ��̊m���A�v���C���[���̑��M�A�Q�[�����̎�M���s��.
     * @param serverName �ڑ���
     * @throws IOException �\�P�b�g���ł̓��o�̓G���[�������ɔ�������.
     * @throws InterruptedException ���荞�ݔ������ɔ�������.
     * @throws NumberFormatException �|�[�g�ԍ����s���̏ꍇ�ɔ�������.
     */
    public void connect(String serverName) throws IOException, InterruptedException, NumberFormatException {
        String[] sp = serverName.split(":");
        if (sp.length >= 2) {
            this.connect(sp[0], Integer.parseInt(sp[1]));
        } else {
            this.connect(sp[0], defaultPort);
        }
    }
    
    /**
     * ���̃^�[�����J�n����܂őҋ@����.
     * �Q�[���̏�Ԃ܂��̓Q�[���̌��ʂ���M����܂őҋ@����.
     * @throws InterruptedException ���荞�ݔ������ɔ�������.
     * @throws SocketException �T�[�o�[�Ƃ̒ʐM���ؒf���ꂽ�ꍇ�ɔ�������.
     */
    public void waitForNextTurn() throws InterruptedException, SocketException {
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
     * ���̍s�������肷��.
     */
    public void decideAction() {
        Action action = player.think(data.getGameState());
        sender.beginSend(DataType.ANSWER, data.getActionBytes(action));
    }
    
    /**
     * �Q�[���̏�Ԃ�\������.
     */
    public void showGameState() {
        player.showGameState(data.getGameState());
    }
    
    /**
     * �Q�[���̌��ʂ�\������.
     * isGameover() �� true �̏�ԂŌĂяo���Ȃ���΂Ȃ�Ȃ�.
     */
    public void showResult() {
        List<Integer> rank = new ArrayList<Integer>();
        for (int i = 0; i < data.getPlayersCount(); ++i) {
            rank.add(data.getRank(i));
        }
        player.showResult(data.getGameState(), rank);
    }

    /**
     * �Q�[�����I�����Ă��邩�ۂ����擾����.
     * @return �Q�[�����I�����Ă���� true �A�����łȂ���� false
     */
    public boolean isGameover() {
        return data.isGameover();
    }
    
    /**
     * �v���C���[�������Ă��邩�ۂ����擾����.
     * @return �v���C���[�������Ă���� true �A�����łȂ���� false
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
