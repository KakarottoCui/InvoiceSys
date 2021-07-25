package tgc.edu.mcy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tgc.edu.mcy.custom.CommonService;
import tgc.edu.mcy.entity.Lessee;
import tgc.edu.mcy.repository.LesseeRepository;

@Service
public class LesseeService extends CommonService<Lessee, Integer>{
	@Autowired
	private LesseeRepository lesseeDAO;

	public Lessee findByName(String username) {
		return lesseeDAO.findByName(username);
	}

	public List<Lessee> findByLesseeAdminId(Integer id) {
		return lesseeDAO.findByLesseeAdminId(id);
	}
}
