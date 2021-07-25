package tgc.edu.mcy.controller;

import java.util.HashMap;
import java.util.HashSet;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import tgc.edu.mcy.custom.AjaxResult;
import tgc.edu.mcy.custom.JpgridUtils;
import tgc.edu.mcy.entity.LesseeAdmin;
import tgc.edu.mcy.entity.SysRole;
import tgc.edu.mcy.entity.SysUser;
import tgc.edu.mcy.entity.User;
import tgc.edu.mcy.repository.RoleRepository;
import tgc.edu.mcy.service.LesseeAdminService;
import tgc.edu.mcy.service.SysUserService;

@Controller
@RequestMapping(value="/lesseeAdmin")
public class LessseeAdminController {
	@Autowired
	private LesseeAdminService lesseeAdminDAO;
	@Autowired
	private SysUserService sysUserDAO;
	@Autowired
	private RoleRepository roleDAO;
	
	@RequestMapping(value="/all")
	public String all() {
		return "system/lesseeAdmin_all";
	}
	
	@RequestMapping(value="/update")
	public String update() {
		return "admin/update";
	}
	
	@RequestMapping(value="/cz")
	@ResponseBody
	public String cz(Integer id) {
		SysUser user = sysUserDAO.findById(id);
		BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
		user.setPassword(encoder.encode("111111"));
		sysUserDAO.save(user);
		return "重置成功！";
	}
	
	@RequestMapping(value="/username")
	@ResponseBody
	public Boolean username(String username) {
		SysUser user = sysUserDAO.findByUsername(username);
		if(user == null) {
			return false;
		}else {
			return true;
		}
	}
	
	//显示所有租户
	@RequestMapping(value="/list")
	@ResponseBody
	public Object list(JpgridUtils form, String unit, String name) {
		Pageable pageable = form.buildPageable();
		Page<LesseeAdmin> page = null;
		Specification<LesseeAdmin> spec = new Specification<LesseeAdmin>() {

			@Override
			public Predicate toPredicate(Root<LesseeAdmin> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				HashSet<Predicate> rules = new HashSet<>();
				if(StringUtils.hasText(name)) {
					rules.add(criteriaBuilder.like(root.get("name"), "%"+name+"%"));
				}else if(StringUtils.hasText(unit)) {
					rules.add(criteriaBuilder.like(root.get("unit"), "%"+unit+"%"));
				}
				return criteriaBuilder.and(rules.toArray(new Predicate[rules.size()]));
			}			
		};
		page = lesseeAdminDAO.findAll(spec, pageable);
		HashMap<String , Object> result = form.getPageResult(page);
		return result;
	}
	
	//添加租户管理员
	@RequestMapping(value="/save")
	@ResponseBody
	public Object save(LesseeAdmin model) {
		BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();   //加密类
		SysRole role=roleDAO.findById(2).get();
		model.setPassword(encoder.encode("111111"));
		model.getRoles().add(role);
		lesseeAdminDAO.save(model);
		return AjaxResult.build(true, "OK");
	}
	
}
