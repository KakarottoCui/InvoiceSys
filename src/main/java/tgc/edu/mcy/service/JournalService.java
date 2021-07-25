package tgc.edu.mcy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tgc.edu.mcy.custom.CommonService;
import tgc.edu.mcy.entity.Journal;
import tgc.edu.mcy.repository.JournalRepository;

@Service
public class JournalService extends CommonService<Journal, Integer> {
	@Autowired
	private JournalRepository journalDAO;
}
