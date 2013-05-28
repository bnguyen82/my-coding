/*
 * Copyright (C) 2010 Michael Baranczak.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pocketgorilla.stripesem;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/** A minimal framework for implementing annotation-based dependency injection.
 *
 * @author Mike Baranczak
 */
public class Injector {

    private Map<Class, AnnotatedMembers> classCache =
            Collections.synchronizedMap( new HashMap<Class, AnnotatedMembers>() );

    private Class annotationType;

    public <A extends Annotation> Injector(Class<A> annotationType) {
        this.annotationType = annotationType;
    }

    public void injectAnnotated(Object object, Provider provider) 
            throws IllegalAccessException, InvocationTargetException {

        injectAnnotatedFields(object, provider);
        injectAnnotatedMethods(object, provider);
    }

    public void injectAnnotatedFields(Object object, Provider provider) throws IllegalAccessException {
        if (object != null) {
            AnnotatedMembers am = getAnnotatedMembers(object.getClass());
            if (am.annFields != null) {
                for (Field field: am.annFields) {
                    field.setAccessible(true);
                    field.set(object, provider.get());
                }
            }
        }
    }

    public void injectAnnotatedMethods(Object object, Provider provider) 
            throws IllegalAccessException, InvocationTargetException {

        if (object != null) {
            AnnotatedMembers am = getAnnotatedMembers(object.getClass());
            if (am.annMethods != null) {
                for (Method method: am.annMethods) {
                    method.setAccessible(true);
                    method.invoke(object, provider.get());
                }
            }
        }
    }

    AnnotatedMembers getAnnotatedMembers(Class type) {
        AnnotatedMembers am = classCache.get(type);
        if (am != null) {
            return am;
        } else {
            am = new AnnotatedMembers();
            //am.type = type;
            
            // get annotated fields
            Collection<Field> fields = getAllFields(type);
            for (Field field: fields) {
                if (field.getAnnotation(annotationType) != null) {
                    am.addField(field);
                }
            }

            // get annotated methods
            Collection<Method> methods = getAllMethods(type);
            for (Method method: methods) {
                if (method.getAnnotation(annotationType) != null) {
                    am.addMethod(method);
                }
            }
            
            classCache.put(type, am);
            return am;
        }
    }


    /**
     * Adds all elements in the array to the given collection.
     *
     * @param collection  the collection to add to, must not be null
     * @param elements  the array of elements to add, must not be null
     * @throws NullPointerException if the collection or array is null
     */
    static void addAll(Collection collection, Object[] elements) {
        for (int i = 0, size = elements.length; i < size; i++) {
            collection.add(elements[i]);
        }
    }


    /** Returns all the fields (public or not) of the specified class and all its superclasses,
     * excluding java.lang.Object. */
    static Collection<Field> getAllFields(Class clazz) {
        if (clazz == null) {
            return null;
        } else {
            ArrayList<Field> fields = new ArrayList<Field>();
            addAll(fields, clazz.getDeclaredFields());
            Class parent = clazz.getSuperclass();
            while ((parent != null) && (!Object.class.equals(parent))) {
                addAll(fields, parent.getDeclaredFields());
                parent = parent.getSuperclass();
            }
            return fields;
        }
    }

    static Collection<Method> getAllMethods(Class clazz) {
        if (clazz == null) {
            return null;
        } else {
            ArrayList<Method> methods = new ArrayList<Method>();
            addAll(methods, clazz.getDeclaredMethods());
            Class parent = clazz.getSuperclass();
            while ((parent != null) && (!Object.class.equals(parent))) {
                addAll(methods, parent.getDeclaredMethods());
                parent = parent.getSuperclass();
            }
            return methods;
        }
    }


    /** The Injector class requires a Provider implementation to obtain the objects to
     * be injected.
     */
    public static interface Provider<T> {

        /** Return the object to be injected.
         */
        public T get();
    }

    static class AnnotatedMembers {
        //Class type;
        Collection<Method> annMethods;
        Collection<Field>  annFields;

        public void addMethod(Method m) {
            if (annMethods == null) {
                annMethods = new ArrayList<Method>();
            }
            annMethods.add(m);
        }

        public void addField(Field f) {
            if (annFields == null) {
                annFields = new ArrayList<Field>();
            }
            annFields.add(f);
        }
    }
}
