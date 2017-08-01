package com.flchy.blog.base.datasource.annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * ������ע��,������ʾmybatis������Դ,����ע�ⶨ����ʹ���ߵĽӿ���, �ӿ�������з�������ʹ��spring�����õ�base����Դ, Ŀ����Ϊ��֧�� ��Ŀ������Դ�Ĺ���,�ڽӿ���������@BaseRepository��
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.TYPE, java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.PARAMETER })
public @interface BaseRepository {

}
