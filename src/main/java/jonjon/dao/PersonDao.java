package jonjon.dao;

import jonjon.repos.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

@Repository
public class PersonDao {

    @Autowired
    PersonRepository personRepository;
}
