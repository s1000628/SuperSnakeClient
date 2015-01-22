package snct_procon.supersnake.net;

/**
 * SuperSnake �ŒʐM����f�[�^�̎�ނ�\��.
 */
public enum DataType {
    UNKNOWN(0),
    
    /**
     * �T�[�o�[����v���C���[�ɃQ�[���̏�Ԃ𑗐M����.
     * ���M�͖��^�[���s����.<br />
     * �Q�����ł������Ă���v���C���[��, �������M������v�l���J�n����.
     */
    GAME_STATE(1),
    
    /**
     * �v���C���[����T�[�o�[�Ɏv�l���ʂ𑗐M����.
     */
    ANSWER(2),
    
    /**
     * �T�[�o�[����v���C���[�ɃQ�[���̏��𑗐M����.
     * �Q�[���J�n���Ɉ�x�������M�����.
     */
    GAME_INFO(3),
    
    /**
     * �v���C���[����T�[�o�[�Ƀv���C���[�̏��𑗐M����.
     * �Q�[���J�n�O�Ɉ�x�������M�����.
     */
    PLAYER_INFO(4),
    
    /**
     * �T�[�o�[����v���C���[�ɃQ�[���̌��ʂ𑗐M����.
     * �Q�[���I����Ɉ�x�������M�����.
     */
    GAME_RESULT(5),
    
    ;
    
    /**
     * ���ۂɑ���M�����f�[�^�̎�ގ��ʔԍ����擾����.
     * @return �f�[�^�̎�ގ��ʔԍ�
     */
    public byte getValue() {
        return v;
    }
    
    /**
     * �f�[�^�̎�ގ��ʔԍ����� DataType ���擾����.
     * @param value �f�[�^�̎�ގ��ʔԍ�
     * @return DataType
     */
    public static DataType getDataType(byte value) {
        if (types == null) {
            types = DataType.values();
        }
        
        // ��ގ��ʔԍ�����v����v�f��T��
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
