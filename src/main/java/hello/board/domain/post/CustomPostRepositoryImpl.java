package hello.board.domain.post;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static hello.board.domain.member.QMember.member;
import static hello.board.domain.post.QPost.*;
import static org.thymeleaf.util.StringUtils.*;

@Repository
public class CustomPostRepositoryImpl implements CustomPostRepository{

    private final JPAQueryFactory queryFactory;

    public CustomPostRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 페이징
     * - 코드를 리펙토링해서, 내용 쿼리과 전체 카운트 쿼리를 읽기 좋게 분리하면 좋다. -> 메소드로 뽑기
     */
    @Override
    public Page<Post> searchPage(PostSearchCondition condition, Pageable pageable) {
        //데이터 조회 쿼리
        List<Post> content = queryFactory
                .selectFrom(post)
                .leftJoin(post.member, member)
                .where(
                        writerNameEq(condition.getWriter()),
                        titleContentHasStr(condition.getTitle(), condition.getContent())
                )
                .orderBy(post.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Post> countQuery = queryFactory
                .selectFrom(post)
                .where(
                        writerNameEq(condition.getWriter()),
                        titleContentHasStr(condition.getTitle(), condition.getContent())
                );

        /**
         *  Count 쿼리 최적화(스프링 데이터 라이브러리가 제공)
         * - count 쿼리가 생략 가능한 경우 생략해서 처리
         *   - 페이지 시작이면서 컨텐츠 사이즈가 페이지 사이즈보다 작을 때
         *   - 마지막 페이지 일 때 (offset + 컨텐츠 사이즈를 더해서 전체 사이즈 구함)
         *   - 위 두경우 페이지 나눌 필요가 없으니까 total값이 필요X!
         */
        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetch().size());
    }

    private BooleanExpression titleContentHasStr(String title, String content) {
        if(isEmpty(title)) return contentHasStr(content);
        else if(isEmpty(content)) return titleHasStr(title);
        else return post.title.contains(title).or(post.content.contains(content));
    }

    private BooleanExpression writerNameEq(String writer) {
        return isEmpty(writer) ? null : post.member.name.eq(writer);
    }

    private BooleanExpression titleHasStr(String title) {
        return isEmpty(title) ? null : post.title.contains(title);
    }

    private BooleanExpression contentHasStr(String content) {
        return isEmpty(content) ? null : post.content.contains(content);
    }
}
