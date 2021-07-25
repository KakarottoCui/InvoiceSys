package tgc.edu.mcy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tgc.edu.mcy.custom.CommonService;
import tgc.edu.mcy.entity.SysRole;
import tgc.edu.mcy.repository.RoleRepository;

@Service
public class RoleService extends CommonService<SysRole, Integer> {
	@Autowired
	private RoleRepository roleDAO;
}
