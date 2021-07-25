package tgc.edu.mcy.repository;

import org.springframework.stereotype.Repository;

import tgc.edu.mcy.custom.CommonRepository;
import tgc.edu.mcy.entity.Accredit;

@Repository
public interface AccreditRepository extends CommonRepository<Accredit, Integer> {

	public Accredit findByLesseeId(Integer id);

}
