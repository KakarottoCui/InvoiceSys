package tgc.edu.mcy.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import tgc.edu.mcy.custom.CommonService;
import tgc.edu.mcy.entity.SysRole;
import tgc.edu.mcy.entity.SysUser;
import tgc.edu.mcy.repository.SysUserRepository;
import tgc.edu.mcy.security.User2;

@Service
public class SysUserService extends CommonService<SysUser, Integer> implements UserDetailsService{
	@Autowired
	private SysUserRepository userDAO;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		SysUser user= userDAO.findByUsername(username);
		if(user==null) {
			throw new UsernameNotFoundException(username);
		}
		List<GrantedAuthority> authorities=new ArrayList<>();
		List<SysRole> roles = user.getRoles();
		for (SysRole role : roles) {
			authorities.add(new SimpleGrantedAuthority(role.getCode()));
		}
		return new User2(user.getUsername(), user.getPassword(),user.getName(), authorities);
	}
	
	public SysUser findByUsername(String username) {
		return userDAO.findByUsername(username);
	}

}