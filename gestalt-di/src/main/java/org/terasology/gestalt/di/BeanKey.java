// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.gestalt.di;

import org.terasology.context.AnnotationMetadata;
import org.terasology.context.Argument;
import org.terasology.context.BeanDefinition;
import org.terasology.context.EmptyAnnotationMetadata;
import org.terasology.context.injection.Qualifier;
import org.terasology.context.injection.Qualifiers;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * A key that is used to look up a Bean.
 * @param <T>
 */
public class BeanKey<T> implements Serializable, CharSequence {
    protected Class<? extends T> baseType;
    protected Class<T> implementingType;
    protected Class[] typeArguments;
    protected Qualifier<T> qualifier;
    protected AnnotationMetadata annotation = EmptyAnnotationMetadata.EMPTY_ARGUMENT;
    private int hashCode;

    public static <T> BeanKey<? extends T> resolveBeanKey(Class<T> implClass, Class<? extends T> clazz, Argument<?> argument) {
        Qualifier<T> qualifier = Qualifiers.resolveQualifier(argument.getAnnotation());
        return new BeanKey(implClass, clazz, qualifier);
    }

    public static <T> BeanKey<T> resolveBeanKey(Class<T> clazz, Argument<?> argument) {
        Qualifier<T> qualifier = Qualifiers.resolveQualifier(argument.getAnnotation());
        return new BeanKey(clazz, qualifier);
    }

    private void updateHash() {
        int result = 1;
        if (baseType == implementingType) {
            result = result * 31 + baseType.hashCode();
        } else {
            result = result * 31 + baseType.hashCode();
            result = result * 31 + implementingType.hashCode();
        }
        if (qualifier != null) {
            result = result * 31 + qualifier.hashCode();
        }
        this.hashCode = 31 * result + Arrays.hashCode(this.typeArguments);
    }

    public BeanKey(Class<T> root) {
        this.baseType = root;
        this.implementingType = root;
        updateHash();
    }

    public BeanKey<T> use(Class<T> target) {
        this.implementingType = target;
        updateHash();
        return this;
    }

    public BeanKey<T> qualifiedBy(Qualifier<T> qualifier){
        this.qualifier = qualifier;
        updateHash();
        return this;
    }

    public BeanKey<T> byArguments(Class... typeArguments) {
        this.typeArguments = typeArguments;
        updateHash();
        return this;
    }

    public BeanKey<T> withAnnotations(AnnotationMetadata annotation) {
        this.annotation = annotation;
        return this;
    }

    BeanKey(BeanDefinition<T> definition, Qualifier<T> qualifier) {
        this(definition.targetClass(), qualifier, definition.getTypeArgument());
    }

    public BeanKey(Class<T> implementingBean, Class<? extends T> beanType, Qualifier<T> qualifier, Class... typeArguments) {
        this.baseType = beanType;
        this.implementingType = implementingBean;
        this.qualifier = qualifier;
        this.typeArguments = (typeArguments == null || typeArguments.length == 0) ? null : typeArguments;
        updateHash();
    }

    public BeanKey(Class<T> beanType, Qualifier<T> qualifier, Class... typeArguments) {
        this(beanType, beanType, qualifier, typeArguments);
    }

    public Class<? extends T> getBaseType() {
        return baseType;
    }

    @Override
    @Nonnull
    public String toString() {
        return baseType.getName();
    }

    @Override
    public int length() {
        return toString().length();
    }

    @Override
    public char charAt(int i) {
        return toString().charAt(i);
    }

    @Override
    public CharSequence subSequence(int i, int i1) {
        return toString().subSequence(i, i1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BeanKey<?> beanKey = (BeanKey<?>) o;
        return hashCode == beanKey.hashCode &&
            Objects.equals(implementingType, beanKey.implementingType) &&
            Objects.equals(baseType, beanKey.baseType) &&
            Arrays.equals(typeArguments, beanKey.typeArguments);
    }

    @Override
    public int hashCode() {
        return hashCode;
    }
}
