package com.server.bookstoremanager.user.util;

import com.server.bookstoremanager.user.dto.MessageDTO;
import com.server.bookstoremanager.user.entity.User;

public class MessageUtils {

    public static MessageDTO creationMessage(User createdUser) {
        return returnMessage(createdUser, "created");
    }

    public static MessageDTO updatedMessage(User updatedUser) {
        return returnMessage(updatedUser, "updated");
    }

    private static MessageDTO returnMessage(User user, String action) {
        String username = user.getUsername();
        Long id = user.getId();
        String updatedUserMessage = String.format("User %s with id %s successfully %s", username, id, action);
        return MessageDTO.builder()
                .message(updatedUserMessage)
                .build();
    }
}
