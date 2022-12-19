package ru.yandex.practicum.filmrate.daoTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmrate.dao.FriendStorage;
import ru.yandex.practicum.filmrate.dao.UserStorage;
import ru.yandex.practicum.filmrate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FriendStorageTest {
    private final FriendStorage friendsStorage;
    private final UserStorage userStorage;

    @Test
    void addFriend() {
        User user = User.builder()
                .id(1)
                .email("nick@email.com")
                .login("nick")
                .name("name")
                .birthday(LocalDate.of(1990, 10, 4))
                .build();
        userStorage.addUser(user);

        User user2 = User.builder()
                .id(2)
                .email("nick2@email.com")
                .login("nick2")
                .name("name2")
                .birthday(LocalDate.of(1994, 10, 4))
                .build();
        userStorage.addUser(user2);

        User user3 = User.builder()
                .id(3)
                .email("nick3@email.com")
                .login("nick3")
                .name("name3")
                .birthday(LocalDate.of(1993, 10, 4))
                .build();
        userStorage.addUser(user3);
        friendsStorage.addFriend(1,2);
        friendsStorage.addFriend(1,3);
        assertEquals(2, friendsStorage.getFriends(1).size());
    }

    @Test
    void deleteFriend() {
        User user = User.builder()
                .id(1)
                .email("nick@email.com")
                .login("nick")
                .name("name")
                .birthday(LocalDate.of(1990, 10, 4))
                .build();
        userStorage.addUser(user);

        User user2 = User.builder()
                .id(2)
                .email("nick2@email.com")
                .login("nick2")
                .name("name2")
                .birthday(LocalDate.of(1994, 10, 4))
                .build();
        userStorage.addUser(user2);

        User user3 = User.builder()
                .id(3)
                .email("nick3@email.com")
                .login("nick3")
                .name("name3")
                .birthday(LocalDate.of(1993, 10, 4))
                .build();
        userStorage.addUser(user3);
        friendsStorage.addFriend(1,3);
        friendsStorage.addFriend(1,2);
        friendsStorage.deleteFriend(1,3);
        assertEquals(1, friendsStorage.getFriends(1).size());
    }
}
