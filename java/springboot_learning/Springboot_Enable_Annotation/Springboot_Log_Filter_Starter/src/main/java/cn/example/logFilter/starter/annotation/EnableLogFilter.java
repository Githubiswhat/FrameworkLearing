package cn.example.logFilter.starter.annotation;

import cn.example.logFilter.starter.config.EnableLogFilterImportSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(EnableLogFilterImportSelector.class) //引入LogFilterAutoConfiguration配置类
public @interface EnableLogFilter {
}
