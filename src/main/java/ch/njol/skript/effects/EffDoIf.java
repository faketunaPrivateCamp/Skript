/**
 *   This file is part of Skript.
 *
 *  Skript is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Skript is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Skript.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 * Copyright 2011-2017 Peter Güttinger and contributors
 */
package ch.njol.skript.effects;

import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;

@Name("Do If")
@Description("Execute an effect if a condition is true.")
@Examples({"on join:",
		"\tgive a diamond to the player if the player has permission \"rank.vip\""})
@Since("INSERT VERSION")
public class EffDoIf extends Effect  {

	static {
		Skript.registerEffect(EffDoIf.class, "<.+> if <.+>");
	}

	@SuppressWarnings({"unchecked", "null"})
	private Effect effect;

	@SuppressWarnings({"unchecked", "null"})
	private Condition condition;

	@SuppressWarnings({"unchecked", "null"})
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		String eff = parseResult.regexes.get(0).group();
		String cond = parseResult.regexes.get(1).group();
		effect = Effect.parse(eff, "Can't understand this effect: " + eff);
		condition = Condition.parse(cond, "Can't understand this condition: " + cond);
		if (effect instanceof EffDoIf) {
			Skript.error("Do if effects may not be nested!");
			return false;
		}
		return effect != null && condition != null;
	}

	@Override
	protected void execute(Event event) {
		if (condition.check(event))
			effect.run(event);
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return effect.toString(event, debug) + " if " + condition.toString(event, debug);
	}

}
