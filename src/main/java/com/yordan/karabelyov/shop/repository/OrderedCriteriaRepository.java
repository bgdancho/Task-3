package com.yordan.karabelyov.shop.repository;

import com.yordan.karabelyov.shop.entity.BaseEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;

import java.util.List;

import static com.yordan.karabelyov.shop.repository.CriteriaRepository.ParamHolder;
import static com.yordan.karabelyov.shop.repository.CriteriaRepository.PredicateGenerationHolder;

public interface OrderedCriteriaRepository<E extends BaseEntity, T extends ParamHolder, C extends PredicateGenerationHolder> extends CriteriaRepository<E, T, C> {
    List<Order> resolveOrderList(T holder, Root<E> root, CriteriaBuilder cb);
}
