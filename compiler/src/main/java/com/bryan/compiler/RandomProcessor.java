package com.bryan.compiler;

import com.bryan.annotations.OnClick;
import com.bryan.annotations.RandomInt;
import com.bryan.annotations.RandomString;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class RandomProcessor extends AbstractProcessor {

    private static final String RANDOM_SUFFIX = "_Random";
    private static final String TARGET_STATEMENT_FORMAT = "target.%1$s = %2$s";
    private static final String CONST_PARAM_TARGET_NAME = "target";

    private static final char CHAR_DOT = '.';

    private Messager messager;
    private Types typesUtil;
    private Elements elementsUtil;
    private Filer filer;

    /**
     * 可选 在该方法中可以获取到processingEnvironment对象，借由该对象可以获取到生成代码的文件对象,
     * debug输出对象，以及一些相关工具类
     * @param processingEnv
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
        typesUtil = processingEnv.getTypeUtils();
        elementsUtil = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
    }

    /**
     * getSupportedSourceVersion() 返回所支持的java版本，一般返回当前所支持的最新java版本即可
     * @return
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    /**
     * 你所需要处理的所有注解，该方法的返回值会被process()方法所接收
     * @return
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        HashSet<String> supportTypes = new LinkedHashSet<>();
        supportTypes.add(RandomInt.class.getCanonicalName());
        supportTypes.add(RandomString.class.getCanonicalName());
        supportTypes.add(OnClick.class.getCanonicalName());
        return supportTypes;
    }

    /**
     * 必须实现 扫描所有被注解的元素，并作处理，最后生成文件。该方法的返回值为boolean类型，
     * 若返回true,则代表本次处理的注解已经都被处理，
     * 不希望下一个注解处理器继续处理，否则下一个注解处理器会继续处理。
     * @param annotations
     * @param roundEnv
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        messager.printMessage(Diagnostic.Kind.NOTE, "process");
        Map<String, List<AnnotatedRandomElement>> annotatedElementMap = new LinkedHashMap<>();

        for (Element element : roundEnv.getElementsAnnotatedWith(RandomInt.class)) {
            AnnotatedRandomInt randomElement = new AnnotatedRandomInt(element);
            messager.printMessage(Diagnostic.Kind.NOTE, randomElement.toString());
            if (!randomElement.isTypeValid(elementsUtil, typesUtil)) {
                messager.printMessage(Diagnostic.Kind.ERROR, randomElement.getSimpleClassName().toString() + "#"
                        + randomElement.getElementName().toString() + " is not in valid type int");
            }
            addAnnotatedElement(annotatedElementMap, randomElement);
        }

        for (Element element : roundEnv.getElementsAnnotatedWith(RandomString.class)) {
            AnnotatedRandomString randomElement = new AnnotatedRandomString(element);
            messager.printMessage(Diagnostic.Kind.NOTE, randomElement.toString());
            if (!randomElement.isTypeValid(elementsUtil, typesUtil)) {
                messager.printMessage(Diagnostic.Kind.ERROR, randomElement.getSimpleClassName().toString() + "#"
                        + element.getSimpleName() + " is not in valid type String");
            }
            addAnnotatedElement(annotatedElementMap, randomElement);
        }

        if (annotatedElementMap.size() == 0) {
            return true;
        }

        try {
            for (Map.Entry<String, List<AnnotatedRandomElement>> entry : annotatedElementMap.entrySet()) {
                MethodSpec constructor = createConstructor(entry.getValue());
                TypeSpec binder = createClass(getClassName(entry.getKey()), constructor);
                JavaFile javaFile = JavaFile.builder(getPackage(entry.getKey()), binder).build();
                javaFile.writeTo(filer);
            }

        } catch (IOException e) {
            messager.printMessage(Diagnostic.Kind.ERROR, "Error on creating java file");
        }

        return true;
    }


    private MethodSpec createConstructor(List<AnnotatedRandomElement> randomElements) {
        AnnotatedRandomElement firstElement = randomElements.get(0);
        MethodSpec.Builder builder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(TypeName.get(firstElement.getElement().getEnclosingElement().asType()), CONST_PARAM_TARGET_NAME);
        for (int i = 0; i < randomElements.size(); i++) {
            addStatement(builder, randomElements.get(i));

        }
        return builder.build();
    }

    private void addStatement(MethodSpec.Builder builder, AnnotatedRandomElement randomElement) {
        builder.addStatement(String.format(
                TARGET_STATEMENT_FORMAT,
                randomElement.getElementName().toString(),
                randomElement.getRandomValue())
        );
    }

    private TypeSpec createClass(String className, MethodSpec constructor) {
        return TypeSpec.classBuilder(className + RANDOM_SUFFIX)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(constructor)
                .build();
    }

    private String getPackage(String qualifier) {
        return qualifier.substring(0, qualifier.lastIndexOf(CHAR_DOT));
    }

    private String getClassName(String qualifier) {
        return qualifier.substring(qualifier.lastIndexOf(CHAR_DOT) + 1);
    }

    private void addAnnotatedElement(Map<String, List<AnnotatedRandomElement>> map, AnnotatedRandomElement randomElement) {
        String qualifier = randomElement.getQualifiedClassName().toString();
        if (map.get(qualifier) == null) {
            map.put(qualifier, new ArrayList<AnnotatedRandomElement>());
        }
        map.get(qualifier).add(randomElement);
    }
}
