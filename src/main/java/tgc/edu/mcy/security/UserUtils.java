package tgc.edu.mcy.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import tgc.edu.mcy.entity.SysUser;
import tgc.edu.mcy.repository.SysUserRepository;

//创建会话
@Component
public class UserUtils {
	@Autowired
	private SysUserRepository userDAO;
	
	public SysUser getUser() {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = userDetails.getUsername();
		SysUser user = userDAO.findByUsername(username);
		return user;
	}
}
