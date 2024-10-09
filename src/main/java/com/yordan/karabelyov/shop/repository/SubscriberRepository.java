package com.yordan.karabelyov.shop.repository;

import com.yordan.karabelyov.shop.entity.Subscriber;
import com.yordan.karabelyov.shop.repository.impl.criteria.holder.SubscriberCriteriaHolder;
import com.yordan.karabelyov.shop.repository.impl.criteria.holder.root.SimplePredicateGenerationHolder;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, Long>,
        SubscriberRepositoryCustom, CriteriaRepository<Subscriber, SubscriberCriteriaHolder, SimplePredicateGenerationHolder<Subscriber>> {
    @EntityGraph(value = Subscriber.ATTR_PRODUCTS, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Subscriber> findById(@Param("id") Long id);
}
