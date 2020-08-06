package com.hzcominfo.processor;

import com.google.auto.service.AutoService;
import com.hzcominfo.annotation.ARouter;

import java.io.IOException;
import java.io.Writer;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * Create by Ljw on 2020/8/6 15:04
 *
 * AutoService则是固定的写法，加个注解即可
 * 通过auto-service中的@AutoService可以自动生成AutoService注解处理器，用来注册
 * 这里写时一定要注意，因为不同包相同的类太多，特别容易出错，我把Processor写成了Process，不报错，找了一个多小时的才发现！！！
 */
// 用来生成 processor/build/classes/java/main/META-INF/services/javax.annotation.processing.Processor 文件
@AutoService(Processor.class)
// 允许/支持的注解类型，让注解处理器处理（新增annotation module）
@SupportedAnnotationTypes("com.hzcominfo.annotation.ARouter")
// 指定JDK编译版本
@SupportedSourceVersion(SourceVersion.RELEASE_7)
// 注解处理器接收的参数
@SupportedOptions("content")
public class ARouterProcessor extends AbstractProcessor {

    // 操作Element工具类 (类、函数、属性都是Element)
    private Elements elementsUtils;

    // type(类信息)工具类，包含用于操作TypeMirror的工具方法
    private Types typesUtils;

    // Messager用来报告错误、警告和其他提示信息
    private Messager messagerUtils;

    // 文件生成器 类/资源，Filter用来创建新的源文件，class文件以及辅助文件
    private Filer filerUtils;

    // 该方法主要用于一些初始化的操作，通过该方法的参数ProcessingEnvironment可以获取一些列有用的工具类


    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        // 父类受保护属性，可以直接拿来使用。
        // 其实就是init方法的参数ProcessingEnvironment
        // processingEnv.getMessager(); //参考源码64行
        elementsUtils = processingEnv.getElementUtils();
        typesUtils = processingEnv.getTypeUtils();
        messagerUtils = processingEnv.getMessager();
        filerUtils = processingEnv.getFiler();

        // 通过ProcessingEnvironment去获取build.gradle传过来的参数
        String content = processingEnv.getOptions().get("content");
        // 有坑：Diagnostic.Kind.ERROR，异常会自动结束，不像安卓中Log.e那么好使
        messagerUtils.printMessage(Diagnostic.Kind.NOTE, content);
    }

    /**
     * 相当于main函数，开始处理注解
     * 注解处理器的核心方法，处理具体的注解，生成Java文件
     *
     * @param annotations 使用了支持处理注解的节点集合（类 上面写了注解）
     * @param roundEnv 当前或是之前的运行环境,可以通过该对象查找找到的注解。
     * @return true 表示后续处理器不会再处理（已经处理完成）
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.isEmpty()) return false;
        // 获取所有带ARouter注解的 类节点
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(ARouter.class);
        for (Element element: elements) {
            // 通过类节点获取包节点（全路径：com.hzcominfo.xxx）
            String packageName = elementsUtils.getPackageOf(element).getQualifiedName().toString();
            // 获取简单类名
            String className = element.getSimpleName().toString();
            messagerUtils.printMessage(Diagnostic.Kind.NOTE, "被注解的类有：" + className);
            // 最终想生成的类文件名
            String finalClassName = className + "$$ARouter";

            // 自动生成对应的类文件，EventBus写法（https://github.com/greenrobot/EventBus）
            try {
                // 创建一个新的源文件（Class），并返回一个对象以允许写入它
                JavaFileObject sourceFile = filerUtils.createSourceFile(packageName + "." + finalClassName);
                // 定义Writer对象，开启写入
                Writer writer = sourceFile.openWriter();
                // 设置包名
                writer.write("package " + packageName + ";\n");

                writer.write("public class " + finalClassName + " {\n");

                writer.write("public static Class<?> findTargetClass(String path) {\n");

                // 获取类之上@ARouter注解的path值
                ARouter aRouter = element.getAnnotation(ARouter.class);

                writer.write("if (path.equals(\"" + aRouter.path() + "\")) {\n");

                writer.write("return " + className + ".class;\n}\n");

                writer.write("return null;\n");

                writer.write("}\n}");

                // 最后结束别忘了
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return true;
    }
}
