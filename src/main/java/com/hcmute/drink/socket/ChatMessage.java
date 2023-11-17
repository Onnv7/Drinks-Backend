package com.hcmute.drink.socket;

import lombok.Builder;
import lombok.Data;
import org.modelmapper.internal.bytebuddy.implementation.bind.annotation.BindingPriority;

@Data
@Builder

public class ChatMessage {
    private String content;
    private String sender;
}
