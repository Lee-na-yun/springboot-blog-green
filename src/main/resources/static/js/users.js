let isUsernameSameCheck = false;

$("#btnJoin").click(() => {
	join();
});

$("#btnUsernameSameCheck").click(() => {	
	checkUsername();
});

$("#btnLogin").click(() => {
	login();
	//login_test();
});

$("#btnDelete").click(() => {
	resign();
});

$("#btnUpdate").click(() => {
	update();
});



//------------------------------------------회원가입-------------------------------------------------------------
function join(){
	// alert("실행됨"); // 실행되는지 test해보기
	if (isUsernameSameCheck == false) {// true일때만 진행하면 되니까 false만 체크하면됨
		alert("유저네임 중복 체크를 진행해주세요.");
		return; // 메서드 종료시키려면 return을 하면 됨!
	}

	// 0. 통신 오브젝트 생성 (post요청이기때문에 body가 있음)
	let data = {
		username: $("#username").val(), // 자바스크립트 object를 jquery로 가져옴
		password: $("#password").val(), // 자바스크립트 object를 jquery로 가져옴
		email: $("#email").val() // 자바스크립트 object를 jquery로 가져옴
	};
	$.ajax("/join", {
		type: "POST",
		dataType: "json",
		data: JSON.stringify(data), // body // 데이터 전송할 때 json으로 변경해서 전송
		headers: {
			"Content-Type": "application/json" // json데이터를 날릴거야!
		}
	}).done((res) => {
		if (res.code == 1) { // 1이면 회원가입 성공이라고 컨트롤러에 적어놨음
			// console.log(res); // 테스트
			location.href = "/loginForm";
		}
	});
}


//------------------------------------------유저네임 중복체크-------------------------------------------------------------
function checkUsername(){
	// 0. 통신 오브젝트 생성 (항상 통신하기전에 해야함) - get 요청(username이 있는지 select할거라서)은 body가 없음.

	// 1. 사용자가 적은 username 값을 가져오기		
	let username = $("#username").val();
	// body.username = username;

	// 2. ajax 통신(DB 확인)
	$.ajax(`/users/usernameSameCheck?username=${username}`, { // (``)을 넣으면 +로 안넣어도 됨
		type: "GET", // default가 get이므로 생략가능
		dataType: "json", // json이 default값이라서 다른걸로 응답하려면 타입을 적어줘야함!
		// dataType:"text" // 데이터를 text로 인식해서 응답하겠다.
		async: true // false(동기적) 쓰지말기!

	}).done((res) => {	// pending이 끝나며 함수가 실행됨 // 함수이름은 중요하지 않으므로 익명함수를 쓰지만 람다식을 더 씀!
		// 요청하면 응답 결과가 res(변수)에 담김
		if (res.code == 1) {
			//alert("통신성공");
			if (res.data == false) {
				alert("아이디가 중복되지 않았습니다.");
				isUsernameSameCheck = true; // 변수로 제어 // true는 통과된것
			} else {
				alert("아이디가 중복되었어요. 다른 아이디를 사용해 주세요.")
				isUsernameSameCheck = false;
				$("#username").val(""); // set해서 val(""); 비워줌
			}
		}
	}); // $.ajax(); :ajax 통신 시작 // ajax통신이 호출되고 응답의 결과가 done(행위)에 들어옴 // $.ajax("주소",{오브젝트})
}


//------------------------------------------로그인-------------------------------------------------------------
//function login_test(){
//	let remember = $("#remember").prop('checked');
//	console.log(remember);
//}

function login(){
	alert("login 함수 호출됨");
	// 0. 통신 오브젝트 생성 (post요청이기때문에 body가 있음)
	let data = {
		username: $("#username").val(),
		password: $("#password").val(),
		remember: $("#remember").prop('checked')
	};

	$.ajax("/login", { // login을 select 하는건데 login만 유일하게 post!
		type: "POST",
		dataType: "json", // 데이터를 json으로 줬으면 좋겠어!
		data: JSON.stringify(data),
		headers: {
			"Content-Type": "application/json; charset=utf-8" // utf-8로 인코딩되어 있음
		}
	}).done((res) => {
		if (res.code == 1) {
			location.href = "/";
		} else {
			alert("로그인 실패, 아이디 혹은 패스워드를 확인해주세요.");
		}
	});
}


//------------------------------------------회원탈퇴-------------------------------------------------------------
function resign(){
	let id = $("#id").val();  // 주소에 해당하는 데이터 // id는 주소에 걸어야함

	$.ajax("/users/" + id, {
		type: "DELETE",
		dataType: "json"
		//data: JSON.stringify(data), 
		//headers:{
		//	"Content-Type" : "application/json; charset=utf-8"
		//} body데이터가 없으므로 삭제
	}).done((res) => {
		if (res.code == 1) {
			alert("회원 탈퇴 완료");
			location.href = "/";
		} else {
			alert("회원탈퇴를 실패하였습니다.");
		}
	});
}


//------------------------------------------회원정보수정-------------------------------------------------------------
function update(){
	let data = {
		password: $("#password").val(),
		email: $("#email").val()
	};
	let id = $("#id").val();  // 주소에 해당하는 데이터

	$.ajax("/users/" + id, { // ${users.id}로 적으면 안됨 -> 스크립트 파일을 다른데로 옮기는 순간 동작을 안함!  -> 바인딩시켜서 끌어 쓰면 됨!!
		type: "PUT",
		dataType: "json",
		data: JSON.stringify(data),
		headers: {
			"Content-Type": "application/json; charset=utf-8"
		}
	}).done((res) => {
		if (res.code == 1) {
			alert("회원 수정 성공");
			location.reload(); // 본인페이지로 돌아감(페이지 새로고침 F5)
		} else {
			alert("회원수정을 실패하였습니다.");
		}
	});
}




