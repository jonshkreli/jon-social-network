package jonjon.services;

import jonjon.configurations.PersonPrivatesyType;
import jonjon.entities.Person;
import jonjon.repos.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * All logic is handled here
 * */
@Service
public class PersonServices {

    @Autowired
    PersonRepository personRepository;

    public Person addFollowing(Long personId, Long personFollowingId) throws Exception {
        if(personId.equals(personFollowingId)) throw new Exception("Can not follow itself");

        Person[] twoPersons = getTwoPersons(personId, personFollowingId);

        Person person = twoPersons[0];
        Person followingPerson = twoPersons[1];


        if(person.isFollowing(followingPerson)) {
            throw new Exception("person " + personId + " already follows person" + personFollowingId);
        } else {
            /*depending on privacy type send request or follow immediately */
            switch (person.getPersonPrivatesyType()) {
                case PUBLIC: person.addFollowing(followingPerson); break;
                case PRIVATE: person.addFollowRequestsSent(followingPerson); break;
                default: person.addFollowing(followingPerson); break;
            }

            personRepository.save(person);

            return person;
        }
    }

    public Person removeFollowing(Long personId, Long personFollowingId) throws Exception {
        if(personId.equals(personFollowingId)) throw new Exception("Can not follow itself");

        Person[] twoPersons = getTwoPersons(personId, personFollowingId);

        Person person = twoPersons[0];
        Person followingPerson = twoPersons[1];


        if(! person.isFollowing(followingPerson)) {
            throw new Exception("person " + personId + " is not following person" + personFollowingId);
        } else {
            person.removeFollowing(followingPerson);

            personRepository.save(person);

            return person;
        }
    }

    /**
     * Check if personId already follows personFollowingId
     * */
    public boolean checkIfFollowing(Long personId, Long personFollowingId) throws Exception {
        Person[] twoPersons = getTwoPersons(personId, personFollowingId);

        Person person = twoPersons[0];
        Person followingPerson = twoPersons[1];

        return person.isFollowing(followingPerson);
    }

    /**
     * Check if personId already has send follow request to personFollowingId
     * */
    public boolean checkIfhasSendFollowRequest(Long personId, Long personFollowingId) throws Exception {
        Person[] twoPersons = getTwoPersons(personId, personFollowingId);

        Person person = twoPersons[0];
        Person followingPerson = twoPersons[1];

        return person.hasSendFollowRequest(followingPerson);
    }

    /**
     * Check if personId already has received follow request from personFollowingId
     * */
    public boolean checkIfReceivedFollowRequest(Long personId, Long personFollowingId) throws Exception {
        Person[] twoPersons = getTwoPersons(personId, personFollowingId);

        Person person = twoPersons[0];
        Person followingPerson = twoPersons[1];

        return person.hasReceivedFollowRequest(followingPerson);
    }



    private Person[] getTwoPersons(Long actorId, Long actedId) throws Exception {
        Optional<Person> optionalPerson = personRepository.findById(actorId);
        if(!optionalPerson.isPresent()) throw new Exception("Can not find person with ID: " + actorId);

        Optional<Person> optionalPersonFollowing = personRepository.findById(actedId);
        if(!optionalPersonFollowing.isPresent()) throw new Exception("Can not find person with ID: " + actedId);

        Person actor = optionalPerson.get();
        Person acted = optionalPersonFollowing.get();

        return new Person[]{actor, acted};
    }

    private Person getOnePerson(Long id) throws Exception {
        Optional<Person> optionalPerson = personRepository.findById(id);
        if(!optionalPerson.isPresent()) throw new Exception("Can not find person with ID: " + id);
        return optionalPerson.get();
    }

    /**
     * @param id Id of person
     * @return a List of all followers
     * */
    public List<Person> getFollowers(Long id) throws Exception {
        List<Person> followers = new ArrayList<>();

        for (Person p : personRepository.findAll()) {
            if(p.isFollowing(getOnePerson(id))) followers.add(p);
        }

        return followers;
    }

    public void manageFollowRequest(Long receiverId, Long senderId, boolean acceptance) throws Exception {
        Person[] twoPersons = getTwoPersons(receiverId, senderId);

        Person receiverPerson = twoPersons[0];
        Person senderPerson = twoPersons[1];

        if(acceptance) { //if receiver has accepted following request
            receiverPerson.removeFollowRequestsReceived(senderPerson);

            senderPerson.removeFollowRequestsSent(receiverPerson);
            senderPerson.addFollowing(receiverPerson);
        } else {
            receiverPerson.removeFollowRequestsReceived(senderPerson);
            senderPerson.removeFollowRequestsSent(receiverPerson);
        }

        personRepository.save(receiverPerson);
        personRepository.save(senderPerson);


    }

    public void deleteAll() {
        personRepository.deleteAll();
    }

}
