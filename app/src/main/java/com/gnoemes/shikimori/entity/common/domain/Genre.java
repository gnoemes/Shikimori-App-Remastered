package com.gnoemes.shikimori.entity.common.domain;

import androidx.annotation.NonNull;

public enum Genre {
    ACTION("action", "Экшен", 1, 56),
    ADVENTURE("adventure", "Приключения", 2, 68),
    CARS("cars", "Машины", 3, 84),
    COMEDY("comedy", "Комедия", 4, 49),
    DEMENTIA("dementia", "Безумие", 5, 90),
    DEMONS("demons", "Демоны", 6, 72),
    MYSTERY("mystery", "Детектив", 7, 46),
    DRAMA("drama", "Драма", 8, 50),
    ECCHI("ecchi", "Этти", 9, 51),
    FANTASY("fantasy", "Фэнтези", 10, 57),
    GAME("game", "Игры", 11, 79),
    HENTAI("hentai", "Хентай", 12, 59),
    HISTORICAL("historical", "Исторический", 13, 69),
    HORROR("horror", "Ужасы", 14, 80),
    KIDS("kids", "Детское", 15, 77),
    MAGIC("magic", "Магия", 16, 58),
    MARTIAL_ARTS("martial_arts", "Боевые исскуства", 17, 66),
    MECHA("mecha", "Меха", 18, 83),
    MUSIC("music", "Музыка", 19, 78),
    PARODY("parody", "Пародия", 20, 86),
    SAMURAI("samurai", "Самураи", 21, 88),
    ROMANCE("romance", "Романтика", 22, 62),
    SCHOOL("school", "Школа", 23, 60),
    SCI_FI("sci_fi", "Фантастика", 24, 53),
    SHOUJO("shoujo", "Сёдзе", 25, 63),
    SHOUJO_AI("shoujo_ai", "Сёдзе Ай", 26, 73),
    SHOUNEN("shounen", "Сёнен", 27, 47),
    SHOUNEN_AI("shounen_ai", "Сёнен Ай", 28, 55),
    SPACE("space", "Космос", 29, 85),
    SPORTS("sports", "Спорт", 30, 76),
    SUPER_POWER("super_power", "Супер силы", 31, 82),
    VAMPIRE("vampire", "Вампиры", 32, 64),
    YAOI("yaoi", "Яойчик", 33, 65),
    YURI("yuri", "Юри", 34, 75),
    HAREM("harem", "Гарем", 35, 71),
    SLICE_OF_LIFE("slice_of_life", "Повседневность", 36, 54),
    SUPERNATURAL("supernatural", "Сверхъестественное", 37, 48),
    MILITATY("military", "Военное", 38, 70),
    POLICE("police", "Полиция", 39, 89),
    PSYCHOLOGICAL("psychological", "Психологический", 40, 67),
    THRILLER("thriller", "Триллер", 41, 81),
    SEINEN("seinen", "Сейнен", 42, 52),
    JOSEI("josei", "Дзёсей", 43, 87),
    GENDER_BENDER("gender_bender", "Смена пола", -1, 74),
    DOUJINSHI("doujinshi", "Додзинси", -1, 61);

    private final String name;
    private final String russianName;
    private final int animeId;
    private final int mangaId;

    Genre(String name, String russianName, int animeId, int mangaId) {
        this.name = name;
        this.russianName = russianName;
        this.animeId = animeId;
        this.mangaId = mangaId;
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
}
