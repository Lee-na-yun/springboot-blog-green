package site.metacoding.red.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class CMRespDto<T> {	// 공통 응답 DTO
	private Integer code; // 1=정상, -1=실패
	private String msg; //실해의 이유, 성공의 이유 메시지 담기
	private T data;// 응답할데이터 : 데이터타입은 항상 다름 ==> 제네릭

}
