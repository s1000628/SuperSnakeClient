
import snct_procon.supersnake.*;
import snct_procon.supersnake.net.*;

import java.net.*;
import java.io.*;

public class SuperSnakeClientMain {

    public static void main(String[] args) throws IOException, InterruptedException {
        // �ڑ���T�[�o�[
        final String host = "localhost";
        final int port = 12345;

        // �v���C���[
        Player player = new ConsolePlayer();
        
        try (SuperSnakeClient client = new SuperSnakeClient(player)) {
            // �ڑ�
            client.connect(host, port);
            
            while (true) {
                // ���̃^�[���̊J�n�҂�
                client.waitForNextTurn();
                
                // �Q�[�����I��������A�Q�[���̌��ʂ�\�����ďI��
                if (client.isGameover()) {
                    client.showResult();
                    break;
                }
                
                // �s�����l����
                client.decideAction();
            }
        }
    }

}
