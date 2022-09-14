package site.metacoding.red.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import site.metacoding.red.domain.boards.Boards;
import site.metacoding.red.domain.boards.BoardsDao;
import site.metacoding.red.domain.users.Users;
import site.metacoding.red.domain.users.UsersDao;
import site.metacoding.red.web.dto.request.boards.UpdateDto;
import site.metacoding.red.web.dto.request.boards.WriteDto;
import site.metacoding.red.web.dto.response.boards.MainDto;
import site.metacoding.red.web.dto.response.boards.PagingDto;

@RequiredArgsConstructor
@Service
public class BoardsService {

	private final BoardsDao boardsDao;
	private final UsersDao usersDao;

	public PagingDto 게시글목록보기(Integer page, String keyword) {
		// 서비스 데이터는 model에 못담음 => 리턴을 못함 => 하나의 객체(PagingDto)에 2개를 담아서 응답하기
		if (page == null) {
			page = 0;
		}
		int startNum = page * 3;
		System.out.println("==========");
		System.out.println("keyword : "+keyword);
		System.out.println("==========");
		List<MainDto> boardsList = boardsDao.findAll(startNum, keyword);
		PagingDto pagingDto = boardsDao.paging(page, keyword);
		if (boardsList.size() == 0)
		pagingDto.setNotResult(true);
		pagingDto.makeBlockInfo(keyword);
		pagingDto.setMainDtos(boardsList);

		return pagingDto;

	}

	public Boards 게시글상세보기(Integer id) {
		return boardsDao.findById(id);
	}

	public void 게시글수정(Integer id, UpdateDto updateDto) { // (Integer id, dto)
		Boards boardsPS = boardsDao.findById(id);
		boardsPS.update(updateDto);
		boardsDao.update(boardsPS);
	}

	public void 게시글삭제하기(Integer id) {
		Boards boardsPS = boardsDao.findById(id);

		if (boardsPS == null) {
			// 이 부분은 나중에 처리!! (exception 처리하는 법 따로 배울 예정)
		}

		boardsDao.deleteById(id);
	}

	public void 게시글쓰기(WriteDto writeDto, Users principal) {
		boardsDao.insert(writeDto.toEntity(principal.getId()));
	}
}
