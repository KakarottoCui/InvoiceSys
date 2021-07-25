package tgc.edu.mcy.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import tgc.edu.mcy.custom.CommonRepository;
import tgc.edu.mcy.entity.Lessee;

@Repository
public interface LesseeRepository extends CommonRepository<Lessee, Integer> {

	public Lessee findByName(String username);

	public List<Lessee> findByLesseeAdminId(Integer id);

}
