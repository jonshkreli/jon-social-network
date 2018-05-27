package jonjon.logic;

import jonjon.dao.PersonDao;
import jonjon.entities.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * All logic is handled here
 * */
@Service
public class PersonServices {

    @Autowired
    PersonDao personDao;

    public Person addFollowing(Long personId, Long personFollowingId) throws Exception {
        if(personId.equals(personFollowingId)) throw new Exception("Can not follow itself");
       else if(personDao.checkIfFollowing(personId, personFollowingId)) {
            //System.out.println("person " + personId + " already follows person" + personFollowingId);
            throw new Exception("person " + personId + " already follows person" + personFollowingId);
        } else {
          return personDao.addFollowing(personId, personFollowingId);
        }
    }

    public void deleteAll() {
        personDao.deleteAll();
    }

}
