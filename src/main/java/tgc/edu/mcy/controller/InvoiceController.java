package tgc.edu.mcy.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import tgc.edu.mcy.custom.AjaxResult;
import tgc.edu.mcy.custom.JpgridUtils;
import tgc.edu.mcy.entity.Invoice;
import tgc.edu.mcy.entity.Journal;
import tgc.edu.mcy.entity.Lessee;
import tgc.edu.mcy.entity.LesseeAdmin;
import tgc.edu.mcy.entity.SysUser;
import tgc.edu.mcy.entity.User;
import tgc.edu.mcy.security.UserUtils;
import tgc.edu.mcy.service.InvoiceService;
import tgc.edu.mcy.service.JournalService;
import tgc.edu.mcy.service.LesseeAdminService;
import tgc.edu.mcy.service.LesseeService;
import tgc.edu.mcy.service.UserService;

@Controller
@RequestMapping(value="/invoice")
public class InvoiceController {
	@Autowired
	private LesseeService lesseeDAO;
	@Autowired
	private InvoiceService invoiceAO;
	@Autowired
	private UserUtils userUtils;
	@Autowired
	private LesseeAdminService lesseeAdminDAO;
	@Autowired
	private UserService userDAO;
	@Autowired
	private JournalService journalDAO;
	
	@RequestMapping(value="all")
	public String all(ModelMap map) {
		SysUser user = userUtils.getUser(); 
		User user2 = userDAO.findById(user.getId());
		List<Lessee> list = lesseeDAO.findByLesseeAdminId(user2.getLessee().getLesseeAdmin().getId());
		map.put("lessee", list);
		return "user/invoice_all";
	}
	
	@RequestMapping(value="/findById")
	public String findById() {
		return "admin/invoiceAll";
	}
	
	@RequestMapping(value="/statistics")
	public String statistics() {
		return "system/statistics";
	}
	
	//显示所有电子发票
	@RequestMapping(value="/list")
	@ResponseBody
	public Object list(JpgridUtils form, String name, String endDate, String number) {
		SysUser user = userUtils.getUser(); 
		User user2 = userDAO.findById(user.getId());
		Pageable pageable = form.buildPageable();
		Page<Invoice> page = null;
		
		Specification<Invoice> spec = new Specification<Invoice>() {

			@Override
			public Predicate toPredicate(Root<Invoice> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				HashSet<Predicate> rules = new HashSet<>();
				if(StringUtils.hasText(name)) {
					rules.add(criteriaBuilder.like(root.get("name"), "%"+name+"%"));
				}else if(StringUtils.hasText(endDate)) {
					rules.add(criteriaBuilder.like(root.get("endDate"), "%"+endDate+"%"));
				}else if(StringUtils.hasText(number)) {
					rules.add(criteriaBuilder.like(root.get("number"), "%"+number+"%"));
				}
				rules.add(criteriaBuilder.equal(root.get("lessee").get("id"), user2.getLessee().getId()));
				return criteriaBuilder.and(rules.toArray(new Predicate[rules.size()]));
			}			
		};
		page = invoiceAO.findAll(spec, pageable);
		HashMap<String , Object> result = form.getPageResult(page);
		return result;
	}
	
	//显示所有电子发票
	@RequestMapping(value="/list1")
	@ResponseBody
	public Object list1(JpgridUtils form, String name, String code, String unit) {
		SysUser user = userUtils.getUser(); 
		LesseeAdmin lesseeAdmin = lesseeAdminDAO.findById(user.getId());
		Pageable pageable = form.buildPageable();
		Page<Invoice> page = null;
		
		Specification<Invoice> spec = new Specification<Invoice>() {
			
			@Override
			public Predicate toPredicate(Root<Invoice> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				HashSet<Predicate> rules = new HashSet<>();
				if(StringUtils.hasText(name)) {
					rules.add(criteriaBuilder.like(root.get("name"), "%"+name+"%"));
				}else if(StringUtils.hasText(code)) {
					rules.add(criteriaBuilder.like(root.get("code"), "%"+code+"%"));
				}else if(StringUtils.hasText(unit)) {
					rules.add(criteriaBuilder.like(root.get("lessee").get("name"), "%"+unit+"%"));
				}
				rules.add(criteriaBuilder.equal(root.get("lessee").get("lesseeAdmin").get("id"), lesseeAdmin.getId()));
				return criteriaBuilder.and(rules.toArray(new Predicate[rules.size()]));
			}			
		};
		page = invoiceAO.findAll(spec, pageable);
		HashMap<String , Object> result = form.getPageResult(page);
		return result;
	}
	
	//验证编号是否已添加
	@RequestMapping(value="/number")
	@ResponseBody
	public Boolean number(String number) {
		Invoice invoice = invoiceAO.findByNumber(number);
		if(invoice == null) {
			return false;
		}else {
			return true;
		}
	}
	
	//添加电子发票
	@RequestMapping(value="/save")
	@ResponseBody
	public Object save(Invoice model) {
		SysUser user1 = userUtils.getUser(); 
		User user2 = userDAO.findById(user1.getId());
		Date d = new Date(); 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		String dateNowStr = sdf.format(d);
		if(model.getCode() == null || model.getCode() == "") {
			Calendar now = Calendar.getInstance();
			String year = String.valueOf(now.get(Calendar.YEAR));  
			String month = String.valueOf((now.get(Calendar.MONTH) + 1)); 
			String day = String.valueOf(now.get(Calendar.DAY_OF_MONTH));
			String intFlag = String.valueOf((int)(Math.random() * 10000 + 1));
			model.setDate(dateNowStr);
	        model.setSerial(year+month+day+intFlag);
	        model.setCode(intFlag+month+day+intFlag);
	        model.setState("未报销");
	        model.setLessee(user2.getLessee());
	        invoiceAO.save(model);
	        Lessee lessee = lesseeDAO.findById(user2.getLessee().getId());
	        Integer a = lessee.getNumber()+1;
	        lessee.setNumber(a);
	        lesseeDAO.save(lessee);
	        
	        SysUser user = userUtils.getUser();
	        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
			String data = df.format(new Date());
			Journal journal = new Journal();
			journal.setDate(data);
			journal.setUsername(user.getUsername());
			journal.setOperationName("添加发票");
			System.out.println(journal.getOperationName());
			journalDAO.save(journal);
	        /*JournalUtil.log(user.getUsername(), "添加发票");*/
			return AjaxResult.build(true, "OK");
		}else {
			model.setEndDate(dateNowStr);
			model.setState("已报销");
			invoiceAO.save(model);
			return AjaxResult.build(true, "OK");
		}
	}
	
	//删除
	@RequestMapping(value="/delete")
	@ResponseBody
	public void delete(String ids) {
		String[] id = ids.split(",");
		for(int i = 0; i < id.length; i++) {
			invoiceAO.deleteById(Integer.parseInt(id[i]));
			Invoice invoice = invoiceAO.findById(Integer.parseInt(id[i]));
			Lessee lessee = lesseeDAO.findById(invoice.getLessee().getId());
			Integer a = lessee.getNumber()-1;
			lessee.setNumber(a);
			lesseeDAO.save(lessee);
		}
	}
}
