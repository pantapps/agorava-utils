/*
 * Copyright 2012 Agorava
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.agorava.utils.solder.reflection.annotated;

import org.agorava.utils.solder.reflection.HierarchyDiscovery;

import javax.enterprise.inject.spi.Annotated;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * The base class for all New Annotated types.
 *
 * @author Stuart Douglas
 * @author Ove Ranheim
 */
abstract class AnnotatedImpl implements Annotated {

    private final Type type;
    private final Set<Type> typeClosure;
    private final AnnotationStore annotations;

    protected AnnotatedImpl(Class<?> type, AnnotationStore annotations, Type genericType, Type overridenType) {
        if (overridenType == null) {
            if (genericType != null) {
                typeClosure = new HierarchyDiscovery(genericType).getTypeClosure();
                this.type = genericType;
            } else {
                typeClosure = new HierarchyDiscovery(type).getTypeClosure();
                this.type = type;
            }
        } else {
            this.type = overridenType;
            this.typeClosure = Collections.singleton(overridenType);
        }


        if (annotations == null) {
            this.annotations = new AnnotationStore();
        } else {
            this.annotations = annotations;
        }
    }

    public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
        return annotations.getAnnotation(annotationType);
    }

    public Set<Annotation> getAnnotations() {
        return annotations.getAnnotations();
    }

    public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
        return annotations.isAnnotationPresent(annotationType);
    }

    public Set<Type> getTypeClosure() {
        return new HashSet<Type>(typeClosure);
    }

    public Type getBaseType() {
        return type;
    }

}
