package com.rahul.connectionsservice.repo;

import com.rahul.connectionsservice.entity.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Optional;

public interface PersonRepo extends Neo4jRepository<Person, Long> {
    Optional<Person> findByUserId(Long userId);
    @Query("""
    MATCH (personA:Person)-[:CONNECTED_TO]-(personB:Person)
    WHERE personA.userId = $userId
    RETURN personB
""")
    List<Person> getFirstDegreeConnections(Long userId);

    @Query("""
        MATCH (personA:Person)-[:CONNECTED_TO]-(firstDegree:Person)-[:CONNECTED_TO]-(secondDegree:Person)
        WHERE personA.userId = $userId
        AND secondDegree <> personA
        AND NOT (personA)-[:CONNECTED_TO]-(secondDegree)
        RETURN DISTINCT secondDegree
    """)
    List<Person> getSecondDegreeConnections(Long userId);

    @Query("""
        MATCH (personA:Person)-[:CONNECTED_TO]-(firstDegree:Person)-[:CONNECTED_TO]-(secondDegree:Person)-[:CONNECTED_TO]-(thirdDegree:Person)
        WHERE personA.userId = $userId
        AND thirdDegree <> personA
        AND NOT (personA)-[:CONNECTED_TO]-(thirdDegree)
        AND NOT (personA)-[:CONNECTED_TO]-(:Person)-[:CONNECTED_TO]-(thirdDegree)
        RETURN DISTINCT thirdDegree
    """)
    List<Person> getThirdDegreeConnections(Long userId);

    // Check if request exists
    @Query("MATCH (p1:Person)-[r:REQUESTED_TO]->(p2:Person) " +
            "WHERE p1.userId = $senderId AND p2.userId = $receiverId " +
            "RETURN count(r) > 0")
    boolean connectionRequestExists(Long senderId, Long receiverId);

    // Check if already connected
    @Query("MATCH (p1:Person)-[:CONNECTED_TO]-(p2:Person) " +
            "WHERE p1.userId = $senderId AND p2.userId = $receiverId " +
            "RETURN count(r) > 0")
    boolean alreadyConnected(Long senderId, Long receiverId);

    // Add connection request
    @Query("MATCH (p1:Person), (p2:Person) " +
            "WHERE p1.userId = $senderId AND p2.userId = $receiverId " +
            "CREATE (p1)-[:REQUESTED_TO]->(p2)")
    void addConnectionRequest(Long senderId, Long receiverId);



    //accept and reject

    @Query("MATCH (sender:Person)-[req:REQUESTED_TO]->(receiver:Person) " +
            "WHERE sender.userId = $senderId AND receiver.userId = $receiverId " +
            "DELETE req " +
            "CREATE (sender)-[:CONNECTED_TO]->(receiver)")
    void acceptConnectionRequest(Long senderId, Long receiverId);

    // 5. Reject the connection request
    // Matches the pending request and simply deletes the relationship
    @Query("MATCH (sender:Person)-[req:REQUESTED_TO]->(receiver:Person) " +
            "WHERE sender.userId = $senderId AND receiver.userId = $receiverId " +
            "DELETE req")
    void rejectConnectionRequest(Long senderId, Long receiverId);
}
