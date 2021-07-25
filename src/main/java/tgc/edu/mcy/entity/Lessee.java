package tgc.edu.mcy.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.springframework.data.annotation.CreatedBy;

//租户
@Entity
public class Lessee {
	private Integer id;
	private String phone;    //电话
	private String name;   //租户名称
	private String linkman;  //联系人
	private LesseeAdmin lesseeAdmin;
	private Integer number;    //电子发票数量
	@Id
	@GeneratedValue
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	@ManyToOne
	@CreatedBy
	public LesseeAdmin getLesseeAdmin() {
		return lesseeAdmin;
	}
	public void setLesseeAdmin(LesseeAdmin lesseeAdmin) {
		this.lesseeAdmin = lesseeAdmin;
	}
	public String getLinkman() {
		return linkman;
	}
	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
}
