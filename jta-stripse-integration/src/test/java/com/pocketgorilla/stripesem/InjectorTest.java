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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 * @author Mike Baranczak
 */
public class InjectorTest {

    @Test
    public void testAnnotatedMembers() throws Exception {
        Injector ix = new Injector(AnnX.class);
        Injector.AnnotatedMembers xam = ix.getAnnotatedMembers(X.class);
        Assert.assertNotNull(xam);
        Assert.assertNotNull(xam.annFields);
        Assert.assertFalse(xam.annFields.isEmpty());
        Field fName = null;
        for (Field field: xam.annFields) {
            if ("name".equals(field.getName())) fName = field;
        }
        Assert.assertNotNull(fName);
    }
    
    @Test
    public void testInjectFields() throws Exception {
        Injector ix = new Injector(AnnX.class);
        X x = new X();
        Injector.Provider<String> stringProvider = new Injector.Provider<String>() {
            public String get() { return "qwerty"; }
        };
        ix.injectAnnotated(x, stringProvider);
        Assert.assertEquals(x.name, "qwerty");
        Assert.assertEquals(x.favoriteColor, "qwerty");
    }

    @Test
    public void testInjectMethods() throws Exception {
        Injector iy = new Injector(AnnY.class);
        X x = new X();
        Injector.Provider<Integer> intProvider = new Injector.Provider<Integer>() {
            public Integer get() { return 420; }
        };
        iy.injectAnnotated(x, intProvider);
        Assert.assertEquals(x.mw, 420);
    }
}

@Retention(value=RetentionPolicy.RUNTIME)
@interface AnnX {}

@Retention(value=RetentionPolicy.RUNTIME)
@interface AnnY {}

@Retention(value=RetentionPolicy.RUNTIME)
@interface AnnZ {}

class Y {

    @AnnX
    String name;
}

class X extends Y {

    String quest;

    @AnnZ
    @AnnX
    String favoriteColor;

    int mw = -1;
    
    @AnnY
    public void setMooseWeight(int mw) {
        this.mw = mw;
    }
}

