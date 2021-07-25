package tgc.edu.mcy.repository;

import org.springframework.stereotype.Repository;

import tgc.edu.mcy.custom.CommonRepository;
import tgc.edu.mcy.entity.Journal;

@Repository
public interface JournalRepository extends CommonRepository<Journal, Integer> {
}
