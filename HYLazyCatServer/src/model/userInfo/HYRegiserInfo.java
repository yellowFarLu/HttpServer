package model.userInfo;

/* �û�ע������Ϣ��ģ�� */

public class HYRegiserInfo extends HYBaseUserInfo{
	
	
	// ���ŷ�
	public int honestyScore;
	
	public HYRegiserInfo() {
		// TODO Auto-generated constructor stub
		this.honestyScore = 100;
	}
	
	// ע���ʱ�򣬳��ŷֳ�ʼ��Ϊ100
	public void setHonestyScore(int honestyScore) {
		if (honestyScore == 0) {
			honestyScore = 100;
			return;
		}
		this.honestyScore = honestyScore;
	}

	public String getUseName() {
		return useName;
	}

	public void setUseName(String useName) {
		if (useName == null) {
			this.useName = "δ�����û�";
			return;
		}
		this.useName = useName;
	}

	public String getPerId() {
		return perId;
	}

	public void setPerId(String perId) {
		if (perId == null) {
			this.perId = "δ��֤���";
			return;
		}
		this.perId = perId;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		if (schoolName == null) {
			this.schoolName = "δ�ƶ�ѧУ";
			return;
		}
		
		this.schoolName = schoolName;
	}
	
	
}
