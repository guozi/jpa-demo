package com.cy.spring.jpa.test;

import com.cy.spring.jpa.model.Person;
import com.cy.spring.jpa.model.QTeacher;
import com.cy.spring.jpa.model.Student;
import com.cy.spring.jpa.model.Teacher;
import com.cy.spring.jpa.repository.PersonRepository;
import com.cy.spring.jpa.repository.StudentRepository;
import com.cy.spring.jpa.repository.TeacherRepository;
import com.cy.spring.jpa.repository.dsl.TeacherDSL;
import com.cy.spring.jpa.service.PersonService;
import com.google.common.collect.Lists;
import com.mysema.query.types.OrderSpecifier;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;


import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;


/**
 * @author yun.chen
 * @create 2015-11-22 20:04
 */
public class JpaTest {

    private ApplicationContext applicationContext = null;

    private PersonService personService;

    PersonRepository personRepository = null;

    TeacherRepository teacherRepository = null;

    StudentRepository studentRepository = null;

    {
        applicationContext = new ClassPathXmlApplicationContext("spring-config.xml");
        personService = applicationContext.getBean(PersonService.class);
        personRepository = applicationContext.getBean(PersonRepository.class);
        teacherRepository = applicationContext.getBean(TeacherRepository.class);
        studentRepository = applicationContext.getBean(StudentRepository.class);
    }

    @Test
    public void testPersonSave() {
        Person person1 = new Person();
        person1.setEmail("A1@sohu.com");
        person1.setAge(20);
        person1.setLastName("A1");

        Person person2 = new Person();
        person2.setEmail("B1@sohu.com");
        person2.setAge(23);
        person2.setLastName("B1");

        personService.save(person1, person2);
    }

    @Test
    public void testDataSource() throws SQLException {
        DataSource dataSource = applicationContext.getBean(DataSource.class);
        System.out.println(dataSource.getConnection());
    }

    @Test
    public void testPersonRepository() {
        Person person = personRepository.findByLastName("A1");
        System.out.println("person===>>>" + person);

        List<Person> personList = personRepository.getByLastNameStartingWithAndIdLessThan("A", 3);
        System.out.println("personList===>>>" + personList);
    }

    @Test
    public void testKeyWord() {
        List<Person> personList = personRepository.getByAddressIdGreaterThan(1);
    }

    @Test
    public void testGetMaxAgePerson() {
        Person person = personRepository.getMaxAgePerson();
        System.out.println(person);
    }

    @Test
    public void testQueryAnnotationParam1() {
        List<Person> personList = personRepository.testQueryAnnotationParam1("AA", "AA@sohu.com");
        System.out.println(personList);
    }

    @Test
    public void testQueryAnnotationParam2() {
        List<Person> personList = personRepository.testQueryAnnotationParam2("AA", "AA@sohu.com");
        System.out.println(personList);
    }

    @Test
    public void testQueryAnnotationLikeParam1() {
        List<Person> personList = personRepository.testQueryAnnotationLikeParam1("A%", "AA");
        System.out.println(personList);
    }

    @Test
    public void testGetTotalCount() {
        long totalCount = personRepository.getTotalCount();
        System.out.println(totalCount);
    }

    @Test
    public void testUpdateEmailById() {
        personService.updateEmailById("xx@google.com", 1);
    }

    @Test
    public void testSavePersons() {
        List<Person> personList = Lists.newArrayList();
        for (int i = 'a'; i < 'z'; i++) {
            Person person = new Person();
            person.setAge(20);
            person.setLastName((char) i + "" + (char) i);
            person.setEmail((char) i + "" + (char) i + "@google.com");
            person.setBirth(new Date());

            personList.add(person);
        }
        personService.savePersons(personList);
    }

    @Test
    public void testPagingAndSortingRepository() {
        //pageNo 从0开始
        int pageNo = 3;
        int pageSize = 5;
        //排序
        Order order = new Order(Sort.Direction.ASC, "id");
        Sort sort = new Sort(order);
        //分页
        Pageable pageable = new PageRequest(pageNo - 1, pageSize, sort);
        Page<Person> page = personRepository.findAll(pageable);

        System.out.println("总记录数:" + page.getTotalElements());
        System.out.println("当前第几页: " + (page.getNumber() + 1));
        System.out.println("总页数: " + page.getTotalPages());
        System.out.println("当前页面的List: " + page.getContent());
        System.out.println("当前页面的记录: " + page.getNumberOfElements());
    }

    //测试实现带查询条件的分页（id>5）
    @Test
    public void testJpaSpecificationExecutor() {
        //pageNo 从0开始
        int pageNo = 3;
        int pageSize = 5;

        //分页
        Pageable pageable = new PageRequest(pageNo - 1, pageSize);
        //通常使用 Specification 的匿名内部类
        Specification<Person> specification = new Specification<Person>() {
            /**
             * @param *root: 代表查询的实体类.
             * @param query: 可以从中可到 Root 对象, 即告知 JPA Criteria 查询要查询哪一个实体类. 还可以
             * 来添加查询条件, 还可以结合 EntityManager 对象得到最终查询的 TypedQuery 对象.
             * @param *cb: CriteriaBuilder 对象. 用于创建 Criteria 相关对象的工厂. 当然可以从中获取到 Predicate 对象
             * @return: *Predicate 类型, 代表一个查询条件.
             */
            @Override
            public Predicate toPredicate(Root<Person> root,
                                         CriteriaQuery<?> query, CriteriaBuilder cb) {
                Path path = root.get("id");
                Predicate predicate = cb.gt(path, 5);
                return predicate;
            }
        };

        Page<Person> page = personRepository.findAll(specification, pageable);

        System.out.println("总记录数:" + page.getTotalElements());
        System.out.println("当前第几页: " + (page.getNumber() + 1));
        System.out.println("总页数: " + page.getTotalPages());
        System.out.println("当前页面的List: " + page.getContent());
        System.out.println("当前页面的记录: " + page.getNumberOfElements());
    }

    @Test
    public void testCustomRepository1() {
        Person person = personRepository.test(1);
        System.out.println(person);
    }

    @Test
    public void testCustomRepository2() {
        try {
            personRepository.reindex();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<Person> person = personRepository.search("cc");
        System.out.println(person);
    }

    @Test
    public void dataInit() {
        List<Teacher> teachers = Lists.newArrayList();
        List<Student> students = Lists.newArrayList();
        Teacher t1 = new Teacher();
        t1.setName("abc");
        t1.setAge(40);

        Teacher t2 = new Teacher();
        t2.setName("abcd");
        t2.setAge(38);

        Teacher t3 = new Teacher();
        t3.setName("bcd");
        t3.setAge(43);

        Student s1 = new Student();
        s1.setName("aa");
        s1.setAge(20);
        s1.setBirthDay(new Date());

        Student s2 = new Student();
        s2.setName("bb");
        s2.setAge(25);
        s2.setBirthDay(new Date());

        s1.getTeacherSet().add(t1);
        s1.getTeacherSet().add(t2);

        s2.getTeacherSet().add(t2);
        s2.getTeacherSet().add(t3);

        t1.getStudentSet().add(s1);
        t1.getStudentSet().add(s2);

        t2.getStudentSet().add(s1);
        t2.getStudentSet().add(s2);

        t3.getStudentSet().add(s1);

        students.add(s1);
        students.add(s2);
        teachers.add(t1);
        teachers.add(t2);
        teachers.add(t3);

        studentRepository.save(students);
        teacherRepository.save(teachers);

    }

    @Test
    public void testQueryDSL() {
//        List<Teacher> teachers = teacherRepository.findAll(TeacherDSL.isNameLike("abc"));
//        System.out.println("Teacher List: " + teachers);

//        List<Teacher> teachers = teacherRepository.findAll(TeacherDSL.isNameIn("abc","bcd"));
//        System.out.println("Teacher List: " + teachers);

//        List<Teacher> teachers = teacherRepository.findAll(TeacherDSL.getAgeBetween(40,50));
//        System.out.println("Teacher List: " + teachers);

//        List<Teacher> teachers = teacherRepository.findAll(TeacherDSL.isStudentAgeEq(20));
//        System.out.println("Teacher List: " + teachers);

        //测试排序
        OrderSpecifier<String> nameOrder = QTeacher.teacher.name.desc();
        OrderSpecifier<Integer> ageDes = QTeacher.teacher.age.asc();
        List<Teacher> teachers = teacherRepository.findAll(nameOrder,ageDes);
        System.out.println("Teacher List: " + teachers);
    }
}
