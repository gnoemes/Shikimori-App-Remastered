package com.gnoemes.shikimori.entity.common.domain;

import androidx.annotation.NonNull;

public class SearchConstants {

    public static final String ADVANCED = "advanced";
    public static final String GENRE = "genre";
    public static final String TYPE = "kind";
    public static final String STATUS = "status";

    ////////////////////////////////////////////////////////////////////
    // Queries
    ////////////////////////////////////////////////////////////////////

    public static final String SEARCH = "search";
    public static final String PAGE = "page";
    public static final String LIMIT = "limit";
    public static final String DURATION = "duration";
    public static final String RATE = "mylist";
    public static final String SCORE = "score";
    public static final String SEASON = "season";
    public static final String ORDER = "order";
    public static final String IDS = "ids";
    public static final String CENSORED = "censored";
    public static final String AGE_RATING = "rating";
    public static final String STUDIO = "studio";

    ////////////////////////////////////////////////////////////////////
    // Values
    ////////////////////////////////////////////////////////////////////

//    public static final String TV_13 = "tv_13";
//    public static final String TV_24 = "tv_24";
//    public static final String TV_48 = "tv_48";
//    public static final String DURATION_LESS_THAN_10 = "S";
//    public static final String DURATION_LESS_THAN_30 = "D";
//    public static final String DURATION_MORE_THAN_30 = "F";
//    public static final String SUMMER = "summer_";
//    public static final String WINTER = "winter_";
//    public static final String SPRING = "spring_";
//    public static final String AUTUMN = "fall_";

    ////////////////////////////////////////////////////////////////////
    // ID values for genres
    ////////////////////////////////////////////////////////////////////

//    public static final int DRAMA = 8;
//    public static final int GAME = 11;
//    public static final int PSYCHOLOGICAL = 40;
//    public static final int ADVENTURE = 2;
//    public static final int MUSIC = 19;
//    public static final int ACTION = 1;
//    public static final int COMEDY = 4;
//    public static final int DEMONS = 6;
//    public static final int POLICE = 39;
//    public static final int SPACE = 29;
//    public static final int ECCHI = 9;
//    public static final int FANTASY = 10;
//    public static final int HENTAI = 12;
//    public static final int HISTORICAL = 13;
//    public static final int HORROR = 14;
//    public static final int MAGIC = 16;
//    public static final int MECHA = 18;
//    public static final int PARODY = 20;
//    public static final int SAMURAI = 21;
//    public static final int ROMANCE = 22;
//    public static final int SCHOOL = 23;
//    public static final int SHOUJO = 25;
//    public static final int SHOUNEN = 27;
//    public static final int SHOUNEN_AI = 28;
//    public static final int SPORTS = 30;
//    public static final int VAMPIRE = 32;
//    public static final int YAOI = 33;
//    public static final int YURI = 34;
//    public static final int HAREM = 35;
//    public static final int SLICE_OF_LIFE = 36;
//    public static final int SEINEN = 42;
//    public static final int JOSEI = 43;
//    public static final int SUPERNATURAL = 37;
//    public static final int THRILLER = 41;
//    public static final int SHOUJO_AI = 26;
//    public static final int SCI_FI = 24;
//    public static final int SUPER_POWER = 31;
//    public static final int MILITARY = 38;
//    public static final int MYSTERY = 7;
//    public static final int KIDS = 15;
//    public static final int CARS = 3;
//    public static final int MARTIAL_ARTS = 17;
//    public static final int DEMENTIA = 5;


    public enum ORDER_BY {
        ORDER("order"),
        ID("id"),
        RANKED("ranked"),
        TYPE("kind"),
        POPULARITY("popularity"),
        NAME("name"),
        AIRED_ON("aired_on"),
        EPISODES("episodes"),
        VOLUMES("volumes"),
        CHAPTERS("chapters"),
        STATUS("status"),
        RANDOM("random"),;

        private final String orderBy;

        ORDER_BY(String orderBy) {
            this.orderBy = orderBy;
        }

        public boolean equalsType(String otherType) {
            return orderBy.equals(otherType);
        }

        @NonNull
        @Override
        public String toString() {
            return this.orderBy;
        }
    }

    public enum SEASONS {
        FALL("fall"),
        WINTER("winter"),
        SPRING("spring"),
        SUMMER("summer");

        private final String season;

        SEASONS(String season) {
            this.season = season;
        }

        public boolean equalsSeason(String otherSeason) {
            return season.equals(otherSeason);
        }

        @NonNull
        @Override
        public String toString() {
            return season;
        }
    }

    public enum DURATIONS {
        SMALL("S"),
        MEDIUM("D"),
        LONG("F");

        private final String duration;

        DURATIONS(String duration) {
            this.duration = duration;
        }

        public boolean equalsDuration(String otherDuration) {
            return duration.equals(otherDuration);
        }

        @NonNull
        @Override
        public String toString() {
            return duration;
        }
    }
}
