package hello.board.web.post;

import lombok.Getter;

/**
 * 1페이지 -> 00행 ~ 09행 (10개) : LIMIT 0,10
 * 2페이지 -> 10행 ~ 19행 (10개) : LIMIT 10,10
 * 3페이지 -> 20행 ~ 29행 (10개) : LIMIT 20, 10
 *
 * 이 클래스는 DB행 계산용
 */

@Getter
public class MyPageble {
    private int page = 1; //현재 클릭한 페이지 index [1](처음엔 1)
    private int size = 10; //한 페이지당 가져올 게시글 개수(처음엔 10개)
    private long totalElementNum; //총 게시글 수

    //특정 페이지의 게시글 시작 행 번호(DB)
    public int getPageStat(){
        return (this.page -1) * size; //(2-1)*10 = 10행 ~ 19행
    }

    public MyPageble(int page, long totalElementNum) {
        this.page = page;
        this.totalElementNum = totalElementNum;
    }

}
