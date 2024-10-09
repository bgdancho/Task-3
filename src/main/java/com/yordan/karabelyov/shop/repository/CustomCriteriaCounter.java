package com.yordan.karabelyov.shop.repository;

public interface CustomCriteriaCounter<T extends CriteriaRepository.ParamHolder> {
    Integer count(T t);
}
