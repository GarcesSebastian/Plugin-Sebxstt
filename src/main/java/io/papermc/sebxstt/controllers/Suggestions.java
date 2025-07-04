package io.papermc.sebxstt.controllers;

import com.mojang.brigadier.suggestion.SuggestionProvider;
import io.papermc.paper.command.brigadier.CommandSourceStack;

import java.util.List;

public class Suggestions {
    public static SuggestionProvider<CommandSourceStack> fromList(List<String> options) {
        return (ctx, builder) -> {
            String remaining = builder.getRemainingLowerCase();
            for (String opt : options) {
                if (remaining.isEmpty() || opt.toLowerCase().startsWith(remaining)) {
                    builder.suggest(opt);
                }
            }
            return builder.buildFuture();
        };
    }
}
