package com.military.comms.repository;

import com.military.comms.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByToUsername(String username);
    List<Message> findByFromUsername(String username);
}
