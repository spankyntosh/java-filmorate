package ru.yandex.practicum.filmorate.dao.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FriendshipDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class FriendshipDAOImpl implements FriendshipDAO {

    private final JdbcTemplate jdbcTemplate;


    @Override
    public void addFriend(Integer userWhoSendId, Integer userWhomSendId) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate).withTableName("friendships");
        Map<String, Object> map = new HashMap<>();
        map.put("user_id_who_send", userWhoSendId);
        map.put("user_id_to_whom_send", userWhomSendId);

        if (isFriendingUserFriendOfCurrentUser(userWhomSendId, userWhoSendId)) {
            map.put("status", "confirmed");
            insert.execute(map);
            changeFriendshipStatus(userWhomSendId, userWhoSendId, "confirmed");
        } else {
            map.put("status", "not confirmed");
            insert.execute(map);
        }
    }

    @Override
    public void excludeFromFriends(Integer currentUserId, Integer excludingFriendId) {
        String statement = "DELETE "
                + "FROM friendships "
                + "WHERE user_id_who_send = ? AND user_id_to_whom_send = ?";

        jdbcTemplate.update(statement, currentUserId, excludingFriendId);
        changeFriendshipStatus(excludingFriendId, currentUserId, "not confirmed");
    }

    /**
     * Проверка того что текущая заявка в друзья уже производилась ранее
     *
     * @param userWhoSendId  пользователь, который хочет добавить другого человека в друзья
     * @param userWhomSendId пользователь которого хотят добавить в друзья
     * @return true or false
     */
    @Override
    public boolean isUserAlreadyInFriends(Integer userWhoSendId, Integer userWhomSendId) {
        String statement = "SELECT * "
                + "FROM friendships "
                + "WHERE user_id_who_send = ? AND user_id_to_whom_send = ?";

        ResultSetExtractor<List<Integer>> extractor = rs -> {
            List<Integer> list = new ArrayList<>();
            while (rs.next()) {
                list.add(rs.getInt("user_id_who_send"));
            }
            return list;
        };
        List<Integer> idList = jdbcTemplate.query(statement, extractor, userWhoSendId, userWhomSendId);
        return !idList.isEmpty();
    }

    /**
     * Проверка, что пользователь которого хотят добавить в друзья ранее сам подавал такую заявку по отношению к текущему
     * пользователю
     *
     * @param userWhoSendId  текущий пользователь который подаёт заявку в друзья
     * @param userWhomSendId пользователь которого хотят добавить в друзья
     * @return true or false
     */
    private boolean isFriendingUserFriendOfCurrentUser(Integer userWhomSendId, Integer userWhoSendId) {
        return isUserAlreadyInFriends(userWhomSendId, userWhoSendId);
    }

    private void changeFriendshipStatus(Integer user1, Integer user2, String status) {
        String statement = "UPDATE friendships "
                + "SET status  = ? "
                + "WHERE user_id_who_send = ? AND user_id_to_whom_send = ?";

        jdbcTemplate.update(statement, status, user1, user2);
    }
}
