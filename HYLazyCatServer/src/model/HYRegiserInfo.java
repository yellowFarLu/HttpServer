package model;

/* �û�ע������Ϣ��ģ�� */

public class HYRegiserInfo {
	// �ֻ�����
	public String photoNumber;
	// �û��ǳ�
	public String useName;
	// ����
	public String pwd;
	// �Ա�
	public String sex;
	// ���֤����
	public String perId;
	// ѧУ����
	public String schoolName;
	// ���ŷ�
	public int honestyScore;
	// �Ƿ�������
	public boolean isRobTaskPerson;
	// �Ƿ�ӵ���
	public boolean isSendTaskPerson;
	
	public HYRegiserInfo() {
		// TODO Auto-generated constructor stub
		this.honestyScore = 100;
	}
	
	// ע���ʱ�򣬳��ŷֳ�ʼ��Ϊ100
	public void setHonestyScore(int honestyScore) {
		if (honestyScore == 0) {
			honestyScore = 100;
		}
		this.honestyScore = honestyScore;
	}
	
	
}
