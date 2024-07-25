package com.twentyone.steachserver.domain.curriculum.repository;

import static com.twentyone.steachserver.domain.curriculum.model.QCurriculum.curriculum;
import static com.twentyone.steachserver.domain.curriculum.model.QCurriculumDetail.curriculumDetail;
import static com.twentyone.steachserver.domain.member.model.QTeacher.teacher;
import static io.jsonwebtoken.lang.Strings.hasText;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.twentyone.steachserver.domain.curriculum.dto.CurriculaOrderType;
import com.twentyone.steachserver.domain.curriculum.dto.CurriculaSearchCondition;
import com.twentyone.steachserver.domain.curriculum.dto.SimpleCurriculumDto;
import com.twentyone.steachserver.domain.curriculum.enums.CurriculumCategory;
import com.twentyone.steachserver.domain.curriculum.model.Curriculum;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class CurriculumSearchRepository {
    private final JPAQueryFactory queryFactory;

    public CurriculumSearchRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public Page<Curriculum> search(CurriculaSearchCondition condition, Pageable pageable) {
        //select s from curriculum s join curriculum_details d where d.title like :search
        JPAQuery<Curriculum> query = queryFactory
                .select(curriculum)
                .from(curriculum)
                .join(curriculum.curriculumDetail, curriculumDetail).fetchJoin()
                .where(
                        curriculumCategoryEq(condition.getCurriculumCategory()),
                        onlyAvailableEq(condition.getOnlyAvailable()),
                        curriculumSearchKeywordEq(condition.getSearch())
                );

        OrderSpecifier<?> orderSpecifier = getOrder(condition.getOrder());
        if (orderSpecifier != null) {
            query.orderBy(orderSpecifier);
        }

        long total = query.fetchCount();

        query.offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<Curriculum> results = query.fetch();

        return new PageImpl(results, pageable, total);
    }

    private OrderSpecifier<?> getOrder(CurriculaOrderType order) {
        OrderSpecifier<?> type = curriculum.createdAt.desc();

        if (order == null) {
            return type;
        }

        switch (order) {
            case LATEST -> type = curriculum.createdAt.desc();
            case POPULAR -> type = curriculumDetail.currentAttendees.desc();
            case POPULAR_PER_RATIO -> type = curriculumDetail.currentAttendees.divide(curriculumDetail.maxAttendees).desc();
        }

        return type;
    }

    public List<SimpleCurriculumDto> searchForSimpleInformationInOrder(CurriculaOrderType order) {
        JPAQuery<SimpleCurriculumDto> query = queryFactory
                .select(Projections.constructor(
                        SimpleCurriculumDto.class,
                        curriculum.curriculumDetail.bannerImgUrl,
                        curriculum.title,
                        curriculum.curriculumDetail.intro,
                        curriculum.curriculumDetail.maxAttendees,
                        curriculum.curriculumDetail.currentAttendees,
                        curriculum.createdAt,
                        curriculum.teacher.name
                ))
                .from(curriculum)
                .join(curriculum.curriculumDetail, curriculumDetail)
                .join(curriculum.teacher, teacher)
                .where(
                        onlyAvailableEq(true)
                )
                .orderBy(getOrder(order))
                .limit(7);

        return query.fetch();
    }

    private BooleanExpression curriculumSearchKeywordEq(String search) {
        if (hasText(search)) {
            BooleanExpression titleContainsSearch = curriculum.title.contains(search);
            BooleanExpression subtitleContainsSearch = curriculumDetail.subTitle.contains(search);
            return titleContainsSearch.or(subtitleContainsSearch);
        }
        return null;
    }

    private BooleanExpression onlyAvailableEq(Boolean onlyAvailable) {
        return (onlyAvailable == null || !onlyAvailable) ? null : curriculumDetail.maxAttendees.ne(curriculumDetail.currentAttendees);
    }

    private BooleanExpression curriculumCategoryEq(CurriculumCategory curriculumCategory) {
        return curriculumCategory == null ? null : curriculum.category.eq(curriculumCategory);
    }
}