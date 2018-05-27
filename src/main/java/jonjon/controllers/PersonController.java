package jonjon.controllers;

import jonjon.dao.PersonDao;
import jonjon.entities.Person;
import jonjon.repos.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/person/{id}")
public class PersonController {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonDao personDao;

    @RequestMapping("/index")
    String index(){
        //mapped to hostname:port/home/index/
        return "Hello from index";
    }

    @PatchMapping("/addFollowing")
    List<Person> addFollowing(@PathVariable String id, @RequestBody String FollowPersonId) {
        Optional<Person> optionalPerson = personRepository.findById(Long.parseLong(id));
        System.out.println("addFollowing");
        if (optionalPerson.isPresent()) {
            Person person = optionalPerson.get();

            Optional<Person> optionalFollowPerson = personRepository.findById(Long.parseLong(FollowPersonId));
            if (optionalFollowPerson.isPresent()) {
                Person FollowPerson = optionalFollowPerson.get();

                person.addFollowing(FollowPerson);
                personRepository.save(person);
                return person.getFollowing();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

}
