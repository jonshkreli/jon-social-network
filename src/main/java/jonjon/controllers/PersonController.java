package jonjon.controllers;

import jonjon.entities.Person;
import jonjon.logic.PersonServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/person/{id}")
public class PersonController {

    @Autowired
    private PersonServices personServices;

    @RequestMapping("/index")
    String index(){
        //mapped to hostname:port/home/index/
        return "Hello from index";
    }

    @PatchMapping("/addFollowing")
    @ResponseBody
    ResponseEntity addFollowing(@PathVariable String id, @RequestBody String followPersonId) {
        try {
            Long personId = Long.parseLong(id);
            Long followId = Long.parseLong(followPersonId);
            Person person = personServices.addFollowing(personId, followId);

            if(person != null) return ResponseEntity.ok(person.getFollowing());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/deleteAll")
    void deleteAll() {
        personServices.deleteAll();
    }

}
