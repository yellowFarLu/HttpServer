package model;

/* 用户注册新消息的模型 */

public class HYRegiserInfo {
	// 手机号码
	public String photoNumber;
	// 用户昵称
	public String useName;
	// 密码
	public String pwd;
	// 性别
	public String sex;
	// 身份证号码
	public String perId;
	// 学校名字
	public String schoolName;
	// 诚信分
	public int honestyScore;
	// 是否抢单者
	public boolean isRobTaskPerson;
	// 是否接单者
	public boolean isSendTaskPerson;
	
	public HYRegiserInfo() {
		// TODO Auto-generated constructor stub
		this.honestyScore = 100;
	}
	
	// 注册的时候，诚信分初始化为100
	public void setHonestyScore(int honestyScore) {
		if (honestyScore == 0) {
			honestyScore = 100;
		}
		this.honestyScore = honestyScore;
	}
	
	
}
