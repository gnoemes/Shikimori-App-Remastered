package com.gnoemes.shikimori.data.repository.user

import com.gnoemes.shikimori.data.local.preference.UserSource
import com.gnoemes.shikimori.data.network.UserApi
import com.gnoemes.shikimori.data.repository.club.ClubResponseConverter
import com.gnoemes.shikimori.data.repository.user.converter.FavoriteListResponseConverter
import com.gnoemes.shikimori.data.repository.user.converter.MessageResponseConverter
import com.gnoemes.shikimori.data.repository.user.converter.UserBriefResponseConverter
import com.gnoemes.shikimori.data.repository.user.converter.UserDetailsResponseConverter
import com.gnoemes.shikimori.data.repository.user.converter.UserHistoryConverter
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.club.data.ClubResponse
import com.gnoemes.shikimori.entity.club.domain.Club
import com.gnoemes.shikimori.entity.common.domain.Image
import com.gnoemes.shikimori.entity.user.data.FavoriteListResponse
import com.gnoemes.shikimori.entity.user.data.MessageResponse
import com.gnoemes.shikimori.entity.user.data.UserBriefResponse
import com.gnoemes.shikimori.entity.user.data.UserDetailsResponse
import com.gnoemes.shikimori.entity.user.data.UserHistoryResponse
import com.gnoemes.shikimori.entity.user.data.UserImageResponse
import com.gnoemes.shikimori.entity.user.domain.FavoriteList
import com.gnoemes.shikimori.entity.user.domain.Message
import com.gnoemes.shikimori.entity.user.domain.MessageType
import com.gnoemes.shikimori.entity.user.domain.UserBrief
import com.gnoemes.shikimori.entity.user.domain.UserDetails
import com.gnoemes.shikimori.entity.user.domain.UserHistory
import com.gnoemes.shikimori.entity.user.domain.UserImage
import com.gnoemes.shikimori.entity.user.domain.UserStat
import com.gnoemes.shikimori.entity.user.domain.UserStats
import com.gnoemes.shikimori.entity.user.domain.UserStatus
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Single
import org.joda.time.DateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("UserRepositoryImpl")
class UserRepositoryImplTest {

    private val api: UserApi = mockk()
    private val userSource: UserSource = mockk()
    private val converter: UserBriefResponseConverter = mockk()
    private val detailsConverter: UserDetailsResponseConverter = mockk()
    private val historyConverter: UserHistoryConverter = mockk()
    private val favoriteConverter: FavoriteListResponseConverter = mockk()
    private val clubConverter: ClubResponseConverter = mockk()
    private val messageConverter: MessageResponseConverter = mockk()
    private lateinit var repository: UserRepositoryImpl

    private val userId = 42L
    private val now = DateTime.now()

    private val sampleUserBrief = UserBrief(
        id = userId, nickname = "TestUser", avatar = "https://avatar.example.com",
        image = UserImage(
            x160 = null, x148 = null, x80 = null,
            x64 = null, x48 = null, x32 = null, x16 = null
        ),
        dateLastOnline = now, name = "Test", sex = "male", website = null,
        dateBirth = null, locale = "ru"
    )

    @BeforeEach
    fun setup() {
        repository = UserRepositoryImpl(
            api = api,
            userSource = userSource,
            converter = converter,
            detailsConverter = detailsConverter,
            historyConverter = historyConverter,
            favoriteConverter = favoriteConverter,
            clubConverter = clubConverter,
            messageConverter = messageConverter
        )
    }

    @Nested
    @DisplayName("getMyUserId")
    inner class GetMyUserId {

        @Test
        fun `should return cached userId when not NO_ID`() {
            every { userSource.getUserId() } returns userId

            val result = repository.getMyUserId().blockingGet()

            assertEquals(userId, result)
            verify(exactly = 0) { api.getCurrentUserBrief() }
        }

        @Test
        fun `should fetch brief when cached userId is NO_ID`() {
            val response = UserBriefResponse(
                id = userId, nickname = "TestUser", avatar = "https://avatar.example.com",
                image = UserImageResponse(
                    x160 = null, x148 = null, x80 = null,
                    x64 = null, x48 = null, x32 = null, x16 = null
                ),
                dateLastOnline = now, name = "Test", sex = "male",
                website = null, dateBirth = null, locale = "ru"
            )

            every { userSource.getUserId() } returns Constants.NO_ID
            every { api.getCurrentUserBrief() } returns Single.just(response)
            every { converter.convertResponse(response) } returns sampleUserBrief

            val result = repository.getMyUserId().blockingGet()

            assertEquals(userId, result)
            verify { api.getCurrentUserBrief() }
            verify { converter.convertResponse(response) }
        }
    }

    @Nested
    @DisplayName("getMyUserBrief")
    inner class GetMyUserBrief {

        @Test
        fun `should return cached user when authorized`() {
            every { userSource.getUserStatus() } returns UserStatus.AUTHORIZED
            every { userSource.getUser() } returns sampleUserBrief

            val result = repository.getMyUserBrief().blockingGet()

            assertEquals(sampleUserBrief, result)
            verify(exactly = 0) { api.getCurrentUserBrief() }
        }

        @Test
        fun `should fetch from API when not cached and save result`() {
            val response = UserBriefResponse(
                id = userId, nickname = "TestUser", avatar = "https://avatar.example.com",
                image = UserImageResponse(
                    x160 = null, x148 = null, x80 = null,
                    x64 = null, x48 = null, x32 = null, x16 = null
                ),
                dateLastOnline = now, name = "Test", sex = "male",
                website = null, dateBirth = null, locale = "ru"
            )

            every { userSource.getUserStatus() } returns UserStatus.GUEST
            every { api.getCurrentUserBrief() } returns Single.just(response)
            every { converter.convertResponse(response) } returns sampleUserBrief

            val result = repository.getMyUserBrief().blockingGet()

            assertEquals(sampleUserBrief, result)
            verify { api.getCurrentUserBrief() }
            verify { userSource.setUser(sampleUserBrief) }
            verify { userSource.setUserId(userId) }
        }
    }

    @Nested
    @DisplayName("getUserMessages")
    inner class GetUserMessages {

        @Test
        fun `should fetch messages after getting current user`() {
            val responseList = listOf<MessageResponse>(mockk())
            val expectedMessages = listOf(
                Message(
                    id = 1L, type = MessageType.INBOX, read = false,
                    body = "Hello", htmlBody = "<p>Hello</p>",
                    dateCreated = now, linked = null,
                    userFrom = sampleUserBrief,
                    userTo = sampleUserBrief
                )
            )

            every { userSource.getUserId() } returns userId
            every { api.getUserMessages(userId, MessageType.INBOX) } returns Single.just(responseList)
            every { messageConverter.apply(responseList) } returns expectedMessages

            val result = repository.getUserMessages(MessageType.INBOX).blockingGet()

            assertEquals(expectedMessages, result)
            verify { api.getUserMessages(userId, MessageType.INBOX) }
            verify { messageConverter.apply(responseList) }
        }
    }

    @Nested
    @DisplayName("getDetails")
    inner class GetDetails {

        @Test
        fun `should fetch user profile from API and convert`() {
            val response = mockk<UserDetailsResponse>()
            val expectedDetails = mockk<UserDetails>()

            every { api.getUserProfile(userId) } returns Single.just(response)
            every { detailsConverter.convertResponse(response) } returns expectedDetails

            val result = repository.getDetails(userId).blockingGet()

            assertEquals(expectedDetails, result)
            verify { api.getUserProfile(userId) }
            verify { detailsConverter.convertResponse(response) }
        }
    }

    @Nested
    @DisplayName("getFriends")
    inner class GetFriends {

        @Test
        fun `should fetch friends from API and convert`() {
            val responseList = listOf<UserBriefResponse>(mockk())
            val expectedFriends = listOf(sampleUserBrief)

            every { api.getUserFriends(userId) } returns Single.just(responseList)
            every { converter.apply(responseList) } returns expectedFriends

            val result = repository.getFriends(userId).blockingGet()

            assertEquals(expectedFriends, result)
            verify { api.getUserFriends(userId) }
            verify { converter.apply(responseList) }
        }
    }

    @Nested
    @DisplayName("getFavorites")
    inner class GetFavorites {

        @Test
        fun `should fetch favorites from API and convert`() {
            val response = mockk<FavoriteListResponse>()
            val expectedFavorites = mockk<FavoriteList>()

            every { api.getUserFavourites(userId) } returns Single.just(response)
            every { favoriteConverter.apply(response) } returns expectedFavorites

            val result = repository.getFavorites(userId).blockingGet()

            assertEquals(expectedFavorites, result)
            verify { api.getUserFavourites(userId) }
            verify { favoriteConverter.apply(response) }
        }
    }

    @Nested
    @DisplayName("getClubs")
    inner class GetClubs {

        @Test
        fun `should fetch clubs from API and convert`() {
            val responseList = listOf<ClubResponse>(mockk())
            val expectedClubs = listOf(
                Club(
                    id = 1L, name = "Test Club",
                    image = Image(original = null, preview = null, x96 = null, x48 = null),
                    isCensored = false,
                    policyJoin = com.gnoemes.shikimori.entity.club.domain.ClubPolicy.FREE,
                    policyComment = com.gnoemes.shikimori.entity.club.domain.ClubCommentPolicy.FREE
                )
            )

            every { api.getUserClubs(userId) } returns Single.just(responseList)
            every { clubConverter.apply(responseList) } returns expectedClubs

            val result = repository.getClubs(userId).blockingGet()

            assertEquals(expectedClubs, result)
            verify { api.getUserClubs(userId) }
            verify { clubConverter.apply(responseList) }
        }
    }

    @Nested
    @DisplayName("getHistory")
    inner class GetHistory {

        @Test
        fun `should fetch history from API, convert, and trim to limit`() {
            val responseList = listOf<UserHistoryResponse>(mockk(), mockk(), mockk())
            val convertedHistory = listOf(
                UserHistory(1L, now, "Watched", null),
                UserHistory(2L, now, "Watched", null),
                UserHistory(3L, now, "Watched", null)
            )

            every { api.getUserHistory(userId, 1, 2) } returns Single.just(responseList)
            every { historyConverter.apply(responseList) } returns convertedHistory

            val result = repository.getHistory(userId, 1, 2).blockingGet()

            assertEquals(2, result.size)
            assertEquals(convertedHistory.take(2), result)
            verify { api.getUserHistory(userId, 1, 2) }
            verify { historyConverter.apply(responseList) }
        }

        @Test
        fun `should return full list when less items than limit`() {
            val responseList = listOf<UserHistoryResponse>(mockk())
            val convertedHistory = listOf(
                UserHistory(1L, now, "Watched", null)
            )

            every { api.getUserHistory(userId, 1, 10) } returns Single.just(responseList)
            every { historyConverter.apply(responseList) } returns convertedHistory

            val result = repository.getHistory(userId, 1, 10).blockingGet()

            assertEquals(1, result.size)
        }
    }

    @Nested
    @DisplayName("social actions")
    inner class SocialActions {

        @Test
        fun `ignore should call API`() {
            every { api.ignoreUser(userId) } returns Completable.complete()
            repository.ignore(userId).blockingAwait()
            verify { api.ignoreUser(userId) }
        }

        @Test
        fun `unignore should call API`() {
            every { api.unignoreUser(userId) } returns Completable.complete()
            repository.unignore(userId).blockingAwait()
            verify { api.unignoreUser(userId) }
        }

        @Test
        fun `addToFriends should call API`() {
            every { api.addToFriends(userId) } returns Completable.complete()
            repository.addToFriends(userId).blockingAwait()
            verify { api.addToFriends(userId) }
        }

        @Test
        fun `removeFriend should call API`() {
            every { api.deleteFriend(userId) } returns Completable.complete()
            repository.removeFriend(userId).blockingAwait()
            verify { api.deleteFriend(userId) }
        }
    }

    @Nested
    @DisplayName("user status management")
    inner class UserStatusManagement {

        @Test
        fun `getUserStatus should delegate to userSource`() {
            every { userSource.getUserStatus() } returns UserStatus.AUTHORIZED
            assertEquals(UserStatus.AUTHORIZED, repository.getUserStatus())
        }

        @Test
        fun `setUserStatus should delegate to userSource`() {
            repository.setUserStatus(UserStatus.GUEST)
            verify { userSource.setUserStatus(UserStatus.GUEST) }
        }

        @Test
        fun `clearUser should delegate to userSource`() {
            repository.clearUser()
            verify { userSource.clearUser() }
        }
    }
}
