package com.example.lumaka.domain.model

import androidx.annotation.StringRes
import com.example.lumaka.R

enum class CategoryEnum (val id: Int, @param:StringRes val title: Int) {
    ALL (id = 0, title = R.string.category_all),
    GENERAL (id = 1, title = R.string.category_general),
    WORK (id = 2, title = R.string.category_work),
    HOUSEHOLD (id = 3, title = R.string.category_household),
    SCHOOL (id = 4, title = R.string.category_school),
    SELFCARE (id = 5, title = R.string.category_selfcare),
    GYM (id = 6, title = R.string.category_gym),
}