package org.springframework.samples.petclinic.owner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.aspect.LogExecutionTime;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleController {

    @Autowired
    String freelife;

    @LogExecutionTime
    @GetMapping("/context")
    public String context() {
        return "hello " + freelife;
    }
}
