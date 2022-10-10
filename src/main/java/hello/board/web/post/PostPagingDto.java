package hello.board.web.post;

import hello.board.domain.post.Post;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * 게시글을 조회하거나 수정, 삭제를 하고 난 뒤, 다시 원래의 목록 페이지로 이동해야 한다.
 * '돌아가기'버튼을 이용해 목록으로 돌아갈 때도 마찬가지다.
 */
@Data
public class PostPagingDto {
    private long totalElements; //총 게시글 수
    private int totalPages;//*총 [페이지] 수

    private final int blockSize = 10;
    private boolean firstBlock=true;//* [1][2]~[10]
    private boolean endBlock=true; //*

    private int number;//*현재 클릭한 page index [1]
    private int startPage;
    private int endPage;

    private List<PostPreviewDto> postPreviewList;

    public PostPagingDto(Page<Post> searchResults) {
        postPreviewList = searchResults.getContent().stream()
                .map(o -> new PostPreviewDto(o))
                .collect(toList());

        this.number = searchResults.getNumber() + 1; //주의 : querydsl 페이징 인덱스는 0부터 시작
        this.totalElements = searchResults.getTotalElements();
        this.totalPages = searchResults.getTotalPages();

        /** 처음, 끝 버튼 처리*/
        int totalBlocks = (int) Math.ceil(this.totalPages / (double) blockSize); //총 블락 수 = 총 페이지 수 / 10 (올림)
        //ex) 77개의 페이지 -> 77/10= 8개의 블록 ([1]~[10]) -> 이게 총 8개

        //현재 블록 = 올림(현재 페이지 인덱스 / 보여줄 인덱스 개수(10)). 올림하는 이유 : 1/10=0.1
        //ex) [이전] [11] [12] [13] [14] [15] [16] [17] [18] [19] [20] [다음] : {올림(11~20/10)= 2} = 2
        int block = (int) Math.ceil((number) / (double) blockSize); //
        if(block != 1){
            firstBlock = false;
        } else if(block != totalBlocks){
            endBlock = false;
        }

        /**페이지 계산*/
        //1,11,21,31,...
        startPage = (block - 1) * blockSize + 1;

        //9,19,29,39,...예외처리
        endPage = startPage + 9 < totalPages? (startPage + blockSize - 1) : totalPages;
    }
}
