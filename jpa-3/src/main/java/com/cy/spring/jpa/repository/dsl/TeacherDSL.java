package com.cy.spring.jpa.repository.dsl;

import com.cy.spring.jpa.model.QPerson;
import com.cy.spring.jpa.model.QTeacher;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;

/**
 * @author yun.chen
 * @create 2015-11-29 12:59
 */
public class TeacherDSL {
    private static final QTeacher Q_TEACHER = QTeacher.teacher;

    private static final QPerson Q_PERSON = QPerson.person;

    private TeacherDSL() {
        throw new AssertionError();
    }

    public static BooleanExpression isNameLike(String name) {
        return Q_TEACHER.name.startsWithIgnoreCase(name);
    }

    public static BooleanExpression isNameIn(String... name) {
        return Q_TEACHER.name.in(name);
    }

    public static Predicate getAgeBetween(int starAge, int endAge) {
        return Q_TEACHER.age.between(starAge,endAge);
    }

    //测试关联查询
    public static BooleanExpression isStudentAgeEq(int age) {
        return Q_TEACHER.studentSet.any().age.eq(age);
    }


    public static BooleanExpression isAgeDescNameAsc() {
        return Q_TEACHER.id.eq(Q_PERSON.id);

    }


}
