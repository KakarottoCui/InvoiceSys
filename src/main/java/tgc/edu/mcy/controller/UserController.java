package tgc.edu.mcy.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

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
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import tgc.edu.mcy.custom.AjaxResult;
import tgc.edu.mcy.custom.JournalUtil;
import tgc.edu.mcy.custom.JpgridUtils;
import tgc.edu.mcy.entity.Journal;
import tgc.edu.mcy.entity.Lessee;
import tgc.edu.mcy.entity.SysRole;
import tgc.edu.mcy.entity.SysUser;
import tgc.edu.mcy.entity.User;
import tgc.edu.mcy.repository.RoleRepository;
import tgc.edu.mcy.security.UserUtils;
import tgc.edu.mcy.service.JournalService;
import tgc.edu.mcy.service.LesseeService;
import tgc.edu.mcy.service.SysUserService;
import tgc.edu.mcy.service.UserService;

@Controller
@RequestMapping(value="/user")
public class UserController {
	@Autowired
	private UserService userDAO;
	@Autowired
	private LesseeService lesseeDAO;
	@Autowired
	private UserUtils userUtils;
	@Autowired
	private RoleRepository roleDAO;
	@Autowired
	private SysUserService userDAO1;
	@Autowired
	private JournalService journalDAO;
	
	@RequestMapping(value="/all")
	public String all(ModelMap map) {
		SysUser user = userUtils.getUser();
		List<Lessee> list = lesseeDAO.findByLesseeAdminId(user.getId());
		map.put("lessee", list);
		return "admin/user_all";
	}
	
	//验证用户名是否已添加
	@RequestMapping(value="/username")
	@ResponseBody
	public Boolean username(String username) {
		SysUser user = userDAO1.findByUsername(username);
		if(user == null) {
			return false;
		}else {
			return true;
		}
	}
	
	//用户修改密码
	@RequestMapping(value="/updata")
	public String updata() {
		return "user/updata";
	}
	
	//显示所有用户
	@RequestMapping(value="/list")
	@ResponseBody
	public Object list(JpgridUtils form, String name, String username) {
		SysUser user = userUtils.getUser();
		Pageable pageable = form.buildPageable();
		Page<User> page = null;
		Specification<User> spec = new Specification<User>() {

			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				HashSet<Predicate> rules = new HashSet<>();
				if(StringUtils.hasText(name)) {
					rules.add(criteriaBuilder.like(root.get("name"), "%"+name+"%"));
				}else if(StringUtils.hasText(username)) {
					rules.add(criteriaBuilder.like(root.get("username"), "%"+username+"%"));
				}
				rules.add(criteriaBuilder.equal(root.get("lessee").get("lesseeAdmin").get("id"), user.getId()));
				return criteriaBuilder.and(rules.toArray(new Predicate[rules.size()]));
			}			
		};
		page = userDAO.findAll(spec, pageable);
		HashMap<String , Object> result = form.getPageResult(page);
		return result;
	}
	
	//重置密码
	@RequestMapping(value="/cz")
	@ResponseBody
	public String cz(Integer id) {
		SysUser user = userDAO1.findById(id);
		BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
		user.setPassword(encoder.encode("111111"));
		userDAO1.save(user);
		return "重置成功！";
	}
	
	//新增用户
	@RequestMapping(value="/save")
	@ResponseBody
	public Object save(User model) {
		BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();   //加密类
		SysRole role=roleDAO.findById(3).get();
		SysUser user = userUtils.getUser();
		if((model.getPassword() == null || model.getPassword() == "")) {
			model.setPassword(encoder.encode("111111"));
			model.setSf(1);
			
			//添加用户日志
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
			String data = df.format(new Date());
			Journal journal = new Journal();
			journal.setDate(data);
			journal.setUsername(user.getUsername());
			journal.setOperationName("添加用户");
			System.out.println(journal.getOperationName());
			journalDAO.save(journal);
			/*JournalUtil.log(user.getUsername(), "添加用户");*/
		}
		model.getRoles().add(role);
		userDAO.save(model);
		return AjaxResult.build(true, "OK");
	}
	
	//删除
	@RequestMapping(value="/delete")
	@ResponseBody
	public void delete(String ids) {
		String[] id = ids.split(","); 
		for(int i = 0; i < id.length; i++) {
			userDAO.deleteById(Integer.parseInt(id[i]));
		}
	}
}
