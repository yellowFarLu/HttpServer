package model;

import javax.swing.ImageIcon;

// ����ģ��
public class HYTaskInfo {
	// ����
	public String content;
	
	// ͼƬURL���Ժ�ͻ���ͨ�����������������������ݿ�������Ҷ�Ӧ��ͼƬ��
	public String imageUrl;
	
	// �Ƿ���
	public boolean isRob;
	
	// �Ƿ����
	public boolean isCompelete;
	
	// �������ߵ��ֻ�����
	public String photoNumber;
	
	// ������
	public String money;
	
	private String sendTime;  // ����ʱ��

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
}
