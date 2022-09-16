package site.metacoding.red.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import site.metacoding.red.domain.users.Users;
import site.metacoding.red.service.UsersService;
import site.metacoding.red.util.Script;
import site.metacoding.red.web.dto.request.users.JoinDto;
import site.metacoding.red.web.dto.request.users.LoginDto;
import site.metacoding.red.web.dto.request.users.UpdateDto;
import site.metacoding.red.web.dto.response.CMRespDto;

@RequiredArgsConstructor
@Controller
public class UsersController {	// 뷰 리턴

	private final UsersService usersService; // UsersService에 @service해서 가능
	private final HttpSession session;
	
	// http://localhost:8000/users/usernameSameCheck?username=ssar
	@GetMapping("/users/usernameSameCheck")
	public @ResponseBody CMRespDto<Boolean> usernameSameCheck(String username) {
		boolean isSame = usersService.유저네임중복확인(username);
		return new CMRespDto<>(1, "성공", isSame); // boolean을 리턴해주면 boolean타입이 왔다갔다하는것이라서 body데이터를 json으로 응답해줘야함(json으로 통일!)
	}
	
	@GetMapping("/joinForm") // 인증이 필요한 페이지는 도메인을 안적음!
	public String joinForm() {
		return "users/joinForm";
	}
	
	@GetMapping("/loginForm") 
	public String loginForm(Model model, HttpServletRequest request) {	// 요청헤더에 쿠키라는 키가 있고 모델에 담아서 주기
		Cookie[] cookies = request.getCookies();
		for(Cookie cookie : cookies) {
			if(cookie.getName().equals("username")) {
				model.addAttribute(cookie.getName(), cookie.getValue());
			}
			
			System.out.println("===================================");
			System.out.println(cookie.getName());
			System.out.println(cookie.getValue());
			System.out.println("===================================");
		}
		return "users/loginForm";
	}
	
	@PostMapping("/join") //회원가입하기
	public @ResponseBody CMRespDto<?> join(@RequestBody JoinDto joinDto) {
		usersService.회원가입(joinDto); // 코드 복잡한건 service가 들고있으면 됨
		return new CMRespDto<>(1, "회원가입성공", null);
	}
	
	@PostMapping("/login") //로그인하기
	public @ResponseBody CMRespDto<?> login(@RequestBody LoginDto loginDto, HttpServletResponse response) { // @ResponseBody는 데이터 리턴 = 뷰리졸버 발동 안함!!  => "redirect:/";의 메세지가 리턴됨
		System.out.println("===================================");
		System.out.println(loginDto.isRemember());
		System.out.println("===================================");
		
		if(loginDto.isRemember()) { // 만약에 remember가 true이면
			Cookie cookie = new Cookie("username", loginDto.getUsername());
			cookie.setMaxAge(60*60*24);
			response.addCookie(cookie); // 체크를하면 만들어지고 체크를 풀면 삭제됨
			//response.setHeader("Set-Cookie", "username"+loginDto.getUsername());
		}else {
			Cookie cookie = new Cookie("username",null);
			cookie.setMaxAge(0);
			response.addCookie(cookie);
		}
		
		Users principal = usersService.로그인(loginDto);// 코드 복잡한건 service가 들고있으면 됨
		// 로그인하고나서 응답을 받아야함 => Users principal
		if(principal == null) { // 인증안됐으면(=로그인안됐으면) null=팅겨내기 ==> return 대신 자바스크립스 사용(Script.java)
			return new CMRespDto<>(-1, "로그인 실패", null);
		}
		session.setAttribute("principal", principal); // 정상이면 session에 담고
		return new CMRespDto<>(1, "로그인 성공", null);
	}
	
	@GetMapping("/users/{id}")  // 회원수정 페이지
	public String updateForm(@PathVariable Integer id, Model model) { // 회원수정페이지는 페이지를 달라는것이니 무조건 model에 데이터를 담고 가야함!!
		Users usersPS = usersService.회원정보보기(id);
		model.addAttribute("users", usersPS);
		return "users/updateForm";
	}
	
	@PutMapping("/users/{id}") // 회원수정하기
	public @ResponseBody CMRespDto<?> update(@PathVariable Integer id, @RequestBody UpdateDto updateDto) {
		Users usersPS = usersService.회원수정(id, updateDto);
		session.setAttribute("principal", usersPS); //세션동기화
		//return "redirect:/users/"+id;
		return new CMRespDto<>(1, "회원수정 성공", null);
	}
	
	@DeleteMapping("/users/{id}")// 회원탈퇴하기= updateForm에 버튼하나 넣으면 됨
	public @ResponseBody CMRespDto<?> delete(@PathVariable Integer id) {
		usersService.회원탈퇴(id);
		session.invalidate();
		return new CMRespDto<>(1, "회원탈퇴 성공", null);
	}
	
	@GetMapping("/logout")
	public String logout() {
		session.invalidate();
		return "redirect:/loginForm";
	}
	
	
//	private final UsersDao usersDao;
//	
//	@GetMapping("/users/{id}")
//	public Users getUsers(@PathVariable Integer id) {
//		return usersDao.findById(id);
//	}

}
