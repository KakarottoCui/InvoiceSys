package tgc.edu.mcy.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import tgc.edu.mcy.custom.VerifyCodeUtils;
import tgc.edu.mcy.entity.Accredit;
import tgc.edu.mcy.entity.Journal;
import tgc.edu.mcy.entity.LesseeAdmin;
import tgc.edu.mcy.entity.SysRole;
import tgc.edu.mcy.entity.SysUser;
import tgc.edu.mcy.entity.User;
import tgc.edu.mcy.security.UserUtils;
import tgc.edu.mcy.service.AccreditService;
import tgc.edu.mcy.service.JournalService;
import tgc.edu.mcy.service.LesseeAdminService;
import tgc.edu.mcy.service.RoleService;
import tgc.edu.mcy.service.SysUserService;
import tgc.edu.mcy.service.UserService;

@Controller
public class LoginController {
	@Autowired
	private UserUtils userUtils;
	@Autowired
	private SysUserService userDAO;
	@Autowired
	private RoleService roleDAO;
	@Autowired
	private LesseeAdminService lesseeAdminDAO;
	@Autowired
	private JournalService journalDAO;
	@Autowired
	private UserService userDAO1;
	@Autowired
	private AccreditService accreditDAO;
	//发送者的邮箱账号
	@Value("${spring.mail.username}")
	private String mailusername;
	@Autowired
	JavaMailSender jms;
	
	@RequestMapping(value="/login",method=RequestMethod.GET)
	public String login() {
		List<SysUser> list = userDAO.findAll();
		if(list.size() == 0) {
			test();
		}
		return "index";
	}
	
	@RequestMapping(value="/zhmm")
	public String zhmm() {
		return "zhmm";
	}
	
	//修改密码
	@RequestMapping(value="/xg")
	public String xg(String pad) {
		SysUser user = userUtils.getUser();
		BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
		user.setPassword(encoder.encode(pad));
		userDAO.save(user);
		return "redirect:logout";
	}
	
	@RequestMapping(value="/username")
	@ResponseBody
	public Boolean username(String username) {
		SysUser user = userDAO.findByUsername(username);
		if(user == null) {
			return false;
		}else {
			return true;
		}
	}
	
	@RequestMapping(value="/yx")
	@ResponseBody
	public String yx(String username, HttpSession session) {
		//建立邮件消息
		SimpleMailMessage mainMessage = new SimpleMailMessage();
		//发送者
		mainMessage.setFrom(mailusername);
		//接收者
		mainMessage.setTo(username);
		//发送的标题
		mainMessage.setSubject("电子发票管理系统");
		String code = VerifyCodeUtils.generateVerifyCode(6);
		//发送的内容
		mainMessage.setText("请记住验证码："+code);
		jms.send(mainMessage);
		System.out.println(code);
		session.setAttribute("yzm", code);
		return "ok";
	}
	
	@RequestMapping(value="/yz")
	@ResponseBody
	public Boolean yz(String code, HttpSession session) {
		String code1 = (String) session.getAttribute("yzm");
		if(code.equals(code1)) {
			return true;
		}else {
			return false;
		}
	}
	
	@RequestMapping(value="/zhmm1")
	@ResponseBody
	public String zhmm1(String username, String password, HttpSession session) {
		SysUser user = userDAO.findByUsername(username);
		BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
		user.setPassword(encoder.encode(password));
		userDAO.save(user);
		session.removeAttribute("yzm");
		return "密码找回成功！";
	}
	
	//判断原密码是否正确
	@RequestMapping(value="/pwd")
	@ResponseBody
	public Boolean pwd(String password) {
		SysUser user = userUtils.getUser();
		BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
		boolean f = encoder.matches(password,user.getPassword());
		return f;
	}
	
	//跳转到登录页面
	@RequestMapping(value="login1")
	public String login1() {
		return "login";
	}
		
	//登录成功跳转日志
	@RequestMapping(value="/main")
	public String main() {
		SysUser user = userUtils.getUser();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String data = df.format(new Date());
		Journal journal = new Journal();
		journal.setDate(data);
		journal.setUsername(user.getUsername());
		journal.setOperationName("登入");
		System.out.println(journal.getOperationName());
		journalDAO.save(journal);
		/*JournalUtil.log(user.getUsername(), "登入");*/
		return "redirect:main1";
	}
	
	//登录成功跳转的欢迎页面
	@RequestMapping(value="/main1")
	public String main1(HttpSession session) {
		SysUser user = userUtils.getUser();
		if(user.getSf() == null) {
			return "main";
		}else {
			User user1 = userDAO1.findById(user.getId());
			Accredit accredit = accreditDAO.findByLesseeId(user1.getLessee().getId());
			String data = accredit.getOverDate();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String data1 = df.format(new Date());
			try {
				Date bt=df.parse(data); 
				Date et=df.parse(data1);
				if (bt.before(et)){ 
					session.setAttribute("str", "租户授权已到期");
					return "redirect:logout";
				}else {
					session.removeAttribute("str");
					return "index";
				}
			} catch (ParseException e) {
				e.printStackTrace();
			} 
			return null;
		}
	}
	
	@RequestMapping(value="/str")
	@ResponseBody
	public String str(HttpSession session) {
		session.removeAttribute("str");
		return "ok";
	}
	
	@RequestMapping(value="/logout2")
	public String logout2(HttpSession session) {
		String str = (String) session.getAttribute("str");
		if(str == null) {
			return "index";
		}else {
			return "redirect:login1";
		}
	}
	
	//退出日志
	@RequestMapping(value="/logout1")
	public String logout1() {
		SysUser user = userUtils.getUser();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String data = df.format(new Date());
		Journal journal = new Journal();
		journal.setDate(data);
		journal.setUsername(user.getUsername());
		journal.setOperationName("登出");
		System.out.println(journal.getOperationName());
		journalDAO.save(journal);
		/*JournalUtil.log(user.getUsername(), "登出");*/
		return "redirect:logout";
	}
	
	//数据库中没有数据，先添加数据
	private void test() {
		BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();   //密码加密类
		SysRole role = new SysRole();		
		role.setName("系统管理员");
		role.setCode("ROLE_SYSTEM");
		roleDAO.save(role);
		SysRole role2 = new SysRole();
		role2.setName("租户管理员");
		role2.setCode("ROLE_ADMIN");
		roleDAO.save(role2);
		SysRole role3 = new SysRole();
		role3.setName("用户");
		role3.setCode("ROLE_USER");
		roleDAO.save(role3);
		
		SysUser user = new SysUser();
		user.setUsername("system");
		user.setPassword(encoder.encode("system"));   //encode密码加密方法
		user.getRoles().add(role);
		userDAO.save(user);
		
		LesseeAdmin lesseeAdmin = new LesseeAdmin();
		lesseeAdmin.setUsername("3027333811@qq.com");
		lesseeAdmin.setPhone("123456");
		lesseeAdmin.setUnit("宜昌");
		lesseeAdmin.setName("管理员");
		lesseeAdmin.setPassword(encoder.encode("admin"));
		lesseeAdmin.getRoles().add(role2);
		lesseeAdminDAO.save(lesseeAdmin);
		
	}
	
	
}
