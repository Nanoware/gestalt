// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.context;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * blank argument definition
 */
public class EmptyAnnotationMetadata implements AnnotationMetadata {
   public final static EmptyAnnotationMetadata EMPTY_ARGUMENT = new EmptyAnnotationMetadata();

    @Override
    public <T extends Annotation>  List<AnnotationValue<Annotation>> getAnnotationsByStereotype(Class<T> stereotype) {
        return Collections.emptyList();
    }

    @Override
    public List<AnnotationValue<Annotation>> getAnnotationsByStereotype(String stereotype) {
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<AnnotationValue<Annotation>> findAnnotations(String annotation) {
        return null;
    }

    @Override
    public <T extends Annotation>   List<AnnotationValue<T>>  findAnnotations(Class<T> annotation) {
        return null;
    }

    @Override
    public boolean hasAnnotation(Class<? extends Annotation> annotation) {
        return false;
    }

    @Override
    public boolean hasAnnotation(String annotation) {
        return false;
    }

    @Override
    public boolean hasStereotype(Class<? extends Annotation> annotation) {
        return false;
    }

    @Override
    public boolean hasStereotype(String annotation) {
        return false;
    }

    @Override
    public Object getRawSingleValue(String annotation, String field) {
        return null;
    }

    @Override
    public Iterator<AnnotationValue<Annotation>[]> iterator() {
        return Collections.emptyIterator();
    }
}
