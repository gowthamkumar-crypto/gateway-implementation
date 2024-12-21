package org.example.controllers;

import org.example.entity.Students;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Stream;

@RestController
public class controller {

    @GetMapping("/students")
    public List<Students> getStudents(){
        return Stream.of(new Students(1,"A"),
                new Students(2,"B"),
                new Students(3,"C")).toList();
    }
}
