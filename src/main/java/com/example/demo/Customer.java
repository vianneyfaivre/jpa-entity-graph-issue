package com.example.demo;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@NamedEntityGraph(
    name = "testGraph",
    attributeNodes = {
        @NamedAttributeNode("nicknames"),
    }
)
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "CUSTOMER_NICKNAMES", joinColumns = @JoinColumn(name = "ID"))
    @Column(name = "NICKNAME")
    private Set<String> nicknames = new HashSet<>();

    protected Customer() {
    }

    public Customer(String firstName, String lastName, Set<String> nicknames) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nicknames = nicknames;
    }

    @Override
    public String toString() {
        return String.format(
                "Customer[id=%d, firstName='%s', lastName='%s']",
                id, firstName, lastName);
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Set<String> getNicknames() {
        return nicknames;
    }
}
