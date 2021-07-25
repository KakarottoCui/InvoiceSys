package tgc.edu.mcy.repository;

import org.springframework.stereotype.Repository;

import tgc.edu.mcy.custom.CommonRepository;
import tgc.edu.mcy.entity.SysRole;

@Repository
public interface RoleRepository extends CommonRepository<SysRole, Integer> {

}
