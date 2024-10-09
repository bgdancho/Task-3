package com.yordan.karabelyov.shop.repository.impl;

import com.yordan.karabelyov.shop.entity.Product;
import com.yordan.karabelyov.shop.enumeration.ProductOrderBy;
import com.yordan.karabelyov.shop.enumeration.SortOrder;
import com.yordan.karabelyov.shop.repository.CustomCriteriaCounter;
import com.yordan.karabelyov.shop.repository.OrderedCriteriaRepository;
import com.yordan.karabelyov.shop.repository.impl.criteria.holder.ProductCriteriaHolder;
import com.yordan.karabelyov.shop.repository.impl.criteria.holder.root.SimplePredicateGenerationHolder;
import com.yordan.karabelyov.shop.util.PaginationUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.graph.GraphSemantic;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.yordan.karabelyov.shop.entity.BaseAuditEntity.ATTR_CREATED_AT;
import static com.yordan.karabelyov.shop.entity.BaseEntity.ATTR_ID;
import static com.yordan.karabelyov.shop.entity.Product.ATTR_STATUS;
import static com.yordan.karabelyov.shop.entity.Product.ATTR_SUBSCRIBERS;

@Repository
public class ProductRepositoryImpl implements OrderedCriteriaRepository<Product, ProductCriteriaHolder,
        SimplePredicateGenerationHolder<Product>>, CustomCriteriaCounter<ProductCriteriaHolder> {

    private final EntityManager entityManager;

    public ProductRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Integer count(ProductCriteriaHolder productCriteriaHolder) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Product> productRoot = criteriaQuery.from(Product.class);

        criteriaQuery
                .select(criteriaBuilder.count(productRoot))
                .where(resolvePredicates(productCriteriaHolder, new SimplePredicateGenerationHolder<>(productRoot)))
                .orderBy(resolveOrderList(productCriteriaHolder, productRoot, criteriaBuilder));

        return entityManager.createQuery(criteriaQuery).getSingleResult().intValue();
    }


    public List<Order> resolveOrderList(ProductCriteriaHolder holder, Root<Product> root, CriteriaBuilder cb) {
        List<Order> orderList = new ArrayList<>();

        if (ProductOrderBy.CREATED_AT.equals(holder.getProductOrderBy())) {
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

    @Override
    public List<Product> findAllByCriteria(ProductCriteriaHolder productCriteriaHolder) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<Product> productRoot = criteriaQuery.from(Product.class);

        productRoot.fetch(ATTR_STATUS, JoinType.LEFT);

        criteriaQuery.select(productRoot)
                .where(resolvePredicates(productCriteriaHolder, new SimplePredicateGenerationHolder<>(productRoot)))
                .orderBy(resolveOrderList(productCriteriaHolder, productRoot, criteriaBuilder));

        Integer pageSize = productCriteriaHolder.getPageSize();
        Integer pageNumber = productCriteriaHolder.getPageNumber();
        TypedQuery<Product> typedQuery = entityManager.createQuery(criteriaQuery);

        if (pageNumber != null) {
            typedQuery.setFirstResult(PaginationUtil.calculateStartingResult(pageNumber, pageSize));
        }

        if (pageSize != null) {
            typedQuery.setMaxResults(pageSize);
        }

        typedQuery.setHint(GraphSemantic.FETCH.getJakartaHintName(), entityManager.getEntityGraph(ATTR_SUBSCRIBERS));

        return typedQuery.getResultList();
    }

    @Override
    public Predicate[] resolvePredicates(ProductCriteriaHolder holder, SimplePredicateGenerationHolder<Product> generationHolder) {
        Root<Product> root = generationHolder.getRoot();
        List<Predicate> predicates = new ArrayList<>();
        List<Long> subscriberIds = holder.getSubscriberIds();

        if (subscriberIds != null) {
            predicates.add(root.get(ATTR_SUBSCRIBERS).get(ATTR_ID).in(subscriberIds));
        }

        return predicates.toArray(Predicate[]::new);
    }
}
