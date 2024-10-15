/**********************************************************
 * Obscuria Lithium (c) 2024, All Rights Reserved.        *
 **********************************************************/

package dev.obscuria.fragmentum.core.v1.common.text;

import com.google.common.collect.Lists;
import dev.obscuria.fragmentum.api.v1.common.text.TextWrapper;
import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public final class TextWrapperImpl implements TextWrapper
{
    private static final String BREAKER = "\n";
    private final String text;
    private final IntObjectMap<String> prefixByLine = new IntObjectHashMap<>();
    private final IntObjectMap<Style> styleByLine = new IntObjectHashMap<>();
    private Function<String, Component> fragment = Component::literal;
    private String defaultPrefix = "";
    private Style defaultStyle = Style.EMPTY;
    private int maxLength = 40;

    public TextWrapperImpl(Component component)
    {
        this(component.getString());
    }

    public TextWrapperImpl(String text)
    {
        this.text = text;
    }

    @Override
    public TextWrapper withMaxLength(int length)
    {
        this.maxLength = length;
        return this;
    }

    @Override
    public TextWrapper withPrefix(String prefix)
    {
        this.defaultPrefix = prefix;
        return this;
    }

    public TextWrapper withLinePrefix(int index, String prefix)
    {
        this.prefixByLine.put(index, prefix);
        return this;
    }

    public TextWrapper withStyle(Style style)
    {
        this.defaultStyle = style;
        return this;
    }

    @Override
    public TextWrapper withLineStyle(int index, Style style)
    {
        this.styleByLine.put(index, style);
        return this;
    }

    @Override
    public TextWrapper fragment(Function<String, Component> function)
    {
        this.fragment = function;
        return this;
    }

    @Override
    public List<? extends Component> build()
    {
        try
        {
            final var result = Lists.<Component>newArrayList();
            final var words = Lists.newArrayList(Arrays
                    .stream(this.text.replace(BREAKER, " #$# ").split(" "))
                    .filter(word -> !word.isBlank()).toList());

            while (!words.isEmpty())
            {
                var line = Component.literal(this.prefixByLine.getOrDefault(result.size(), this.defaultPrefix));
                final var firstWord = words.removeFirst();
                if (firstWord.equals("#$#"))
                {
                    result.add(line.withStyle(this.styleByLine.getOrDefault(result.size(), this.defaultStyle)));
                    continue;
                }
                line.append(fragment.apply(firstWord));
                while (!words.isEmpty())
                {
                    final var word = words.getFirst();
                    if (word.equals("#$#"))
                    {
                        words.removeFirst();
                        break;
                    }
                    final var finalWord = this.fragment.apply(word);
                    if (getLength(line.getString() + " " + finalWord.getString()) > this.maxLength) break;
                    line.append(" ").append(finalWord);
                    words.removeFirst();
                }
                result.add(line.withStyle(this.styleByLine.getOrDefault(result.size(), this.defaultStyle)));
            }

            return result;
        } catch (Exception ignored)
        {
            return Lists.newArrayList();
        }
    }

    public static int getLength(String string)
    {
        return ChatFormatting.stripFormatting(string).length();
    }
}
