package jonjon.entities;


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

	@Relationship(type = "FOLLOWS")
	private List<Person> following = new ArrayList<>();

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

	public List<Person> getFollowing() {
		return following;
	}

	public void setFollowing(List<Person> following) {
		this.following = following;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
