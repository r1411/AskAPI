package me.r1411.askapi.repository;

import me.r1411.askapi.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Integer> {

}
