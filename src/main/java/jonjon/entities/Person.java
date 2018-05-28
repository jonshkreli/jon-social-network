package jonjon.entities;


import jonjon.configurations.PersonPrivatesyType;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.ArrayList;
import java.util.List;

@NodeEntity
public class Person {

	@Id @GeneratedValue private Long id;

	private String firstName;
	private String lastName;

	private PersonPrivatesyType personPrivatesyType = PersonPrivatesyType.PUBLIC;

	@Relationship(type = "FOLLOWS")
	private List<Person> following = new ArrayList<>();

	@Relationship(type = "want to follow")
	private List<Person> followRequestsSent = new ArrayList<>();

	@Relationship(type = "follow requests")
	private List<Person> followRequestsReceived = new ArrayList<>();


	public Long getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/*Following list operations*/

	public List<Person> getFollowing() {
		return following;
	}

	public void addFollowing(Person person) {
		following.add(person);
	}

	public void removeFollowing(Person person) {
		following.remove(person);
	}

	public boolean isFollowing(Person person) {
		if(following.contains(person)) return true;
		else return false;
	}


	/*FollowRequestsSent list operations*/

	public List<Person> getFollowRequestsSent() {
		return followRequestsSent;
	}

	public void addFollowRequestsSent(Person person) {
		followRequestsSent.add(person);
	}

	public void removeFollowRequestsSent(Person person) {
		followRequestsSent.remove(person);
	}

	public boolean hasSendFollowRequest(Person person) {
		if(followRequestsSent.contains(person)) return true;
		else return false;
	}


	/*FollowRequestsReceived list operations*/

	public List<Person> getFollowRequestsReceived() {
		return followRequestsReceived;
	}

	public void addFollowRequestsReceived(Person person) {
		followRequestsReceived.add(person);
	}

	public void removeFollowRequestsReceived(Person person) {
		followRequestsReceived.remove(person);
	}

	public boolean hasReceivedFollowRequest(Person person) {
		if(followRequestsReceived.contains(person)) return true;
		else return false;
	}


	public PersonPrivatesyType getPersonPrivatesyType() {
		return personPrivatesyType;
	}

	public void setPersonPrivatesyType(PersonPrivatesyType personPrivatesyType) {
		this.personPrivatesyType = personPrivatesyType;
	}
}
