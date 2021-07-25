package tgc.edu.mcy.repository;

import org.springframework.stereotype.Repository;

import tgc.edu.mcy.custom.CommonRepository;
import tgc.edu.mcy.entity.SysUser;

@Repository
public interface SysUserRepository extends CommonRepository<SysUser, Integer> {

	public SysUser findByUsername(String username);
	
}
