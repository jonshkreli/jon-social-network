package jonjon.controllers;

import jonjon.entities.Person;
import jonjon.services.PersonServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
@RequestMapping("/people/{id}")
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
    public ResponseEntity addFollowing(@PathVariable String id, @RequestBody String followPersonId) {
        try {
            Long personId = Long.parseLong(id);
            Long followId = Long.parseLong(followPersonId);
            Person person = personServices.addFollowing(personId, followId);

            if(person != null) {
                //Link selfLink = linkTo(PersonController.class).slash(person.getId()).withSelfRel();
                Resources<Person> result = new Resources<Person>(person.getFollowing());
                return new ResponseEntity(result, HttpStatus.CREATED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @PatchMapping("/removeFollowing")
    @ResponseBody
    public ResponseEntity removeFollowing(@PathVariable String id, @RequestBody String followPersonId) {
        try {
            Long personId = Long.parseLong(id);
            Long followId = Long.parseLong(followPersonId);
            Person person = personServices.removeFollowing(personId, followId);

            if(person != null) {
               // Link selfLink = linkTo(PersonController.class).slash(person.getId()).withSelfRel();
                Resources<Person> result = new Resources<Person>(person.getFollowing());
                return new ResponseEntity(result, HttpStatus.CREATED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/getFollowers")
    @ResponseBody
    public ResponseEntity getFollowers(@PathVariable String id) {
        try {
            Long personId = Long.parseLong(id);
                Resources<Person> result = new Resources<Person>(personServices.getFollowers(personId));
                return new ResponseEntity(result, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/manageFollowRequest", produces = "text/plain")
    @ResponseBody
    ResponseEntity manageFollowRequest(
            @PathVariable String receiverId,
            @RequestParam("to") String personSenderRequestId,
            @RequestParam("acceptance") String acceptance
            ) {
        try {
            Long id = Long.parseLong(receiverId);
            Long senderRequestId = Long.parseLong(personSenderRequestId);

            boolean acceptanceResult = false;

            if(acceptance.equalsIgnoreCase("true") || acceptance.equals("1") ||
                    acceptance.equalsIgnoreCase("yes"))
                acceptanceResult = true;

            personServices.manageFollowRequest(id, senderRequestId, acceptanceResult);

            String message = "";
            if(acceptanceResult) message = "follow request accepted";
            else message = "follow request ignored";

            return new ResponseEntity(message, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping("/deleteAll")
    void deleteAll() {
        personServices.deleteAll();
    }

}
