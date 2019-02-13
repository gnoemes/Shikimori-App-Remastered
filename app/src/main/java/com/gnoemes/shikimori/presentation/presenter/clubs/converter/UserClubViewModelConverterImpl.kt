package com.gnoemes.shikimori.presentation.presenter.clubs.converter

import android.content.Context
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.club.domain.Club
import com.gnoemes.shikimori.entity.club.domain.ClubCommentPolicy
import com.gnoemes.shikimori.entity.club.domain.ClubPolicy
import com.gnoemes.shikimori.entity.club.presentation.UserClubViewModel
import javax.inject.Inject

class UserClubViewModelConverterImpl @Inject constructor(
        private val context: Context
) : UserClubViewModelConverter {

    override fun apply(t: List<Club>): List<UserClubViewModel> = t.map { convertClub(it) }

    private fun convertClub(it: Club): UserClubViewModel {

        val descriptionBuilder = StringBuilder()

        if (it.isCensored) descriptionBuilder.append("18+")
        if (descriptionBuilder.isNotEmpty()) descriptionBuilder.append(" • ")
        descriptionBuilder.append(getLocalizedPolicy(it.policyJoin, it.policyComment))


        return UserClubViewModel(
                it.id,
                it.image,
                it.name,
                descriptionBuilder.toString(),
                it.isCensored
        )
    }

    private fun getLocalizedPolicy(join: ClubPolicy, disc: ClubCommentPolicy): String {
        return when {
            join == ClubPolicy.FREE && disc == ClubCommentPolicy.FREE -> context.getString(R.string.club_free)
            join == ClubPolicy.FREE && disc == ClubCommentPolicy.MEMBERS -> context.getString(R.string.club_free_join)
            disc == ClubCommentPolicy.FREE -> context.getString(R.string.club_free_discussion)
            join != ClubPolicy.FREE -> context.getString(R.string.club_invite)
            else -> ""
        }
    }
}