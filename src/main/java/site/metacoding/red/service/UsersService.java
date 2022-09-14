package site.metacoding.red.service;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.metacoding.red.domain.boards.BoardsDao;
import site.metacoding.red.domain.users.Users;
import site.metacoding.red.domain.users.UsersDao;
import site.metacoding.red.web.dto.request.users.JoinDto;
import site.metacoding.red.web.dto.request.users.LoginDto;
import site.metacoding.red.web.dto.request.users.UpdateDto;

@RequiredArgsConstructor
@Service
public class UsersService {
	
	private final UsersDao usersDao; // 디펜던시인젝션
	private final BoardsDao boardsDao;  // 디펜던시인젝션

	public void 회원가입(JoinDto joinDto) {	// username, password, email을 받는 dto 받아서 // 엔티티로 변경후 // 넘기기(엔티티로 디비 수행)
		Users users = joinDto.toEntity();
		usersDao.insert(users);
	}		// users테이블
	
	public Users 로그인 (LoginDto loginDto) {	// username, password 받는 dto // Users를 세션에 받아야하므로 users 리턴
		Users usersPS = usersDao.findByUsername(loginDto.getUsername());
		// if로 usersPS의 password와 디티오 password 비교 (객체의 password를 new)
		if(usersPS.getPassword().equals(loginDto.getPassword())) {
			return usersPS;
		}else {
			return null;
		}
	}		// users테이블
	
	public void 회원수정(Integer id, UpdateDto updateDto) {	// id, password, email 받는 dto (username은 수정 안할것임)
		// 1. 영속화
		Users usersPS = usersDao.findById(id);
		// 2. 영속화된 객체 변경
		usersPS.회원정보수정(updateDto);
		// 3. DB 수행(update)
		usersDao.update(usersPS);
	}		// users테이블
	
	@Transactional(rollbackFor = RuntimeException.class) // 전체를 하나의 트랜젝션으로 묶는 것
	public void 회원탈퇴(Integer id) {	// id(user Id)만 받으면 됨
		usersDao.deleteById(id); // --> 디비에 들어가서 write가 수행되는 동시에 트랜젝션에 걸림 -> 딜리트가 끝나면 커밋됨
		boardsDao.updateByUsersId(id);  //해당회원이 적은글을 모두 찾아서 usersId를 null로 업데이트(); // null = 유령회원을 null로 바꾸는것
		
		// 마이바티스 for문 돌리며 update하는것 찾기  // update 10번
		
		
	}		// users & boards 테이블  // user를 삭제하고 board테이블에 userid를 null로 바꾸기
	
	//public void 로그아웃() {}		// 세션만 날리는것 -> db연결될 필요 없음 ==> 컨트롤러에 넣으면 됨!
	// 톰캣이 만들어준 request, response 객체는 서비스에 안남김 (db연결 안되면 서비스 x)
	
	public boolean 유저네임중복확인(String username) {
		Users usersPS = usersDao.findByUsername(username);
		// 있으면 true, 없으면 false 요청
		if(usersPS == null) {
			return false;
		}else {
			return true;
		}
	}	  // users테이블
	
	public Users 회원정보보기(Integer id) {
		Users usersPS = usersDao.findById(id);
		return usersPS;
	}
}
