package tgc.edu.mcy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tgc.edu.mcy.custom.CommonService;
import tgc.edu.mcy.entity.LesseeAdmin;
import tgc.edu.mcy.repository.LesseeAdminRepository;

@Service
public class LesseeAdminService extends CommonService<LesseeAdmin, Integer> {
	@Autowired
	private LesseeAdminRepository lesseeAdminDAO;
}
