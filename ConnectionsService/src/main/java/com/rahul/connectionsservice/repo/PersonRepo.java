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

}
