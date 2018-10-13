package com.gnoemes.shikimori.presentation.view.base.fragment

import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.Router

interface RouterProvider {
    val localRouter: Router
    val localNavigator: Navigator
}