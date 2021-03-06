// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.gestalt.di.scanners;

import org.terasology.context.BeanDefinition;
import org.terasology.gestalt.di.BeanEnvironment;
import org.terasology.gestalt.di.BeanScanner;
import org.terasology.context.utils.BeanUtilities;
import org.terasology.gestalt.di.ServiceRegistry;
import org.terasology.context.injection.Qualifiers;

/**
 * A standard scanner that looks up by prefix loads them into the registry by common annotation ({@link org.terasology.context.annotation.Transient} , {@link javax.inject.Singleton}, {@link org.terasology.context.annotation.Scoped})
 */
public class StandardScanner implements BeanScanner {
    private final String prefix;
    private final ClassLoader[] loaders;

    public StandardScanner(String prefix, ClassLoader... loaders) {
        this.prefix = prefix;
        this.loaders = loaders;
    }

    public StandardScanner(String prefix) {
        this.prefix = prefix;
        loaders = new ClassLoader[]{};
    }

    @Override
    public void apply(ServiceRegistry registry, BeanEnvironment environment) {
        if (loaders.length == 0) {
            for (ClassLoader loader : environment.classLoaders()) {
                for (BeanDefinition definition : environment.byPrefix(loader, prefix)) {
                    loadDefinition(definition, registry);
                }
            }
        } else {
            for (ClassLoader loader : this.loaders) {
                for (BeanDefinition definition : environment.byPrefix(loader, prefix)) {
                    loadDefinition(definition, registry);
                }
            }
        }
    }

    private void loadDefinition(BeanDefinition definition, ServiceRegistry registry) {
        registry.with(definition.targetClass())
            .lifetime(BeanUtilities.resolveLifetime(definition.getAnnotationMetadata()))
            .byQualifier(Qualifiers.resolveQualifier(definition.getAnnotationMetadata()));
    }
}
