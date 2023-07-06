package fr.rader.gertrude.commands.getters;

import fr.rader.gertrude.utils.ChannelUtils;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.internal.utils.Checks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Member;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Util class to get a {@link Function} from the given class.
 * This Function is then used to get a specific object from JDA's {@link OptionMapping}s
 */
public final class ClassToOptionGetter {

    private static final Map<Class<?>, Function<OptionMapping, Object>> functionMap = new HashMap<>();

    // private constructor, we don't want an instance of this class
    private ClassToOptionGetter() {
    }

    /**
     * Get a getter {@link Function} depending on the given class.
     * This Function is then used to get a specific object from JDA's {@link OptionMapping}s
     *
     * @param clazz     The class to get the getter for
     * @param option    The option to get the element from
     * @return          A {@link Function} if the class has a getter for it, {@code null} otherwise
     */
    @Nullable
    public static Object get(@NotNull final Class<?> clazz, @NotNull final OptionMapping option) {
        Checks.notNull(clazz, "clazz");

        if (Channel.class.isAssignableFrom(clazz)) {
            return ChannelUtils.getAsNormalChannel(option.getAsChannel());
        }

        Function<OptionMapping, Object> function = functionMap.get(clazz);
        if (function == null) {
            return null;
        }

        return function.apply(option);
    }

    static {
        // string type
        functionMap.put(String.class,         OptionMapping::getAsString);

        // numbers/boolean
        functionMap.put(Boolean.class,        OptionMapping::getAsBoolean);
        functionMap.put(boolean.class,        OptionMapping::getAsBoolean);
        functionMap.put(Long.class,           OptionMapping::getAsLong);
        functionMap.put(long.class,           OptionMapping::getAsLong);
        functionMap.put(Integer.class,        OptionMapping::getAsInt);
        functionMap.put(int.class,            OptionMapping::getAsInt);
        functionMap.put(Double.class,         OptionMapping::getAsDouble);
        functionMap.put(double.class,         OptionMapping::getAsDouble);

        // mentionables
        functionMap.put(IMentionable.class,   OptionMapping::getAsMentionable);
        functionMap.put(Member.class,         OptionMapping::getAsMember);
        functionMap.put(User.class,           OptionMapping::getAsUser);
        functionMap.put(Role.class,           OptionMapping::getAsRole);

        // channels
        functionMap.put(ChannelType.class,    OptionMapping::getChannelType);
    }
}
