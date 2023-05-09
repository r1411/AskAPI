package me.r1411.askapi.repository;

import me.r1411.askapi.model.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    Page<Question> findByBoardId(int id, Pageable pageable);
}
