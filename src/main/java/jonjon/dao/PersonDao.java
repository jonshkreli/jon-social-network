package jonjon.dao;

import jonjon.entities.Person;
import jonjon.repos.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * All additional crud operations here
 * */
@Repository
public class PersonDao {

    @Autowired
    PersonRepository personRepository;

    public Person addFollowing(Long personId, Long personFollowingId) throws Exception {
            Optional<Person> optionalPerson = personRepository.findById(personId);
            if(!optionalPerson.isPresent()) throw new Exception("Can not find person with ID: " + personId);

            Optional<Person> optionalPersonFollowing = personRepository.findById(personFollowingId);
            if(!optionalPersonFollowing.isPresent()) throw new Exception("Can not find person with ID: " + personFollowingId);

            Person person = optionalPerson.get();
            Person followingPerson = optionalPersonFollowing.get();

            person.addFollowing(followingPerson);

            personRepository.save(person);

            return person;
    }

    /**
     * Check if personId already follows personFollowingId
     * */
    public boolean checkIfFollowing(Long personId, Long personFollowingId) throws Exception {
            Optional<Person> optionalPerson = personRepository.findById(personId);
            if(!optionalPerson.isPresent()) throw new Exception("Can not find person with ID: " + personId);

            Optional<Person> optionalPersonFollowing = personRepository.findById(personFollowingId);
            if(!optionalPersonFollowing.isPresent()) throw new Exception("Can not find person with ID: " + personFollowingId);

            Person person = optionalPerson.get();
            Person followingPerson = optionalPersonFollowing.get();

            return person.isFollowing(followingPerson);
    }

    public void deleteAll() {
        personRepository.deleteAll();
    }

}
