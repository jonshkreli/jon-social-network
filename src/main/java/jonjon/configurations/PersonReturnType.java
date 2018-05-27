package jonjon.configurations;

import jonjon.entities.Person;

import java.util.List;

public enum PersonReturnType {
    PERSON, FOLLOWING_LIST, VOID;

    List<Person> returnPerson(PersonReturnType personReturnType, Person person) {
        if(this == FOLLOWING_LIST) return person.getFollowing();
        else return null;
    }


}
