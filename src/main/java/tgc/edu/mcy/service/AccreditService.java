package tgc.edu.mcy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tgc.edu.mcy.custom.CommonService;
import tgc.edu.mcy.entity.Accredit;
import tgc.edu.mcy.repository.AccreditRepository;

@Service
public class AccreditService extends CommonService<Accredit, Integer>{
	@Autowired
	private AccreditRepository accreditDAO;

	public Accredit findByLesseeId(Integer id) {
		return accreditDAO.findByLesseeId(id);
	}
}
