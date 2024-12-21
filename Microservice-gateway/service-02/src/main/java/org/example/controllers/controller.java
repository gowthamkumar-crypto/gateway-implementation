package org.example.controllers;

import org.example.entity.Cources;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Stream;

@RestController
public class controller {

    @GetMapping("/cources")
    public List<Cources> getStudents(){
        return Stream.of(new Cources(1,"C-1"),
                new Cources(2,"C-2"),
                new Cources(3,"C-3")).toList();
    }
}
