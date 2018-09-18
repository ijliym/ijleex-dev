/*
 * Copyright 2011-2018 ijym-lee
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * <p><a href="https://zeroturnaround.com/software/jrebel/download/#!/have-license">JRebel</a>介绍</p>
 *
 * <p>IDEA上原生是不支持热部署的，一般更新了 Java 文件后要手动重启 Tomcat 服务器，才能生效，浪费时间。</p>
 * <p>JRebel-IntelliJ 是 JDEA 的热部署插件。这个类实现修改字节码的方式破解JRebel插件。</p>
 *
 * <p>因为这种方式稍有瑕疵（发现有时候会失效），且我已经转为使用 Spring-Boot，“spring-boot-devtools” 支持热部署，故 JRebel 已经不使用了。保留这个类是为了记录历史。</p>
 *
 * @author liym
 * @since 2018-09-18 10:22 新建
 */
package me.ijleex.test.jrebel.activation;
