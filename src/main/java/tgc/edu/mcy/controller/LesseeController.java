package tgc.edu.mcy.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

import tgc.edu.mcy.custom.JpgridUtils;
import tgc.edu.mcy.entity.Accredit;
import tgc.edu.mcy.entity.Journal;
import tgc.edu.mcy.entity.Lessee;
import tgc.edu.mcy.entity.LesseeAdmin;
import tgc.edu.mcy.entity.SysUser;
import tgc.edu.mcy.security.UserUtils;
import tgc.edu.mcy.service.AccreditService;
import tgc.edu.mcy.service.JournalService;
import tgc.edu.mcy.service.LesseeAdminService;
import tgc.edu.mcy.service.LesseeService;

@Controller
@RequestMapping(value="/lessee")
public class LesseeController {
	@Autowired
	private LesseeService lesseeDAO;
	@Autowired
	private UserUtils userUtils;
	@Autowired
	private LesseeAdminService lesseeAdminDAO;
	@Autowired
	private AccreditService accreditDAO;
	@Autowired
	private JournalService journalDAO;
	
	@RequestMapping(value="/all")
	public String all() {
		return "system/lessee_all";
	}
	
	@RequestMapping(value="/add")
	public String add() {
		return "admin/lessee_add";
	}
	
	//显示所有租户
	@RequestMapping(value="/list")
	@ResponseBody
	public Object list(JpgridUtils form, String name, String unit, String linkman) {
		Pageable pageable = form.buildPageable();
		Page<Lessee> page = null;
		Specification<Lessee> spec = new Specification<Lessee>() {

			@Override
			public Predicate toPredicate(Root<Lessee> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				HashSet<Predicate> rules = new HashSet<>();
				if(StringUtils.hasText(name)) {
					rules.add(criteriaBuilder.like(root.get("name"), "%"+name+"%"));
				}else if(StringUtils.hasText(unit)) {
					rules.add(criteriaBuilder.like(root.get("lesseeAdmin").get("unit"), "%"+unit+"%"));
				}else if(StringUtils.hasText(linkman)) {
					rules.add(criteriaBuilder.like(root.get("linkman"), "%"+linkman+"%"));
				}
				return criteriaBuilder.and(rules.toArray(new Predicate[rules.size()]));
			}			
		};
		page = lesseeDAO.findAll(spec, pageable);
		HashMap<String , Object> result = form.getPageResult(page);
		return result;
	}
	
	@RequestMapping(value="/list1")
	@ResponseBody
	public Object list1(JpgridUtils form, String name, String unit, String linkman) {
		Pageable pageable = form.buildPageable();
		Page<Lessee> page = null;
		Specification<Lessee> spec = new Specification<Lessee>() {
			
			@Override
			public Predicate toPredicate(Root<Lessee> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				HashSet<Predicate> rules = new HashSet<>();
				if(StringUtils.hasText(name)) {
					rules.add(criteriaBuilder.like(root.get("name"), "%"+name+"%"));
				}else if(StringUtils.hasText(unit)) {
					rules.add(criteriaBuilder.like(root.get("lesseeAdmin").get("unit"), "%"+unit+"%"));
				}else if(StringUtils.hasText(linkman)) {
					rules.add(criteriaBuilder.like(root.get("linkman"), "%"+linkman+"%"));
				}
				return criteriaBuilder.and(rules.toArray(new Predicate[rules.size()]));
			}			
		};
		page = lesseeDAO.findAll(spec, pageable);
		HashMap<String , Object> result = form.getPageResult(page);
		return result;
	}
	
	//判断租户名称是否存在
	@RequestMapping(value="/username")
	@ResponseBody
	public Boolean username(String username) {
		Lessee lessee = lesseeDAO.findByName(username);
		if(lessee == null) {
			return false;
		}else {
			return true;
		}
	}
	
	//判断租户管理员和密码之后正确
	@RequestMapping(value="/gly")
	@ResponseBody
	public Boolean gly(String username, String password) {
		SysUser user = userUtils.getUser();
		BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
		boolean f = false;
		if(username.equals(user.getUsername()) && encoder.matches(password,user.getPassword())) {
			f = true;
		}
		return f;
	}
	
	//添加租户
	@RequestMapping(value="save")
	public String save(Lessee model) {
		//添加租户
		SysUser user = userUtils.getUser();
		LesseeAdmin lesseeAdmin = lesseeAdminDAO.findById(user.getId());
		model.setNumber(0);
		model.setLesseeAdmin(lesseeAdmin);
		lesseeDAO.save(model);
		
		//添加一年授权
		Accredit accredit = new Accredit();
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		accredit.setLessee(model);
		accredit.setMonth(12);
		accredit.setBeginDate(sdf.format(date));
		accredit.setCost(0);
		Date newDate = stepMonth(date, 12);
		accredit.setOverDate(sdf.format(newDate));
		accreditDAO.save(accredit);
		
		//添加日志
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String data = df.format(new Date());
		Journal journal = new Journal();
		journal.setDate(data);
		journal.setUsername(user.getUsername());
		journal.setOperationName("注册租户");
		System.out.println(journal.getOperationName());
		journalDAO.save(journal);
		/*JournalUtil.log(user.getUsername(), "注册租户");*/
		return "redirect:add";
	}
	
	public static Date stepMonth(Date sourceDate, int month) {
        Calendar c = Calendar.getInstance();
        c.setTime(sourceDate);
        c.add(Calendar.MONTH, month);
        return c.getTime();
    }
}
