package io.papermc.sebxstt.helpers;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.CommandSourceStack;

import java.util.Objects;

public class ReturnCommand {
    // Comando simple: /name
    public static LiteralCommandNode<CommandSourceStack> create(
            String name,
            Command<CommandSourceStack> callback
    ) {
        return Commands.literal(name)
                .executes(callback)
                .build();
    }

    // Comando con un solo argumento: /name <arg>
    public static LiteralCommandNode<CommandSourceStack> createArg(
            String name,
            String argument,
            Command<CommandSourceStack> callback
    ) {
        return Commands.literal(name)
                .then(
                        Commands.argument(argument, StringArgumentType.greedyString())
                                .executes(callback)
                )
                .build();
    }

    @SafeVarargs
    public static LiteralCommandNode<CommandSourceStack> createNested(
            Command<CommandSourceStack> callback,
            String[] literals,
            ArgumentSpec... args
    ) {
        Objects.requireNonNull(literals);
        if (literals.length == 0) {
            throw new IllegalArgumentException("Debe tener al menos un literal");
        }
        var node = Commands.literal(literals[0]);
        for (int i = 1; i < literals.length; i++) {
            node = node.then(Commands.literal(literals[i]));
        }
        if (args.length > 0) {
            for (ArgumentSpec spec : args) {
                node = node.then(
                        Commands.argument(spec.name, spec.type)
                                .executes(callback)
                );
            }
        } else {
            node = node.executes(callback);
        }
        return node.build();
    }

    public static class ArgumentSpec {
        public final String name;
        public final ArgumentType<?> type;
        public ArgumentSpec(String name, ArgumentType<?> type) {
            this.name = name;
            this.type = type;
        }
    }
}
