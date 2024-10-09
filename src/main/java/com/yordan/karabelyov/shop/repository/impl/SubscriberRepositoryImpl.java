package com.yordan.karabelyov.shop.repository.impl;

import com.yordan.karabelyov.shop.entity.Subscriber;
import com.yordan.karabelyov.shop.enumeration.SortOrder;
import com.yordan.karabelyov.shop.enumeration.SubscriberOrderBy;
import com.yordan.karabelyov.shop.repository.CustomCriteriaCounter;
import com.yordan.karabelyov.shop.repository.OrderedCriteriaRepository;
import com.yordan.karabelyov.shop.repository.impl.criteria.holder.SubscriberCriteriaHolder;
import com.yordan.karabelyov.shop.repository.impl.criteria.holder.root.SimplePredicateGenerationHolder;
import com.yordan.karabelyov.shop.util.PaginationUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.graph.GraphSemantic;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.yordan.karabelyov.shop.entity.BaseAuditEntity.ATTR_CREATED_AT;
import static com.yordan.karabelyov.shop.entity.BaseEntity.ATTR_ID;
import static com.yordan.karabelyov.shop.entity.Subscriber.ATTR_PRODUCTS;

@Repository
public class SubscriberRepositoryImpl implements OrderedCriteriaRepository<Subscriber, SubscriberCriteriaHolder,
        SimplePredicateGenerationHolder<Subscriber>>,
        CustomCriteriaCounter<SubscriberCriteriaHolder> {
    private final EntityManager entityManager;

    public SubscriberRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Subscriber> findAllByCriteria(SubscriberCriteriaHolder subscriberCriteriaHolder) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Subscriber> criteriaQuery = criteriaBuilder.createQuery(Subscriber.class);
        Root<Subscriber> subscriberRoot = criteriaQuery.from(Subscriber.class);

        criteriaQuery.select(subscriberRoot)
                .where(resolvePredicates(subscriberCriteriaHolder, new SimplePredicateGenerationHolder<>(subscriberRoot)))
                        .orderBy(resolveOrderList(subscriberCriteriaHolder, subscriberRoot, criteriaBuilder));

        Integer pageSize = subscriberCriteriaHolder.getPageSize();
        Integer pageNumber = subscriberCriteriaHolder.getPageNumber();
        TypedQuery<Subscriber> typedQuery = entityManager.createQuery(criteriaQuery);

        if (pageNumber != null) {
            typedQuery.setFirstResult(PaginationUtil.calculateStartingResult(pageNumber, pageSize));
        }

        if (pageSize != null) {
            typedQuery.setMaxResults(pageSize);
        }

        typedQuery.setHint(GraphSemantic.FETCH.getJakartaHintName(), entityManager.getEntityGraph(ATTR_PRODUCTS));

        return typedQuery.getResultList();
    }

    @Override
    public Predicate[] resolvePredicates(SubscriberCriteriaHolder holder, SimplePredicateGenerationHolder<Subscriber> generationHolder) {
        Root<Subscriber> root = generationHolder.getRoot();
        List<Predicate> predicates = new ArrayList<>();
        List<Long> productIds = holder.getProductIds();

        if (productIds != null) {
            predicates.add(root.get(ATTR_PRODUCTS).get(ATTR_ID).in(productIds));
        }

        return predicates.toArray(Predicate[]::new);
    }


    @Override
    public Integer count(SubscriberCriteriaHolder subscriberCriteriaHolder) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Subscriber> subscriberRoot = criteriaQuery.from(Subscriber.class);

        criteriaQuery
                .select(criteriaBuilder.count(subscriberRoot))
                .where(resolvePredicates(subscriberCriteriaHolder, new SimplePredicateGenerationHolder<>(subscriberRoot)))
                        .orderBy(resolveOrderList(subscriberCriteriaHolder, subscriberRoot, criteriaBuilder));

        return entityManager.createQuery(criteriaQuery).getSingleResult().intValue();
    }

    @Override
    public List<Order> resolveOrderList(SubscriberCriteriaHolder holder, Root<Subscriber> root, CriteriaBuilder cb) {
        List<Order> orderList = new ArrayList<>();

        if (SubscriberOrderBy.CREATED_AT.equals(holder.getSubscriberOrderBy())) {
            if (SortOrder.DESC.equals(holder.getSortOrder())) {
                orderList.add(cb.desc(root.get(ATTR_CREATED_AT)));
                orderList.add(cb.desc(root.get(ATTR_ID)));
            } else {
                orderList.add(cb.asc(root.get(ATTR_CREATED_AT)));
                orderList.add(cb.asc(root.get(ATTR_ID)));
            }
        }
        return orderList;
    }
}
