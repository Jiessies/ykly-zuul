package com.ykly.zuul;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Jdk8 {
    public static void main(String[] args) {

        List<Student> list = new ArrayList<>();
        list.add(new Student(33L, "aaa", "chuangye"));
        list.add(new Student(25L, "bbb", "chuangye1"));
        list.add(new Student(9L, "ccc", "chuangye1"));
        list.add(new Student(11L, "ddd", "chuangye1"));

        Map<String, Optional<Student>> allMapTask = list.stream().collect(
                Collectors.groupingBy(Student::getAddress, Collectors.maxBy((o1, o2) -> o1.getAge().compareTo(o2.getAge()))));

        // 遍历获取对象信息
        for (Map.Entry<String,Optional<Student>> entry: allMapTask.entrySet()) {
            Student student = entry.getValue().get();
            System.out.println(JSON.toJSONString(student));
        }

        list.forEach(student -> {
            System.out.println(JSON.toJSONString(student));
        });

        list.stream().forEach(student -> {
            System.out.println(student.getAddress());
        });

        List<Student> resultList = list.stream().filter(student -> !student.getName().equals("ddd")).sorted(Comparator.comparing(Student::getAge)).collect(Collectors.toList());
        resultList.stream().forEach(student -> {
            System.out.println(JSON.toJSONString(student));
        });

        System.out.println("---------------------");
        Map<String,List<Student>> stringStudentMap = list.stream().collect(Collectors.groupingBy(Student::getAddress));
        stringStudentMap.entrySet().stream().forEach(stringListEntry -> {
            System.out.println(JSON.toJSONString(stringListEntry));
        });


        Map<String, Student> stringStudentMap1 = list.stream().collect(Collectors.toMap(Student::getName, e -> e, (k1,k2) -> k1));
        stringStudentMap1.entrySet().stream().forEach(stringStudentEntry -> {
            System.out.println(JSON.toJSONString(stringStudentEntry));
        });

        List<Long> integerList =  list.stream().map(Student::getAge).collect(Collectors.toList());
        integerList.stream().forEach(integer -> {
            System.out.println(integer);
        });

        Long integer = list.stream().map(Student::getAge).reduce(0L,Long::max);
        System.out.println(integer);




        Map<String, Student> studentMap = new HashMap<>();
        studentMap.put("aaa", new Student(33L, "aaa", "chuangye1"));
        studentMap.put("bbb", new Student(25L, "bbb", "chuangye1"));
        studentMap.put("ccc", new Student(9L, "ccc", "chuangye"));
        studentMap.put("ddd", new Student(11L, "ddd", "chuangye"));

        studentMap.entrySet().stream().forEach(stringStudentEntry -> {
            System.out.println(JSON.toJSONString(stringStudentEntry));
        });

        Stream<Map.Entry<String, Student>> stream = studentMap.entrySet().stream().filter(stringStudentEntry -> stringStudentEntry.getValue().getAge() > 11).sorted(Comparator.comparing(stringStudentEntry -> stringStudentEntry.getValue().getAge()));
        Map<String, Student> resultMap = stream.collect(Collectors.toMap((e) -> (String) e.getKey(),
                (e) -> e.getValue()));

        System.out.println(JSON.toJSONString(resultMap));
    }
}


@Data
@NoArgsConstructor
@AllArgsConstructor
class Student{
    private Long age;
    private String name;
    private String address;
}

