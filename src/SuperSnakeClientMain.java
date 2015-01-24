
import snct_procon.supersnake.*;

import java.io.*;
import java.net.*;

public class SuperSnakeClientMain {

    public static void main(String[] args) throws IOException, InterruptedException {
        // �ڑ���T�[�o�[
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("�ڑ�������(host:port) > ");
        String serverName = in.readLine();

        // �v���C���[
        Player player = new ConsolePlayer();
        
        try (SuperSnakeClient client = new SuperSnakeClient(player)) {
            // �ڑ�
            try {
                client.connect(serverName);
            }
            catch (Exception ex) {
                System.err.println("�T�[�o�[�ւ̐ڑ��Ɏ��s���܂����B");
                return;
            }
            
            try {
                while (true) {
                    // ���̃^�[���̊J�n�҂�
                    client.waitForNextTurn();
                    
                    // �Q�[�����I��������A�Q�[���̌��ʂ�\�����ďI��
                    if (client.isGameover()) {
                        client.showResult();
                        break;
                    }
                    
                    // �Q�[���̏�Ԃ�\��
                    client.showGameState();
                    
                    // �s�����l����
                    if (client.isAlive()) {
                        client.decideAction();
                    }
                }
            }
            catch (SocketException ex) {
                System.err.println("�T�[�o�[�Ƃ̒ʐM���ؒf����܂����B");
            }
        }
    }

}
