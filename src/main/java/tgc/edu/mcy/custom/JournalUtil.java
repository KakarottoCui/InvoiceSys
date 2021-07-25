package tgc.edu.mcy.custom;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import tgc.edu.mcy.entity.Journal;
import tgc.edu.mcy.service.JournalService;

@Controller
public class JournalUtil {
	@Autowired
	private static JournalService journalDAO;
	
	public static void log(String username, String name) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String data = df.format(new Date());
		Journal journal = new Journal();
		journal.setDate(data);
		journal.setUsername(username);
		journal.setOperationName(name);
		System.out.println(journal.getOperationName());
		journalDAO.save(journal);			
	}
}
