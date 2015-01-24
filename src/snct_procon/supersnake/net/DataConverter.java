package snct_procon.supersnake.net;

import snct_procon.supersnake.*;
import java.util.*;
import java.io.*;

/**
 * SuperSnake �ŗp����e��ʐM�p�f�[�^�̕ϊ����s��.
 */
public class DataConverter {
    
    public DataConverter() {
    }
    
    /**
     * �����̃v���C���[�̖��O��ݒ肷��.
     * @param value �v���C���[�̖��O
     */
    public void setMyPlayerName(String value) {
        this.myName = new String(value);
    }
    
    /**
     * �����̃v���C���[�̐F��ݒ肷��.
     * @param color �v���C���[�̐F
     */
    public void setMyPlayerColor(Color color) {
        this.myColor = color.clone();
    }
    
    /**
     * �����̃v���C���[�ԍ����擾����.
     * @return �v���C���[�ԍ�
     */
    public int getMyPlayerNumber() {
        return myPlayerNum;
    }
    
    /**
     * �t�B�[���h�̏�Ԃ��擾����.
     * @return �t�B�[���h�̏��
     */
    public FieldState getFieldState() {
        return field;
    }
    
    /**
     * �v���C���[�����擾����.
     * @return �v���C���[��
     */
    public int getPlayersCount() {
        return players.length;
    }
    
    /**
     * �v���C���[�̏�Ԃ��擾����.
     * @param playerNumber �擾����v���C���[�̃v���C���[�ԍ�
     * @return �v���C���[�̏��
     */
    public PlayerState getPlayerState(int playerNumber) {
        return players[playerNumber];
    }
    
    /**
     * �v���C���[�̏��ʂ��擾����.
     * @param playerNumber �擾����v���C���[�̃v���C���[�ԍ�
     * @return �v���C���[�̈ʒu
     */
    public int getRank(int playerNumber) {
        return rank[playerNumber];
    }
    
    /**
     * �Q�[�����I�����Ă��邩�ۂ����擾����.
     * @return �Q�[�����I�����Ă���� true �A�����łȂ���� false
     */
    public boolean isGameover() {
        return gameover;
    }
    
    /**
     * �Q�[���̏�Ԃ��擾����.
     * @return �Q�[���̏��
     */
    public GameState getGameState() {
        List<PlayerState> playerStates = new ArrayList<PlayerState>();
        for (PlayerState player : players) {
            playerStates.add(player);
        }
        return new GameState(field, playerStates);
    }
    
    /**
     * �v���C���[�̏�񑗐M�p�̃o�C�g��𐶐�����.
     * @return ���M�p�o�C�g��
     */
    public byte[] getPlayerInfoBytes() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            Sender.writeString(bos, myName);
        }
        catch (IOException ex) {
            // ByteArrayOutputStream.write() �̌Ăяo���Ȃ̂� IO �G���[�͔������Ȃ�
        }
        bos.write(myColor.getR());
        bos.write(myColor.getG());
        bos.write(myColor.getB());
        return bos.toByteArray();
    }
    
    /**
     * �v���C���[�̍s�����M�p�̃o�C�g��𐶐�����.
     * @param action �v���C���[�̍s��
     * @return ���M�p�o�C�g��
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
     * ��M�f�[�^����Q�[���̏���ݒ肷��.
     * @param data ��M�f�[�^�̃o�C�g��
     */
    public void setGameInfo(byte[] data) {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        
        try {
            // �t�B�[���h��
            String fieldName = Receiver.readString(bis);
            
            // �t�B�[���h�T�C�Y
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
            
            // �v���C���[�l��
            int n = Receiver.readInt32(bis);
            players = new PlayerState[n];
            
            // �v���C���[
            for (int i = 0; i < n; ++i) {
                // �v���C���[�̖��O
                String name = Receiver.readString(bis);
                
                // �v���C���[�̐F
                int r = bis.read();
                int g = bis.read();
                int b = bis.read();
                Color color = new Color(r, g, b);
                
                players[i] = new PlayerState(name, color, new Point(0, 0), Direction.RIGHT, true);
            }
            
            // �����̃v���C���[�ԍ�
            myPlayerNum = Receiver.readInt32(bis);
        }
        catch (IOException ex) {
            // ByteArrayInputStream.read() �̌Ăяo���Ȃ̂� IO �G���[�͔������Ȃ�
        }
    }
  
    /**
     * ��M�f�[�^����Q�[���̏�Ԃ�ݒ肷��.
     * @param data ��M�f�[�^�̃o�C�g��
     */
    public void setGameState(byte[] data) {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        
        try {
            // �t�B�[���h�T�C�Y
            int w = Receiver.readInt32(bis);
            int h = Receiver.readInt32(bis);
            Size size = new Size(w, h);
            
            // �Z��
            CellState[][] cellsArr = new CellState[w][h];
            for (int y = 0; y < h; ++y) {
                for (int x = 0; x < w; ++x) {
                    // �ʍs�\�t���O
                    boolean passable = (bis.read() != 0);
                    
                    // �F
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
            
            // �v���C���[�l��
            int n = Receiver.readInt32(bis);
            
            // �v���C���[
            for (int i = 0; i < n; ++i) {
                // �ʒu
                int x = Receiver.readInt32(bis);
                int y = Receiver.readInt32(bis);
                Point pos = new Point(x, y);
                
                // ����
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
                
                // ����
                boolean alive = (bis.read() == 0);
                
                players[i] = new PlayerState(players[i].getName(), players[i].getColor(), pos, dir, alive);
            }
        }
        catch (IOException ex) {
            // ByteArrayInputStream.read() �̌Ăяo���Ȃ̂� IO �G���[�͔������Ȃ�
        }
    }
    
    /**
     * ��M�f�[�^����Q�[���̌��ʂ�ݒ肷��B
     * @param data ��M�f�[�^�̃o�C�g��
     */
    public void setGameResult(byte[] data) {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        
        try {
            // �t�B�[���h�T�C�Y
            int w = Receiver.readInt32(bis);
            int h = Receiver.readInt32(bis);
            Size size = new Size(w, h);
            
            // �Z��
            CellState[][] cellsArr = new CellState[w][h];
            for (int y = 0; y < h; ++y) {
                for (int x = 0; x < w; ++x) {
                    // �ʍs�\�t���O
                    boolean passable = (bis.read() != 0);
                    
                    // �F
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
            
            // �v���C���[�l��
            int n = Receiver.readInt32(bis);
            
            // �v���C���[
            for (int i = 0; i < n; ++i) {
                // �ʒu
                int x = Receiver.readInt32(bis);
                int y = Receiver.readInt32(bis);
                Point pos = new Point(x, y);
                
                // ����
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
                
                // ����
                boolean alive = (bis.read() == 0);
                
                players[i] = new PlayerState(players[i].getName(), players[i].getColor(), pos, dir, alive);
            }
            
            // ����
            rank = new int[n];
            for (int i = 0; i < n; ++i) {
                rank[i] = Receiver.readInt32(bis);
            }
            
            // �Q�[���I�[�o�[
            gameover = true;
        }
        catch (IOException ex) {
            // ByteArrayInputStream.read() �̌Ăяo���Ȃ̂� IO �G���[�͔������Ȃ�
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
