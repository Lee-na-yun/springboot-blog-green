package site.metacoding.red.web;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import site.metacoding.red.domain.users.Users;
import site.metacoding.red.service.UsersService;
import site.metacoding.red.util.Script;
import site.metacoding.red.web.dto.request.users.JoinDto;
import site.metacoding.red.web.dto.request.users.LoginDto;
import site.metacoding.red.web.dto.request.users.UpdateDto;

@RequiredArgsConstructor
@Controller
public class UsersController {	// 뷰 리턴

	private final UsersService usersService; // UsersService에 @service해서 가능
	private final HttpSession session;
	
	@GetMapping("/joinForm") // 인증이 필요한 페이지는 도메인을 안적음!
	public String joinForm() {
		return "users/joinForm";
	}
	
	@GetMapping("/loginForm") 
	public String loginForm() {	// 쿠키 배워보기
		return "users/loginForm";
	}
	
	@PostMapping("/join") //회원가입하기
	public String join(JoinDto joinDto) {
		usersService.회원가입(joinDto); // 코드 복잡한건 service가 들고있으면 됨
		return "redirect:/loginForm";
	}
	
	@PostMapping("/login") //로그인하기
	public @ResponseBody String login(LoginDto loginDto) { // @ResponseBody는 데이터 리턴 = 뷰리졸버 발동 안함!!  => "redirect:/";의 메세지가 리턴됨
		Users principal = usersService.로그인(loginDto);// 코드 복잡한건 service가 들고있으면 됨
		// 로그인하고나서 응답을 받아야함 => Users principal
		if(principal == null) { // 인증안됐으면(=로그인안됐으면) null=팅겨내기 ==> return 대신 자바스크립스 사용(Script.java)
			return Script.back("아이디 혹은 비밀번호가 틀렸습니다.");
		}
		session.setAttribute("principal", principal);
		//return "redirect:/";
		return Script.href("/"); // "redirect:/"; <-이거랑 같음!
	}
	
	@GetMapping("/uses/{id}")  // 회원수정 페이지
	public String updateForm(@PathVariable Integer id, Model model) { // 회원수정페이지는 페이지를 달라는것이니 무조건 model에 데이터를 담고 가야함!!
		Users usersPS = usersService.회원정보보기(id);
		model.addAttribute("users", usersPS);
		return "users/updateForm";
	}
	
	@PutMapping("/users/{id}") // 회원수정하기
	public String update(@PathVariable Integer id, UpdateDto updateDto) {
		usersService.회원수정(id, updateDto);
		return "redirect:/users/"+id;
	}
	
	@DeleteMapping("/users/{id}")// 회원탈퇴하기= updateForm에 버튼하나 넣으면 됨
	public @ResponseBody String delete(@PathVariable Integer id) {
		usersService.회원탈퇴(id);
		return Script.href("/loginForm", "회원탈퇴가 완료되었습니다!");
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
