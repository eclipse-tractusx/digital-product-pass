/*********************************************************************************
 *
 * Tractus-X - Digital Product Passport Application
 *
 * Copyright (c) 2022, 2024 BMW AG, Henkel AG & Co. KGaA
 * Copyright (c) 2023, 2024 CGI Deutschland B.V. & Co. KG
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

/**
 * This class consists exclusively of methods to operate at runtime execution (Reflection).
 *
 * <p> The methods defined here are intended to inspect, retrieve or manipulate other code in the same system at the runtime.
 *
 */
public final class ReflectionUtil {
    private ReflectionUtil() {
        throw new IllegalStateException("Tool/Utility Class Illegal Initialization");
    }

    /**
     * Gets the simple class name of a given {@code Class} object.
     * <p>
     * @param   classObj
     *          the Class object to retrieve the name.
     *
     * @return  a {@code String} object representing the name of the Class.
     *
     * @throws UtilException
     *          if unable to read the YAML file.
     */
    public static String getCurrentClassName(Class classObj){
        return classObj.getSimpleName();
    }
    public static Boolean classIsTest(Class classObj){
        return ReflectionUtil.getCurrentClassName(classObj).contains("test");
    }
    public static Class<?> instanceClass(String packagePath, String className){
        try {
            String classPath = packagePath + "." + className;
            return Class.forName(classPath);
        }catch (ClassNotFoundException e){
            throw new UtilException(ReflectionUtil.class, e, "It was not possible to instance class, class ["+packagePath+"."+className+"] not found!");
        }catch (Exception e){
            throw new UtilException(ReflectionUtil.class, e, "It was not possible to instance class!");
        }
    }
}
