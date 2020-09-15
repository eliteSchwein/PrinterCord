package de.eliteschw31n.utils;

import net.dv8tion.jda.api.entities.MessageReaction;

import java.util.List;

public class SetupStep {
    final private String stepName;
    final private String stepDescription;
    final private List<MessageReaction.ReactionEmote> emotes;
    private Object value;

    public SetupStep(final String stepName, final String stepDescription, final List emotes) {
        this.stepName = stepName;
        this.stepDescription = stepDescription;
        this.emotes = emotes;
    }

    public String getStepName() {
        return stepName;
    }

    public String getStepDescription() {
        return stepDescription;
    }

    public List<MessageReaction.ReactionEmote> getEmotes() {
        return emotes;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
