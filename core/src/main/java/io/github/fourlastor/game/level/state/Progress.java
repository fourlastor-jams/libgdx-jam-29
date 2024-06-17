package io.github.fourlastor.game.level.state;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Progress {
    public abstract Builder builder();

    public abstract float total();

    public abstract float art();

    public abstract float tech();

    public abstract float story();

    public abstract float mech();

    public float artProgress() {
        return art() / allProgresses();
    }

    public float techProgress() {
        return tech() / allProgresses();
    }

    public float storyProgress() {
        return story() / allProgresses();
    }

    public float mechProgress() {
        return mech() / allProgresses();
    }

    private float allProgresses() {
        return art() + tech() + story() + mech();
    }

    public static Progress initial() {
        return new AutoValue_Progress.Builder()
                .total(0)
                .art(0)
                .tech(0)
                .story(0)
                .mech(0)
                .build();
    }

    public enum Type {
        ART("Art"),
        TECH("Tech"),
        STORY("Story"),
        MECH("Mech");

        public final String displayName;

        Type(String displayName) {
            this.displayName = displayName;
        }
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Progress.Builder total(float value);

        public abstract Progress.Builder art(float value);

        public abstract Progress.Builder tech(float value);

        public abstract Progress.Builder story(float value);

        public abstract Progress.Builder mech(float value);

        public abstract Progress build();
    }
}
