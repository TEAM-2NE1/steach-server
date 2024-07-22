package com.twentyone.steachserver.domain.curriculum.repository;

import static com.twentyone.steachserver.domain.curriculum.model.QCurriculum.*;
import static com.twentyone.steachserver.domain.curriculum.model.QCurriculumDetail.*;
import static io.jsonwebtoken.lang.Strings.hasText;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.twentyone.steachserver.domain.curriculum.dto.CurriculaOrderType;
import com.twentyone.steachserver.domain.curriculum.dto.CurriculaSearchCondition;
import com.twentyone.steachserver.domain.curriculum.enums.CurriculumCategory;
import com.twentyone.steachserver.domain.curriculum.model.Curriculum;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class CurriculumSearchRepository {
    private final JPAQueryFactory queryFactory;

    public CurriculumSearchRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<Curriculum> search(CurriculaSearchCondition condition) {
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

        return query.fetch();
    }

    private OrderSpecifier<?> getOrder(CurriculaOrderType order) {
        OrderSpecifier<?> type = curriculum.createdAt.desc();

        if (order == null) {
            return type;
        }

        switch (order) {
            case LATEST -> type = curriculum.createdAt.desc();
            case POPULAR -> type = curriculumDetail.currentAttendees.desc();
        }

        return type;
    }

    private BooleanExpression curriculumSearchKeywordEq(String search) {
        return hasText(search) ? curriculum.title.like(search) : null;
    }

    private BooleanExpression onlyAvailableEq(Boolean onlyAvailable) {
        return onlyAvailable == null ? null : curriculumDetail.maxAttendees.ne(curriculumDetail.currentAttendees);
    }

    private BooleanExpression curriculumCategoryEq(CurriculumCategory curriculumCategory) {
        return curriculumCategory == null ? null : curriculum.category.eq(curriculumCategory);
    }
}