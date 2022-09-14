package site.metacoding.red.domain.users;

import java.util.List;

import site.metacoding.red.web.dto.request.users.JoinDto;
import site.metacoding.red.web.dto.request.users.LoginDto;


public interface UsersDao {
	public void insert(Users users);	
	public List<Users> findAll();	
	public Users findById(Integer id); 
	public void update(Users boards);
	public void deleteById(Integer id);
	public Users findByUsername(String username);
}
