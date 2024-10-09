package com.yordan.karabelyov.shop.repository.impl.criteria.holder.root;

import com.yordan.karabelyov.shop.entity.BaseEntity;
import com.yordan.karabelyov.shop.repository.CriteriaRepository;
import jakarta.persistence.criteria.Root;

import java.util.Objects;

public class SimplePredicateGenerationHolder<E extends BaseEntity> implements CriteriaRepository.PredicateGenerationHolder {
    private Root<E> root;

    public SimplePredicateGenerationHolder(Root<E> root) {
        this.root = root;
    }

    public Root<E> getRoot() {
        return root;
    }

    public void setRoot(Root<E> root) {
        this.root = root;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimplePredicateGenerationHolder<?> that)) return false;
        return Objects.equals(root, that.root);
    }

    @Override
    public int hashCode() {
        return Objects.hash(root);
    }
}
