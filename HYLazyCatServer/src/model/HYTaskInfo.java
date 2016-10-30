package model;

import javax.swing.ImageIcon;

// 任务模型
public class HYTaskInfo {
	// 内容
	public String content;
	
	// 图片URL（以后客户端通过该属性来到服务器的数据库里面查找对应的图片）
	public String imageUrl;
	
	// 是否被抢
	public boolean isRob;
	
	// 是否完成
	public boolean isCompelete;
	
	// 发任务者的手机号码
	public String photoNumber;
	
	// 任务金额
	public String money;
}
