/*
 * Copyright 2011-2017 JacobLee007
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

package me.ijleex.test.jrebel.activation;

import java.io.IOException;
import java.util.Base64;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import org.junit.jupiter.api.Test;

/**
 * 使用 javassist 修改 JRebel-IntelliJ Class 文件的字节码，激活 JRebel-IntelliJ
 * <br>
 * 主要是修改验证签名的方法，使方法直接返回 true（ if (true) {return true;} ）
 * <p>
 * javassist（org.javassist:javassist:3.22.0-GA）
 *
 * <hr/>
 * <small>
 * <p>
 * <ui>
 * <li>2017-07-18 09:42:32 经过这几天使用 IdeaIU-172.3317.53，发现并确认了一个很严重
 * 的问题：这个版本对输入法的支持有很大的问题，连续试了好几个输入法，有极点五笔输入法、谷歌拼音输入法，
 * 还有今天新安装的华宇拼音输入法；打字时，都会出现不能显示输入框的问题。
 * </li>
 * <li>
 * 2017-07-19 09:13:58 IdeaIU-2017.2 (IU-172.3317.76) 前几天下载的 IdeaIU-172.3317.53，
 * 对输入法支持的支持不好，我以为是 EAP 版本的原因，而且在 youtrack.jetbrains.com 有很多人提了这个问题，
 * 我也提交了，而且还得到回答，所以今天出了 2017.2 的正式版，我就下载了这个正式版，尝试之后，很郁闷，
 * 我喜欢的极点五笔和谷歌拼音还是不能使用。这时，我就下载了一个QQ拼音输入法，试过之后，居然能用，实在是太郁闷了。
 * ——QQ拼音输入法好丑！听说极点五笔和谷歌拼音这些很久没更新的输入法，在Win10系统中就不能很好的使用，IdeaIU-2017.2
 * 又使用了类似 Win10 的界面，我觉得内中有些关联！
 * </li>
 * <li>
 * 2017-07-19 20:52:44 收到 Hao Ding&lt;no_reply@jetbrains.com&gt; 的邮件，让我用 Oracle JRE 替换
 * IdeaIU-2017.2 自带的 jre64（OpenJDK Runtime Environment (build 1.8.0_152-release-915-b5)）；
 * 我用 IdeaIU-2017.1.5 的 jre64 替换原本的 jre64，果然可以使用极点五笔和谷歌拼音这些输入法了。
 * </li>
 * </ui>
 * </p>
 * </small>
 *
 * <a href="https://zeroturnaround.com/software/jrebel/download/changelog/2018-x/#intellij">JRebel 2018.x Changelog</a>
 * <br/>
 * <a href="https://confluence.jetbrains.com/display/IDEADEV/IntelliJ+IDEA+2018.1+Release+Notes">
 * IntelliJ IDEA 2018.1 Release Notes (Mar 27, 2018)</a>
 *
 * @author 李远明
 * @version 2018-09-18 11:18 Moved from "user-manager" project to "ijleex-dev" project
 * @since 2017-04-27 13:33:53 新建
 */
class JRebelActivationTest {

    /**
     * Invalid signature!
     *
     * @version 2017-09-19 16:27:25 Windows 10 Professional (1703, 15063.13) @Company
     * @version 2018-01-19 15:13:41 Windows 10 Professional (1709, 16299.125) @Company
     * @since 2017-04-27 17:47:28
     */
    @Test
    void test00() {
        String str = "SW52YWxpZCBzaWduYXR1cmUh";
        byte[] bytes = Base64.getDecoder().decode(str);
        System.out.println(new String(bytes)); // Invalid signature!
    }

    /**
     * 修改验证签名的方法的字节码，使方法直接返回 true
     *
     * <p>这个方法修改的类与方法，名称是固定的，即：RSADigestSigner#verifySignature；启动IDEA，会在 ~/.jrebel/jrebel-licensing.log
     * 日志文件中看到 “Invalid signature!” 异常信息。
     * </p>
     * <p>
     * 执行这个方法后，会在当前项目根目录下，生成一个文件，路径为 org/zeroturnaround/../signers/RSADigestSigner.class，
     * 将 RSADigestSigner.class 文件替换 <b>jr-ide-idea/lib/jrebel-config-client-7.1.6.jar</b> 文件中对应的文件，重启 IDEA，
     * 即可在 ~/.jrebel/jrebel-licensing.log 日志文件中，看到验证签名成功的日志信息。
     * </p>
     * <p>
     * 但是，这样还是不行，运行 JRebel 时，还是会在控制台中出现验证签名不成功的信息：“UNABLE TO INITIALIZE LICENSING - NO LICENSE FOUND”，
     * 这时，请执行 {@link #test02()} 方法，进行类似的操作。
     * </p>
     *
     * @throws NotFoundException insertClassPath
     * @author 李远明
     * @version 2017-05-03 09:12 JRebel 7.0.8
     * <blockquote>
     * jrebel-config-client-7.0.8.jar:
     * <pre>
     * java.lang.Exception: Invalid signature!
     * at com.zeroturnaround.javarebel.av.a(JRebel:1098) ~[na:7.0.8]
     * at com.zeroturnaround.javarebel.at.a(JRebel:77) [na:7.0.8]
     * ..
     * </pre>
     * </blockquote>
     * @version 2017-05-23 09:53 JRebel 7.0.9
     * @see #test02()
     * @since 2017-04-27 15:20:11 新建
     */
    @Test
    void test01() throws NotFoundException {
        ClassPool cp = ClassPool.getDefault();

        // 取得需要反编译的jar文件，设定路径
        String path = "D:/Workspace/JetBrains/IdeaIU-2018.1.win/_IntelliJIdea/config/plugins/jr-ide-idea/lib/jrebel-config-client-2018.1.0.jar";
        cp.insertClassPath(path);

        // 取得需要反编译修改的Class文件，注意是完整路径
        // org.zeroturnaround.bundled.org.bouncycastle.crypto.signers.RSADigestSigner#verifySignature(byte[])
        String className = "org.zeroturnaround.bundled.org.bouncycastle.crypto.signers.RSADigestSigner";
        CtClass cc = cp.get(className);

        // 取得需要修改的方法
        String methodName = "verifySignature";
        CtMethod method = cc.getDeclaredMethod(methodName);

        CtClass returnType = method.getReturnType();
        String returnName = returnType.getName();
        System.out.println(returnType + "\t\t return: " + returnName);

        modifyMethod(cc, method); // 修改签名方法 2017-05-03 20:37:11
    }

    /**
     * 修改验证签名的方法的字节码，使方法直接返回 true
     *
     * <p style="color:red;">
     * 请先完成 {@link #test01()} 方法中的步骤。
     * </p>
     * <p>
     * 完成 test01() 中的步骤之后，运行 JRebel，这时控制台会出现 “JRebel:  UNABLE TO INITIALIZE LICENSING - NO LICENSE FOUND”
     * 的输出信息；同时，~/.jrebel/jrebel-licensing.log 日志文件会出现 “Invalid signature!” 的异常信息，
     * 根据异常信息，反编译找到混淆后的签名方法。
     * </p>
     * <p>查找关键字：SW52YWxpZCBzaWduYXR1cmUh</p>
     * <p>
     * 再修改本方法中的 className 变量的值，并执行。其余步骤，参考 {@link #test01()} 方法的注释。
     * 修改的文件为 <b>jr-ide-idea/lib/jrebel6/jrebel.jar</b>。
     * </p>
     * <p>JRebel: First redeploy prevented! You're on the right track! Keep on!</p>
     *
     * @throws NotFoundException insertClassPath
     * @author 李远明
     * @version 2017-05-03 09:12 JRebel 7.0.8
     * <blockquote>
     * jrebel.jar:
     * <pre>
     * java.lang.Exception: Invalid signature!
     * at com.zeroturnaround.javarebel.kc.a(SourceFile:98)
     * at com.zeroturnaround.javarebel.kc.a(SourceFile:75)
     * at com.zeroturnaround.javarebel.ka.a(SourceFile:77)
     * ..
     * </pre>
     * </blockquote>
     * @version 2017-05-03 10:50 用这种暴力破解的方式，好像有 Bug，还没研究完成。
     * @version 2017-05-23 09:53 JRebel 7.0.9：com.zeroturnaround.javarebel.dsz
     * @version 2017-06-13 09:38:52 JRebel 7.0.10：com.zeroturnaround.javarebel.dte
     * <blockquote>
     * jrebel.jar:
     * <pre>
     * at com.zeroturnaround.javarebel.kb.a(SourceFile:98)  --> a(UserLicense): String str = "SW52YWxpZCBzaWduYXR1cmUh"
     * at com.zeroturnaround.javarebel.kb.a(SourceFile:75)
     * at com.zeroturnaround.javarebel.jz.a(SourceFile:69)
     * at com.zeroturnaround.javarebel.SDKLicensingImpl.findAndValidateLicenseFromSource(SourceFile:572)
     * ..
     * </pre>
     * </blockquote>
     * @version 2017-07-07 08:53:17 JRebel 7.0.11：com.zeroturnaround.javarebel.dtm
     * @version 2017-07-11 17:46:01 JRebel 7.0.12：com.zeroturnaround.javarebel.dtm
     * @version 2017-08-01 08:58:50 JRebel 7.0.13 (31th July 2017): com.zeroturnaround.javarebel.dtu
     * @version 2017-08-22 21:46:54 JRebel 7.0.14 (22nd August 2017): com.zeroturnaround.javarebel.dts
     * @version 2017-09-11 22:00:22 JRebel 7.0.15 (11th September 2017): com.zeroturnaround.javarebel.dxe
     * @version 2017-10-08 15:53:59 JRebel 7.1.0 (28th September 2017): com.zeroturnaround.javarebel.dyi
     * @version 2017-10-20 10:08:35 JRebel 7.1.1 (19th October 2017): com.zeroturnaround.javarebel.eav
     * @version 2017-11-10 09:10:40 JRebel 7.1.2 (9th November 2017): com.zeroturnaround.javarebel.ebe
     * @version 2017-12-12 12:35:54 JRebel 7.1.3 (30th November 2017): com.zeroturnaround.javarebel.ebl
     * @version 2017-12-13 15:21:52 移除 test01、test02 两个方法的注释中关于 IDEA 的更新日志；合并 JRebel 的更新日志到 test02 方法
     * @version 2017-12-21 09:10:51 JRebel 7.1.4 (20th December 2017): com.zeroturnaround.javarebel.ebk
     * @version 2018-01-19 16:52:20 JRebel 7.1.5 (17th January 2018): com.zeroturnaround.javarebel.ebk
     * @version 2018-02-20 21:05:51 JRebel 7.1.6 (6th February 2018): com.zeroturnaround.javarebel.eak
     * @version 2018-03-02 10:00:37 JRebel 7.1.7 (1st March 2018): com.zeroturnaround.javarebel.eay
     * @version 2018-04-08 09:27:05 JRebel 2018.1.0 (2nd April 2018): 原 License 已失效，且已转到 Spring-Boot，故不再使用 JRebel
     * @see #test01()
     * @since 2017-04-27 16:41:46 新建
     */
    @Test
    void test02() throws NotFoundException {
        ClassPool cp = ClassPool.getDefault();

        // 取得需要反编译的jar文件，设定路径
        String path = "D:/Workspace/JetBrains/IdeaIU-2018.1.win/_IntelliJIdea/config/plugins/jr-ide-idea/lib/jrebel6/jrebel.jar";
        cp.insertClassPath(path);

        // 取得需要反编译修改的Class文件，注意是完整路径：com.zeroturnaround.javarebel.???#a(byte[])
        String className = "com.zeroturnaround.javarebel.eay";
        CtClass cc = cp.get(className); // 混淆后的签名类，更新版本后，可能不一样

        // 取得需要修改的方法名，即混淆后的签名方法名：throw new IllegalStateException("RSADigestSigner not initialised for verification");
        String methodName = "a";
        CtMethod[] methods = cc.getDeclaredMethods(methodName);
        for (CtMethod method : methods) {
            CtClass returnType = method.getReturnType();
            String returnName = returnType.getName();
            System.out.println(returnType + "\t\t return: " + returnName);

            if ("boolean".equals(returnName)) { // 返回类型为 boolean
                modifyMethod(cc, method); // 修改签名方法 2017-05-03 20:37:11
            }
        }
    }

    /**
     * 使方法直接返回 true
     *
     * @param cc Class文件
     * @param method 方法
     * @since 2017-05-03 20:31 合并方法
     */
    private void modifyMethod(CtClass cc, CtMethod method) {
        String src = "if(true){return true;}"; // if(1!=0){return true;}
        try {
            method.insertBefore(src); // 插入修改项，我们让他直接返回 true

            // 写入保存
            cc.writeFile();
            System.out.println("修改 “签名方法” 方法完成。");
        } catch (CannotCompileException e) { // insertBefore, writeFile
            System.out.println("修改 “签名方法” 方法失败，语法错误：" + src);
            e.printStackTrace();
        } catch (IOException e) { // writeFile
            System.out.println("修改 “签名方法” 方法失败：");
            e.printStackTrace();
        } catch (NotFoundException e) { // writeFile
            System.out.println("签名类不存在：");
            e.printStackTrace();
        }
    }

}
