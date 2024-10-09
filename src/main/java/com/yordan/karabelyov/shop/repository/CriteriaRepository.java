package com.yordan.karabelyov.shop.repository;

import com.yordan.karabelyov.shop.entity.BaseEntity;
import jakarta.persistence.criteria.Predicate;

import java.util.List;

import static com.yordan.karabelyov.shop.repository.CriteriaRepository.ParamHolder;
import static com.yordan.karabelyov.shop.repository.CriteriaRepository.PredicateGenerationHolder;

public interface CriteriaRepository<
        E extends BaseEntity,
        T extends ParamHolder,
        P extends PredicateGenerationHolder> {

    interface ParamHolder {
    }

    interface PredicateGenerationHolder {
    }

    List<E> findAllByCriteria(T t);

    Predicate[] resolvePredicates(T t, P p);

}
