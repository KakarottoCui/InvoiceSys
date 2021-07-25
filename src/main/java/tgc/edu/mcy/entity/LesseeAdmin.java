package tgc.edu.mcy.entity;

import javax.persistence.Entity;

//租户管理员
@Entity
public class LesseeAdmin extends SysUser {
	private String unit;	//使用单位
	private String phone;  //电话
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
}
