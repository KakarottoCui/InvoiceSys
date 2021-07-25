package tgc.edu.mcy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tgc.edu.mcy.custom.CommonService;
import tgc.edu.mcy.entity.User;
import tgc.edu.mcy.repository.UserRepository;

@Service
public class UserService extends CommonService<User, Integer> {
	@Autowired
	private UserRepository userDAO;
}
