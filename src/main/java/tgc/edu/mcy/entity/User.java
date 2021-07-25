package tgc.edu.mcy.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.springframework.data.annotation.CreatedBy;

//用户
@Entity
public class User extends SysUser {
	private String remark;   //备注
	private Lessee lessee;  //租户

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	@ManyToOne
	@CreatedBy
	public Lessee getLessee() {
		return lessee;
	}

	public void setLessee(Lessee lessee) {
		this.lessee = lessee;
	}
	
}
