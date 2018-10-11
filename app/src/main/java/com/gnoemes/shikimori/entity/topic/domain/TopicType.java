package com.gnoemes.shikimori.entity.topic.domain;

public enum TopicType {
    NEWS("Topics::NewsTopic"),
    NEWS_LINK_ONLY("Topics::NewsTopic"),
    ANIME("Topics::EntryTopics::AnimeTopic"),
    MANGA("Topics::EntryTopics::MangaTopic"),
    PERSON("Topics::EntryTopics::PersonTopic"),
    CHARACTER("Topics::EntryTopics::CharacterTopic"),
    REVIEW("Topics::EntryTopics::ReviewTopic"),
    COSPLAY("Topics::EntryTopics::CosplayGalleryTopic"),
    CONTEST("Topics::EntryTopics::ContestTopic"),
    COLLECTION("Topics::EntryTopics::CollectionTopic"),
    CLUB("Topics::EntryTopics::ClubTopic"),
    CLUB_USER("Topics::ClubUserTopic"),
    DEFAULT("Topic");

    private final String type;

    TopicType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public boolean isEqualType(String otherType) {
        return type.equals(otherType);
    }
}
