package com.murillons.login_auth.repositories;

import com.murillons.login_auth.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSaveUser() {
        User user = new User();
        user.setEmail("teste@example.com");
        user.setPassword("password123");
        User savedUser = userRepository.save(user);

        assertNotNull(savedUser.getId(), "O ID do usuário salvo deve ser gerado.");
        assertEquals("teste@example.com", savedUser.getEmail(), "O email salvo deve corresponder ao esperado.");
        assertEquals("password123", savedUser.getPassword(), "A senha salva deve corresponder ao esperado.");
    }

    @Test
    public void testUniqueEmailConstraint() {
        User user1 = new User();
        user1.setEmail("unique@example.com");
        user1.setPassword("password123");
        userRepository.save(user1);

        User user2 = new User();
        user2.setEmail("unique@example.com");
        user2.setPassword("password456");

        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.saveAndFlush(user2);
        }, "Salvar um usuário com o mesmo email deve lançar uma exceção.");
    }
}