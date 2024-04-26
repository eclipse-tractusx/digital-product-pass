/*********************************************************************************
 *
 * Tractus-X - Digital Product Passport Application
 *
 * Copyright (c) 2022, 2024 BASF SE, BMW AG, Henkel AG & Co. KGaA
 * Copyright (c) 2022, 2024 Contributors to the Eclipse Foundation
 *
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the
 * License for the specific language govern in permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 ********************************************************************************/

package utils;

import utils.exceptions.UtilException;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class consists exclusively of methods to operate at runtime execution (Reflection).
 *
 * <p> The methods defined here are intended to inspect, retrieve or manipulate other code in the same system at the runtime.
 */
public final class ReflectionUtil {
    private ReflectionUtil() {
        throw new IllegalStateException("Tool/Utility Class Illegal Initialization");
    }

    /**
     * Gets the simple class name of a given {@code Class} object.
     * <p>
     *
     * @param classObj the Class object to retrieve the name.
     * @return a {@code String} object representing the name of the Class.
     * @throws UtilException if unable to read the YAML file.
     */
    public static String getCurrentClassName(Class<?> classObj) {
        return classObj.getSimpleName();
    }

    /**
     * Instances dynamically a class {@code Class} object with dynamic parameters.
     * <p>
     *
     * @param classObj       a {@code Class<T>} Class object to be instanced.
     * @param parameterTypes a {@code Object...} array of parameters to be added to the contructor
     * @return a {@code T} a instance from the given {@code classObj} object
     * @throws UtilException if unable to instance the class
     */
    public static <T> T instanceClass(Class<T> classObj, Object... parameterTypes) {
        try {
            // Get the constructor with the class types from the parameters
            Constructor<T> constructor = classObj.getConstructor(Arrays.stream(parameterTypes).map(Object::getClass).toArray(Class[]::new));
            // Instance the object dynamically and return it in the response
            return constructor.newInstance(parameterTypes);
        } catch (Exception e) {
            throw new UtilException(ReflectionUtil.class, e, "It was not possible to instance class ["+classObj.getName()+"]!");
        }
    }

    public static Boolean classIsTest(Class<?> classObj) {
        return ReflectionUtil.getCurrentClassName(classObj).contains("test");
    }

    public static Class<?> instanceClass(String packagePath, String className) {
        try {
            String classPath = packagePath + "." + className;
            return Class.forName(classPath);
        } catch (ClassNotFoundException e) {
            throw new UtilException(ReflectionUtil.class, e, "It was not possible to instance class, class [" + packagePath + "." + className + "] not found!");
        } catch (Exception e) {
            throw new UtilException(ReflectionUtil.class, e, "It was not possible to instance class!");
        }
    }
}
