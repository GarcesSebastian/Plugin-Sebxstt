package io.papermc.sebxstt.functions.utils;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.CommandSourceStack;

public class ReturnCommand {
    public static LiteralCommandNode<CommandSourceStack> create(
            String name,
            Command<CommandSourceStack> callback
    ) {
        return Commands.literal(name)
                .executes(callback)
                .build();
    }
}
