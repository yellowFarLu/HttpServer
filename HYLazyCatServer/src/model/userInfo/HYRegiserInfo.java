package model.userInfo;

/* 用户注册新消息的模型 */

public class HYRegiserInfo extends HYBaseUserInfo{
	
	
	// 诚信分
	public int honestyScore;
	
	public HYRegiserInfo() {
		// TODO Auto-generated constructor stub
		this.honestyScore = 100;
	}
	
	// 注册的时候，诚信分初始化为100
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
			this.useName = "未命名用户";
			return;
		}
		this.useName = useName;
	}

	public String getPerId() {
		return perId;
	}

	public void setPerId(String perId) {
		if (perId == null) {
			this.perId = "未认证身份";
			return;
		}
		this.perId = perId;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		if (schoolName == null) {
			this.schoolName = "未制定学校";
			return;
		}
		
		this.schoolName = schoolName;
	}
	
	
}
