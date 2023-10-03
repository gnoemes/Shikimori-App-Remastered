package com.gnoemes.shikimori.entity.common.domain;

import androidx.annotation.NonNull;

public enum Genre {
    ACTION("action", "Экшен", 1, 56, false),
    ADVENTURE("adventure", "Приключения", 2, 68, false),
    CARS("cars", "Машины", 3, 84, false),
    COMEDY("comedy", "Комедия", 4, 49, false),
    DEMENTIA("dementia", "Безумие", 5, 90, false),
    DEMONS("demons", "Демоны", 6, 72, false),
    MYSTERY("mystery", "Детектив", 7, 46, false),
    DRAMA("drama", "Драма", 8, 50, false),
    ECCHI("ecchi", "Этти", 9, 51, false),
    FANTASY("fantasy", "Фэнтези", 10, 57, false),
    GAME("game", "Игры", 11, 79, false),
    HENTAI("hentai", "Хентай", 12, 59, true),
    HISTORICAL("historical", "Исторический", 13, 69, false),
    HORROR("horror", "Ужасы", 14, 80, false),
    KIDS("kids", "Детское", 15, 77, false),
    MAGIC("magic", "Магия", 16, 58, false),
    MARTIAL_ARTS("martial_arts", "Боевые исскуства", 17, 66, false),
    MECHA("mecha", "Меха", 18, 83, false),
    MUSIC("music", "Музыка", 19, 78, false),
    PARODY("parody", "Пародия", 20, 86, false),
    SAMURAI("samurai", "Самураи", 21, 88, false),
    ROMANCE("romance", "Романтика", 22, 62, false),
    SCHOOL("school", "Школа", 23, 60, false),
    SCI_FI("sci_fi", "Фантастика", 24, 53, false),
    SHOUJO("shoujo", "Сёдзе", 25, 63, false),
    SHOUJO_AI("shoujo_ai", "Сёдзе Ай", 26, 73, false),
    SHOUNEN("shounen", "Сёнен", 27, 47, false),
    SHOUNEN_AI("shounen_ai", "Сёнен Ай", 28, 55, false),
    SPACE("space", "Космос", 29, 85, false),
    SPORTS("sports", "Спорт", 30, 76, false),
    SUPER_POWER("super_power", "Супер силы", 31, 82, false),
    VAMPIRE("vampire", "Вампиры", 32, 64, false),
    YAOI("yaoi", "Яой", 33, 65, true),
    YURI("yuri", "Юри", 34, 75, true),
    HAREM("harem", "Гарем", 35, 71, false),
    SLICE_OF_LIFE("slice_of_life", "Повседневность", 36, 54, false),
    SUPERNATURAL("supernatural", "Сверхъестественное", 37, 48, false),
    MILITARY("military", "Военное", 38, 70, false),
    POLICE("police", "Полиция", 39, 89, false),
    PSYCHOLOGICAL("psychological", "Психологический", 40, 67, false),
    THRILLER("thriller", "Триллер", 41, 81, false),
    SEINEN("seinen", "Сейнен", 42, 52, false),
    JOSEI("josei", "Дзёсей", 43, 87, false),
    GENDER_BENDER("gender_bender", "Смена пола", -1, 74, false),
    DOUJINSHI("doujinshi", "Додзинси", -1, 61, false),
    GOURMET("gourmet", "Гурман", 543, 544, false),
    WORK_LIFE("work_life", "Работа", 541, 542, false),
    EROTICA("erotica", "Эротика", 539, 540, false);

    private final String name;
    private final String russianName;
    private final int animeId;
    private final int mangaId;
    private final boolean isR18;

    Genre(String name, String russianName, int animeId, int mangaId, boolean isR18) {
        this.name = name;
        this.russianName = russianName;
        this.animeId = animeId;
        this.mangaId = mangaId;
        this.isR18 = isR18;
    }

    public boolean equalsName(String otherName) {
        return name.equals(otherName);
    }

    public boolean equalsRussianName(String otherName) {
        return russianName.equals(otherName);
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }

    public String getAnimeId() {
        return String.valueOf(animeId);
    }

    public String getMangaId() {
        return String.valueOf(mangaId);
    }

    public String getRussianName() {
        return russianName;
    }

    public boolean getIsR18() {
        return isR18;
    }

    public boolean hasContentId(boolean isAnime) {
        return isAnime ? this.animeId != -1 : this.mangaId != -1;
    }
}
