package org.springframework.samples.petclinic.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//메서드에 붙일거기때문에 METHOD로 지정
@Target(ElementType.METHOD)
//애노테이션을 사용한 코드를 언제까지 유지할건지 런타임때까지 유지
@Retention(RetentionPolicy.RUNTIME)
public @interface LogExecutionTime {

}
